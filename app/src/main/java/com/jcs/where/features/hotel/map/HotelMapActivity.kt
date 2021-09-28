package com.jcs.where.features.hotel.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import biz.laenger.android.vpbs.BottomSheetUtils
import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior
import com.blankj.utilcode.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.hotel.HotelHomeRecommend
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.hotel.home.HotelHomeRecommendAdapter
import com.jcs.where.features.map.CustomInfoWindowAdapter
import com.jcs.where.features.map.HotelCustomInfoWindowAdapter
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocationUtil
import com.jcs.where.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_map_hotel.*

import java.util.*

/**
 * Created by Wangsw  2021/9/27 14:06.
 *  酒店地图
 */
class HotelMapActivity : BaseMvpActivity<HotelMapPresenter>(), HotelMapView {

    /** 酒店分类 id */
    private var categoryId = 0

    /** 搜索内容 */
    var searchInput: String? = null

    /** 星级 */
    var starLevel: String? = null

    /** 住宿类型ID */
    var hotelTypeIds: String? = null

    /** 价格区间 */
    var priceRange: String? = null

    /** 酒店分数 */
    var grade: String? = null

    /** 内容和 tab二级分类 */
    private lateinit var mPagerAdapter: HotelMapPagerAdapter

    /** marker 选中后，对应的列表内容 */
    private lateinit var mMarkerContentAdapter: HotelHomeRecommendAdapter

    /** pager Behavior */
    private lateinit var pagerBehavior: ViewPagerBottomSheetBehavior<LinearLayout>

    /** maker  Behavior */
    private lateinit var makerBehavior: BottomSheetBehavior<RecyclerView>

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_map_hotel

    companion object {

        fun navigation(
            context: Context, searchInput: String?, starLevel: String?,
            priceRange: String?, grade: String?
        ) {

            val bundle = Bundle().apply {
                putString(Constant.PARAM_SEARCH, searchInput)
                putString(Constant.PARAM_STAR_LEVEL, starLevel)
                putString(Constant.PARAM_PRICE_RANGE, priceRange)
                putString(Constant.PARAM_GRADE, grade)
            }
            val intent = Intent(context, HotelMapActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        initExtra()
        initMap()
        initCategory()
        initBehavior()
        initMarkerClickListContent()

    }




    private fun initExtra() {
        val bundle = intent.extras ?: return
        bundle.apply {
            searchInput = getString(Constant.PARAM_SEARCH)
            starLevel = getString(Constant.PARAM_STAR_LEVEL)
            hotelTypeIds = getString(Constant.PARAM_TYPE_ID)
            priceRange = getString(Constant.PARAM_PRICE_RANGE)
            grade = getString(Constant.PARAM_GRADE)
        }


    }

    private fun initCategory() {
        mPagerAdapter = HotelMapPagerAdapter(supportFragmentManager).apply {
            searchInput = this@HotelMapActivity.searchInput
            starLevel = this@HotelMapActivity.starLevel
            priceRange = this@HotelMapActivity.priceRange
            grade = this@HotelMapActivity.grade
        }

    }

    private fun initBehavior() {
        BottomSheetUtils.setupViewPager(content_vp)

        pagerBehavior = ViewPagerBottomSheetBehavior.from(bottom_sheet_ll)
        pagerBehavior.state = ViewPagerBottomSheetBehavior.STATE_EXPANDED

        makerBehavior = BottomSheetBehavior.from(bottom_sheet_rv)
        makerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        makerBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            override fun onStateChanged(bottomSheet: View, newState: Int) = when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> pagerBehavior.state = ViewPagerBottomSheetBehavior.STATE_COLLAPSED
                BottomSheetBehavior.STATE_HIDDEN -> pagerBehavior.state = ViewPagerBottomSheetBehavior.STATE_EXPANDED
                else -> {
                }
            }
        })

    }



    private fun initMarkerClickListContent() {
        mMarkerContentAdapter = HotelHomeRecommendAdapter().apply {

            setOnItemClickListener { _, _, position ->
                val data = this.data[position]
                // TODO 进入酒店详情
            }
        }


        bottom_sheet_rv.apply {
            adapter = mMarkerContentAdapter
            // 禁用横向滑动
            layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollHorizontally() = false
            }
        }
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(bottom_sheet_rv)

    }

    override fun initData() {
        presenter = HotelMapPresenter(this)
        presenter.getHotelChildCategory(categoryId)

    }


    override fun bindListener() {

    }

    override fun bindCategory(response: ArrayList<Category>) {
        // pager
        mPagerAdapter.category.addAll(response)
        mPagerAdapter.notifyDataSetChanged()
        content_vp.offscreenPageLimit = response.size
        content_vp.adapter = mPagerAdapter
    }

    // ################ 地图相关 ###################

    private lateinit var map: GoogleMap

    // 我的位置
    private lateinit var myLocation: CameraPosition

    // 地图上的所有maker
    private var makers: ArrayList<Marker?> = ArrayList()


    private fun initMap() {
        // 获取 SupportMapFragment 并在地图准备好使用时请求通知
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return

        map.apply {

            // 移动到巴朗牙
            moveCamera(CameraUpdateFactory.newLatLng(LatLng(Constant.LAT, Constant.LNG)))


            // 调整内置UI padding 防止logo被遮挡
            setPadding(0, 0, 0, SizeUtils.dp2px(120f))

            // 自定义信息样式
            setInfoWindowAdapter(HotelCustomInfoWindowAdapter(this@HotelMapActivity))

            // 点击标记
            setOnMarkerClickListener(this@HotelMapActivity)

            // 点击标记信息窗口
            setOnInfoWindowClickListener(this@HotelMapActivity)

            // 移动到我的位置
            setOnMyLocationButtonClickListener(this@HotelMapActivity)

            // 点击我的位置
            setOnMyLocationClickListener(this@HotelMapActivity)
            // 辅助功能模式，覆盖视图上的默认内容描述
            setContentDescription("Map with lots of markers.");
        }

        map.uiSettings.apply {
            isMapToolbarEnabled = true
            // 隐藏我的位置和图层
            // isMyLocationButtonEnabled = false
        }

        // 当前位置
        enableMyLocation()

        // 获得展示在地图上的数据
//        presenter.getMakerData(ID_GOVERNMENT)
    }


    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return

        PermissionUtils.permissionAny(
            this, {
                if (it) {
                    map.isMyLocationEnabled = true
                    getMyLocationInfo()
                }
            }, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }



    /**
     * 获取我的位置信息
     */
    private fun getMyLocationInfo() {
        LocationUtil.getInstance().addressCallback = object : LocationUtil.AddressCallback {
            override fun onGetAddress(address: Address) {
                val countryName = address.countryName //国家
                val adminArea = address.adminArea //省
                val locality = address.locality //市
                val subLocality = address.subLocality //区
                val featureName = address.featureName //街道
            }

            override fun onGetLocation(lat: Double, lng: Double) {
                CacheUtil.getShareDefault().put(Constant.SP_LATITUDE, lat.toFloat())
                CacheUtil.getShareDefault().put(Constant.SP_LONGITUDE, lng.toFloat())


                myLocation = CameraPosition.Builder().target(LatLng(lat, lng))
                    .zoom(20f)
                    .bearing(0f)
                    .tilt(0f)
                    .build()
            }
        }
    }

    /**
     * 移动到我的位置
     */
    override fun onMyLocationButtonClick(): Boolean {
        if (!::myLocation.isInitialized) return false
        map.animateCamera(CameraUpdateFactory.newCameraPosition(myLocation))
        return false
    }

    /**
     * 点击我的位置 maker
     */
    override fun onMyLocationClick(location: Location) {
        ToastUtils.showShort("Current location : \nlatitude:${location.latitude}" + "\nlongitude:${location.longitude}  ")
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * 点击标记上的信息窗口
     */
    override fun onInfoWindowClick(marker: Marker) = Unit


    override fun bindMakerList(response: MutableList<HotelHomeRecommend>) {
        if (!::map.isInitialized ) return

        if (response.isEmpty()) {
            map.clear()
            makers.clear()
            return
        }

        // 相机移动到maker范围
        val bounds = LatLngBounds.Builder()

        response.forEach {
            bounds.include(LatLng(it.lat, it.lng))
        }

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 300))


        // 在地图上添加大量Marker
        addMarkersToMap(response)

        // 展示marker 列表数据
        mMarkerContentAdapter.setNewInstance(response)


    }

    private fun addMarkersToMap(response: MutableList<HotelHomeRecommend>) {
        if (!::map.isInitialized || response.isEmpty()) return
        map.clear()
        makers.clear()

        response.forEach {

            val view = LayoutInflater.from(this).inflate(R.layout.custom_info_hotel_maker, null)
            val title_tv = view.findViewById<TextView>(R.id.title_tv)
            title_tv.text = StringUtils.getString(R.string.price_unit_format,it.price.toPlainString())

            val maker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(it.lat, it.lng))
                    .title(it.name)
                    .snippet("")
                    .icon(BitmapDescriptorFactory.fromBitmap(ConvertUtils.view2Bitmap(view)))
            )
            maker?.tag = it
            makers.add(maker)
        }

    }


}