package com.jcs.where.features.hotel.map

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatCheckedTextView
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
import com.jcs.where.api.response.hotel.HotelHomeRecommend
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.hotel.home.HotelHomeRecommendAdapter
import com.jcs.where.features.map.HotelCustomInfoWindowAdapter
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocationUtil
import com.jcs.where.utils.PermissionUtils
import com.jcs.where.widget.calendar.JcsCalendarAdapter
import com.jcs.where.widget.calendar.JcsCalendarDialog

import kotlinx.android.synthetic.main.activity_map_hotel.*


import org.greenrobot.eventbus.EventBus

import java.util.*

/**
 * Created by Wangsw  2021/9/27 14:06.
 *  酒店地图
 */
class HotelMapActivity : BaseMvpActivity<HotelMapPresenter>(), HotelMapView, JcsCalendarDialog.OnDateSelectedListener {

    /** 酒店分类 id ,用户获取酒店下的子分类 */
    private var hotelCategoryId = 0

    /** 当前参与请求的分类id */
    private var currentRequestCategoryId = 0

    /** 搜索内容 */
    var searchInput: String? = null

    /** 星级 */
    var starLevel: String? = null

    /** 价格区间 */
    var priceRange: String? = null

    /** 酒店分数 */
    var grade: String? = null


    /** 房间数量 */
    private var roomNumber = 1

    private lateinit var mJcsCalendarDialog: JcsCalendarDialog

    private lateinit var mStartDateBean: JcsCalendarAdapter.CalendarBean
    private lateinit var mEndDateBean: JcsCalendarAdapter.CalendarBean


    private var contentIsMap = false

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
            context: Context,
            hotelCategoryId: Int,
            starLevel: String? = null,
            priceRange: String? = null,
            grade: String? = null,
            roomNumber: Int = 1,
            startDate: JcsCalendarAdapter.CalendarBean,
            endDate: JcsCalendarAdapter.CalendarBean
        ) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_CATEGORY_ID, hotelCategoryId)
                putString(Constant.PARAM_STAR_LEVEL, starLevel)
                putString(Constant.PARAM_PRICE_RANGE, priceRange)
                putString(Constant.PARAM_GRADE, grade)
                putInt(Constant.PARAM_ROOM_NUMBER, roomNumber)
                putSerializable(Constant.PARAM_START_DATE, startDate)
                putSerializable(Constant.PARAM_END_DATE, endDate)
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


    /** 处理搜索 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bundle = it.data?.extras
            searchInput = bundle?.getString(Constant.PARAM_NAME, "")
            search()
        }
    }

    private fun search() {
        delete_iv.visibility = if (searchInput.isNullOrBlank()) {
            View.GONE
        } else {
            View.VISIBLE
        }
        search_tv.text = searchInput
        EventBus.getDefault().post(BaseEvent<String?>(EventCode.EVENT_REFRESH_CHILD, searchInput))
        // todo marker
        presenter.getMakerData(search_input = searchInput, star_level = starLevel, price_range = priceRange, currentRequestCategoryId)
    }


    private fun initExtra() {
        val bundle = intent.extras ?: return
        bundle.apply {
            hotelCategoryId = getInt(Constant.PARAM_CATEGORY_ID)
            starLevel = getString(Constant.PARAM_STAR_LEVEL)
            priceRange = getString(Constant.PARAM_PRICE_RANGE)
            grade = getString(Constant.PARAM_GRADE)
            mStartDateBean = getSerializable(Constant.PARAM_START_DATE) as JcsCalendarAdapter.CalendarBean
            mEndDateBean = getSerializable(Constant.PARAM_END_DATE) as JcsCalendarAdapter.CalendarBean
            roomNumber = getInt(Constant.PARAM_ROOM_NUMBER, 1)
            updateDate()
        }


    }

    private fun updateDate() {
        start_date_tv.text = mStartDateBean.showMonthDayDateWithSplit
        end_date_tv.text = mEndDateBean.showMonthDayDateWithSplit
    }

    private fun initCategory() {
        mPagerAdapter = HotelMapPagerAdapter(supportFragmentManager).apply {
            starLevel = this@HotelMapActivity.starLevel
            priceRange = this@HotelMapActivity.priceRange
            grade = this@HotelMapActivity.grade
            startDateBean = mStartDateBean
            endDateBean = mEndDateBean
            roomNumberCount = this@HotelMapActivity.roomNumber
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
                HotelDetailActivity2.navigation(
                    this@HotelMapActivity,
                    data.id,
                    mStartDateBean,
                    mEndDateBean,
                    starLevel,
                    priceRange,
                    grade,
                    roomNumber
                )

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
        mJcsCalendarDialog = JcsCalendarDialog().apply {
            initCalendar(this@HotelMapActivity)
            setOnDateSelectedListener(this@HotelMapActivity)

        }

        presenter = HotelMapPresenter(this)
        presenter.getHotelChildCategory(hotelCategoryId)
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
                presenter.getMakerData(
                    search_input = searchInput,
                    star_level = starLevel,
                    price_range = priceRange,
                    currentRequestCategoryId
                )
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

        date_ll.setOnClickListener {
            mJcsCalendarDialog.show(supportFragmentManager)
        }


    }

    override fun bindCategory(response: ArrayList<Category>) {
        // pager
        mPagerAdapter.category.addAll(response)
        mPagerAdapter.notifyDataSetChanged()
        content_vp.offscreenPageLimit = response.size
        content_vp.adapter = mPagerAdapter
        tabs_type.setViewPager(content_vp)
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
            setPadding(0, 0, 0, SizeUtils.dp2px(220f))

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
        presenter.getMakerData(search_input = searchInput, star_level = starLevel, price_range = priceRange, currentRequestCategoryId)
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


    override fun bindMakerList(response: MutableList<HotelHomeRecommend>) {
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

    private fun addMarkersToMap(response: MutableList<HotelHomeRecommend>) {
        if (!::map.isInitialized || response.isEmpty()) return
        map.clear()
        makers.clear()

        response.forEach {

            val view = LayoutInflater.from(this).inflate(R.layout.custom_info_hotel_maker_2, null)
            view.findViewById<AppCompatCheckedTextView>(R.id.title_tv).apply {
                text = StringUtils.getString(R.string.price_unit_format, it.price.toPlainString())
                isChecked = false
            }

            view.findViewById<ImageView>(R.id.triangle_iv).apply {
                setImageResource(R.drawable.shape_triangle_down_white)
            }

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


    /**
     * 点击标记上的信息窗口
     */
    override fun onInfoWindowClick(marker: Marker) = Unit

    override fun onMarkerClick(marker: Marker): Boolean {
        // 所有 maker 设置成未选中
        makers.forEach {
            val makerTag = it?.tag as HotelHomeRecommend
            val view = LayoutInflater.from(this).inflate(R.layout.custom_info_hotel_maker_2, null)
            view.findViewById<AppCompatCheckedTextView>(R.id.title_tv).apply {
                text = StringUtils.getString(R.string.price_unit_format, makerTag.price.toPlainString())
                isChecked = false
            }
            view.findViewById<ImageView>(R.id.triangle_iv).apply {
                setImageResource(R.drawable.shape_triangle_down_white)
            }

            it.setIcon(BitmapDescriptorFactory.fromBitmap(ConvertUtils.view2Bitmap(view)))
        }
        marker.hideInfoWindow()

        // 设置当前选中
        val makerTag = marker.tag as HotelHomeRecommend
        val view = LayoutInflater.from(this).inflate(R.layout.custom_info_hotel_maker_2, null)
        view.findViewById<AppCompatCheckedTextView>(R.id.title_tv).apply {
            text = StringUtils.getString(R.string.price_unit_format, makerTag.price.toPlainString())
            isChecked = true
        }
        view.findViewById<ImageView>(R.id.triangle_iv).apply {
            setImageResource(R.drawable.shape_triangle_down_blue)
        }

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

    override fun onDateSelected(startDate: JcsCalendarAdapter.CalendarBean, endDate: JcsCalendarAdapter.CalendarBean) {
        mStartDateBean = startDate
        mEndDateBean = endDate
        updateDate()
    }


}