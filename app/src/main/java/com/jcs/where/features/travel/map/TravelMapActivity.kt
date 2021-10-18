package com.jcs.where.features.travel.map


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
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
import com.jcs.where.api.response.travel.TravelChild
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.map.HotelCustomInfoWindowAdapter
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocationUtil
import com.jcs.where.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_travel_map.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by Wangsw  2021/10/18 9:37.
 *  旅游地图
 */
class TravelMapActivity : BaseMvpActivity<TravelMapPresenter>(), TravelMapView {

    /** 旅游模块分类id */
    private var travelCategoryId = 0

    /** 当前参与请求的分类id */
    private var currentRequestCategoryId = 0

    /** 搜索内容 */
    var searchInput: String? = null

    /** 子列表 */
    private lateinit var mPagerAdapter: TravelPagerAdapter

    /** marker 选中后弹出的 item */
    private lateinit var mMarkerContentAdapter: TravelMarkerSelectedAdapter

    /** pager Behavior */
    private lateinit var pagerBehavior: ViewPagerBottomSheetBehavior<LinearLayout>

    /** maker  Behavior */
    private lateinit var makerBehavior: BottomSheetBehavior<RecyclerView>

    /** 区分地图和列表模式 */
    private var contentIsMap = false

    // ################ 地图相关 ###################

    private lateinit var map: GoogleMap

    // 我的位置
    private lateinit var myLocation: CameraPosition

    // 地图上的所有maker
    private var makers: ArrayList<Marker?> = ArrayList()

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_travel_map

    companion object {

        fun navigation(context: Context, categoryId: Int) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_CATEGORY_ID, categoryId)
            }
            val intent = Intent(context, TravelMapActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    /** 处理搜索 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bundle = it.data?.extras
            searchInput = bundle?.getString(Constant.PARAM_NAME, "")
            search()
        }
    }


    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        initExtra()
        initMap()
        initBehavior()
        initMarkerClickListContent()
    }


    private fun initExtra() {
        val bundle = intent.extras ?: return
        bundle.apply {
            travelCategoryId = getInt(Constant.PARAM_CATEGORY_ID)
        }
        mPagerAdapter = TravelPagerAdapter(supportFragmentManager)
    }

    private fun initMap() {
        // 获取 SupportMapFragment 并在地图准备好使用时请求通知
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
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
        mMarkerContentAdapter = TravelMarkerSelectedAdapter().apply {

            setOnItemClickListener { _, _, position ->
                val data = this.data[position]
                // todo 旅游详情
            }
        }


        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(bottom_sheet_rv)

        bottom_sheet_rv.apply {
            adapter = mMarkerContentAdapter
            // 禁用横向滑动
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    newState == RecyclerView.SCROLL_STATE_IDLE
                }
            })
        }

    }


    override fun initData() {
        presenter = TravelMapPresenter(this)
        presenter.getGovernmentChildCategory(travelCategoryId)


    }

    override fun bindListener() {
        search_tv.setOnClickListener {
            searchLauncher.launch(Intent(this, SearchAllActivity::class.java).putExtra(Constant.PARAM_TYPE, 4))
            makerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        type_iv.setOnClickListener {

            VibrateUtils.vibrate(50)
            if (contentIsMap) {
                type_iv.setImageResource(R.mipmap.ic_type_map)
                makerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                type_iv.setImageResource(R.mipmap.ic_type_list)
                makerBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            contentIsMap = !contentIsMap

        }
        content_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit


            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {
                if (mPagerAdapter.category.isEmpty()) return
                if (!::map.isInitialized) return

                val category = mPagerAdapter.category[position]

                currentRequestCategoryId = category.id
                presenter.getMakerData(currentRequestCategoryId, searchInput)
                makerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

        })

        back_iv.setOnClickListener {
            finish()
        }

        delete_iv.setOnClickListener {
            searchInput = null
            search()
        }

    }

    override fun bindSecondCategory(response: ArrayList<Category>) {
        // pager
        mPagerAdapter.category.addAll(response)
        mPagerAdapter.notifyDataSetChanged()
        content_vp.offscreenPageLimit = response.size
        content_vp.adapter = mPagerAdapter
        tabs_type.setViewPager(content_vp)
    }


    private fun search() {
        delete_iv.visibility = if (searchInput.isNullOrBlank()) {
            View.GONE
        } else {
            View.VISIBLE
        }
        search_tv.text = searchInput
        EventBus.getDefault().post(BaseEvent<String?>(EventCode.EVENT_REFRESH_CHILD, searchInput))
        presenter.getMakerData(currentRequestCategoryId, searchInput)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return

        map.apply {

            // 移动到巴朗牙
            moveCamera(CameraUpdateFactory.newLatLng(LatLng(Constant.LAT, Constant.LNG)))


            // 调整内置UI padding 防止logo被遮挡
            setPadding(0, 0, 0, SizeUtils.dp2px(220f))

            // 自定义信息样式
            setInfoWindowAdapter(HotelCustomInfoWindowAdapter(this@TravelMapActivity))

            // 点击标记
            setOnMarkerClickListener(this@TravelMapActivity)

            // 点击标记信息窗口
            setOnInfoWindowClickListener(this@TravelMapActivity)

            // 移动到我的位置
            setOnMyLocationButtonClickListener(this@TravelMapActivity)

            // 点击我的位置
            setOnMyLocationClickListener(this@TravelMapActivity)
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
        presenter.getMakerData(currentRequestCategoryId, searchInput)
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


    override fun bindMakerList(response: MutableList<TravelChild>) {
        if (!::map.isInitialized) return

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

    /**
     * 向地图中添加 Maker
     */
    private fun addMarkersToMap(response: MutableList<TravelChild>) {
        if (!::map.isInitialized || response.isEmpty()) return
        map.clear()
        makers.clear()

        response.forEach {

            val view = LayoutInflater.from(this).inflate(R.layout.custom_info_contents_2, null)
            val title_tv = view.findViewById<TextView>(R.id.title_tv)
            val image_iv = view.findViewById<ImageView>(R.id.image_iv)
            title_tv.text = it.name
            image_iv.setImageResource(R.mipmap.ic_marker_common_travel)

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


    override fun onMarkerClick(marker: Marker): Boolean {

        // 所有 maker 设置成未选中
        makers.forEach {
            val makerTag = it?.tag as TravelChild
            val view = LayoutInflater.from(this).inflate(R.layout.custom_info_contents_2, null)
            val title_tv = view.findViewById<TextView>(R.id.title_tv)
            title_tv.text = makerTag.name
            val image_iv = view.findViewById<ImageView>(R.id.image_iv)
            image_iv.setImageResource(R.mipmap.ic_marker_common_travel)

            it.setIcon(BitmapDescriptorFactory.fromBitmap(ConvertUtils.view2Bitmap(view)))

        }
        marker.hideInfoWindow()

        // 设置当前选中
        val currentMakerTag = marker.tag as TravelChild
        val view = LayoutInflater.from(this).inflate(R.layout.custom_info_contents_2, null)
        val title_tv = view.findViewById<TextView>(R.id.title_tv)
        title_tv.text = currentMakerTag.name
        val image_iv = view.findViewById<ImageView>(R.id.image_iv)
        image_iv.setImageResource(R.mipmap.ic_marker_select_travel)
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(ConvertUtils.view2Bitmap(view)))

        // 切换底部列表数据
        val index = makers.indexOf(marker)
        if (index != -1) {
            bottom_sheet_rv.scrollToPosition(index)
            makerBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        // 地图平滑移动到目标位置
        Handler(mainLooper).postDelayed({
            val targetCamera = CameraPosition.Builder().target(marker.position)
                .zoom(15.5f)
                .bearing(0f)
                .tilt(0f)
                .build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(targetCamera))

        }, 10)

        return false
    }

    override fun onMyLocationButtonClick(): Boolean {
        if (!::myLocation.isInitialized) return false
        map.animateCamera(CameraUpdateFactory.newCameraPosition(myLocation))
        return false
    }

    override fun onMyLocationClick(location: Location) {
        ToastUtils.showShort("Current location : \nlatitude:${location.latitude}" + "\nlongitude:${location.longitude}  ")
    }


    override fun onInfoWindowClick(marker: Marker) = Unit

}