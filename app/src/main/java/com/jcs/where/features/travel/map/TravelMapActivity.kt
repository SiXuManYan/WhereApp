package com.jcs.where.features.travel.map


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jcs.where.R
import com.jcs.where.api.response.CityPickerResponse
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.travel.TravelChild
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.map.HotelCustomInfoWindowAdapter
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.features.travel.detail.TravelDetailActivity
import com.jcs.where.features.travel.map.child.TravelChildAdapter
import com.jcs.where.features.travel.map.filter.TravelCategoryFilterAdapter
import com.jcs.where.features.travel.map.filter.TravelCityFilterAdapter
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocationUtil
import com.jcs.where.utils.PermissionUtils
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_travel_map.*
import kotlinx.android.synthetic.main.layout_travel_filter_content.*

/**
 * Created by Wangsw  2021/10/18 9:37.
 *  旅游地图
    区域Id 优先级最高，
    首次进来用经纬度请求，值为用户手动选择的经纬度（未选择时取默认值）
    选择全部城市时，（用经纬度请求）
    区域id为0 ，经纬度使用手动选择经纬度
    选择其他城市时，用区域id请求
 */
class TravelMapActivity : BaseMvpActivity<TravelMapPresenter>(), TravelMapView {

    /** 旅游模块分类id */
    private var travelCategoryId = 0


    /** 搜索内容 */
    var searchInput: String? = null


    /** marker 选中后弹出的 item */
    private lateinit var mMarkerContentAdapter: TravelMarkerSelectedAdapter

    /** pager Behavior */
    private lateinit var pagerBehavior: BottomSheetBehavior<RecyclerView>

    /** maker  Behavior */
    private lateinit var makerBehavior: BottomSheetBehavior<RecyclerView>

    /** 区分地图和列表模式 */
    private var contentIsMap = false

    /** 城市筛选 */
    private lateinit var cityFilterAdapter: TravelCityFilterAdapter

    /** 分类筛选 */
    private lateinit var categoryFilterAdapter: TravelCategoryFilterAdapter

    /** 纬度筛选 */
    var requestLatitude = 0.0

    /** 经度筛选 */
    var requestLongitude = 0.0

    var requestAreaId = "0"

    /** 内容列表空view */
    private lateinit var emptyView: EmptyView

    /** 内容列表 */
    private lateinit var mAdapter: TravelChildAdapter

    /** 内容列表 page */
    private var page = Constant.DEFAULT_FIRST_PAGE

    /** 当前参与请求的分类id */
    var currentRequestCategoryId = 0


    // ################ 地图相关 ###################

    private lateinit var map: GoogleMap

    // 我的位置
    private lateinit var myLocation: CameraPosition

    // 地图上的所有maker
    private var makers: ArrayList<Marker?> = ArrayList()

    private var needMoveCamera = false

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
        initFilter()
        initContentList()
    }

    /** 筛选 */
    @SuppressLint("NotifyDataSetChanged")
    private fun initFilter() {

        // 筛选列表
        categoryFilterAdapter = TravelCategoryFilterAdapter()
        category_filter_rv.apply {
            adapter = categoryFilterAdapter
            layoutManager = LinearLayoutManager(this@TravelMapActivity, LinearLayoutManager.VERTICAL, false)
        }

        cityFilterAdapter = TravelCityFilterAdapter()
        city_filter_rv.apply {
            adapter = cityFilterAdapter
            layoutManager = LinearLayoutManager(this@TravelMapActivity, LinearLayoutManager.VERTICAL, false)
        }

        cityFilterAdapter.setOnItemClickListener { _, _, position ->
            val data = cityFilterAdapter.data
            val item = data[position]
            data.forEach {
                it.nativeIsSelected = it.id == item.id
            }
            cityFilterAdapter.notifyDataSetChanged()

            selected_city_tv.text = item.name
            requestLatitude = item.lat
            requestLongitude = item.lng
            requestAreaId = item.id
            page = Constant.DEFAULT_FIRST_PAGE
            travel_filter_ll.visibility = View.GONE
            requestContentList()
            requestMakerList()
            needMoveCamera = true
        }

        categoryFilterAdapter.setOnItemClickListener { _, _, position ->
            val data = categoryFilterAdapter.data
            val item = data[position]
            data.forEach {
                it.nativeIsSelected = it.id == item.id
            }
            categoryFilterAdapter.notifyDataSetChanged()

            select_category_tv.text = item.name
            currentRequestCategoryId = item.id
            page = Constant.DEFAULT_FIRST_PAGE
            travel_filter_ll.visibility = View.GONE
            requestContentList()
            requestMakerList()
        }

        // UI内容切换
        select_category_ll.setOnClickListener {
            when (travel_filter_ll.visibility) {
                View.VISIBLE -> {
                    // 如果是当前类型的展开，直接关闭
                    if (filter_sw.displayedChild == 0) {
                        closeCategoryFilter()
                        travel_filter_ll.visibility = View.GONE
                    } else {
                        // 切换到当前类型
                        showCategoryFilter()
                    }
                }
                View.GONE -> showCategoryFilter()
                else -> {}
            }
        }

        selected_city_ll.setOnClickListener {
            when (travel_filter_ll.visibility) {
                View.VISIBLE -> {
                    // 如果是当前类型的展开，直接关闭
                    if (filter_sw.displayedChild == 1) {
                        closeCategoryFilter()
                        travel_filter_ll.visibility = View.GONE
                    } else {
                        // 切换到当前类型
                        showCityFilter()
                    }
                }
                View.GONE -> showCityFilter()
                else -> {}
            }

        }

        dismiss_view.setOnClickListener {
            select_category_iv.rotation = 0f
            selected_city_iv.rotation = 0f
            travel_filter_ll.visibility = View.GONE
        }

    }

    private fun closeCategoryFilter() {
        select_category_tv.isChecked = false
        select_category_iv.rotation = 0f
    }

    private fun showCategoryFilter() {
        travel_filter_ll.visibility = View.VISIBLE
        select_category_tv.isChecked = true
        select_category_iv.rotation = 180f
        filter_sw.displayedChild = 0
        closeCityFilter()
    }

    private fun closeCityFilter() {
        selected_city_tv.isChecked = false
        selected_city_iv.rotation = 0f
    }

    private fun showCityFilter() {
        travel_filter_ll.visibility = View.VISIBLE
        selected_city_tv.isChecked = true
        selected_city_iv.rotation = 180f
        filter_sw.displayedChild = 1
        closeCategoryFilter()
    }


    private fun initContentList() {

        emptyView = EmptyView(this).apply {
            setEmptyImage(R.mipmap.ic_empty_search)
            setEmptyMessage(R.string.empty_search_message)
            setEmptyHint(R.string.empty_search_hint)
        }

        mAdapter = TravelChildAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            setOnItemClickListener { _, _, position ->
                val data = mAdapter.data[position]
                TravelDetailActivity.navigation(this@TravelMapActivity, data.id)
            }
            loadMoreModule.setOnLoadMoreListener {
                page++
                requestContentList()
            }
        }

        bottom_sheet_content_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@TravelMapActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0).apply {
                    setDrawHeaderFooter(true)
                })

        }


    }


    private fun initExtra() {
        val bundle = intent.extras ?: return
        bundle.apply {
            travelCategoryId = getInt(Constant.PARAM_CATEGORY_ID)
        }

        // 首次进来，不传区域id ，只传经纬度（取上次选择）
        val safeSelectLatLng = CacheUtil.getSafeSelectLatLng()
        requestAreaId = "0"
        requestLatitude = safeSelectLatLng.latitude
        requestLongitude = safeSelectLatLng.longitude
    }


    private fun initMap() {
        // 获取 SupportMapFragment 并在地图准备好使用时请求通知
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun initBehavior() {

        pagerBehavior = BottomSheetBehavior.from(bottom_sheet_content_rv)
        pagerBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        makerBehavior = BottomSheetBehavior.from(bottom_sheet_rv)
        makerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        makerBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            override fun onStateChanged(bottomSheet: View, newState: Int) = when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> pagerBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                BottomSheetBehavior.STATE_HIDDEN -> pagerBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                else -> {
                }
            }
        })

    }

    private fun initMarkerClickListContent() {
        // 点击 marker 后显示的列表

        mMarkerContentAdapter = TravelMarkerSelectedAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val data = this.data[position]
                TravelDetailActivity.navigation(this@TravelMapActivity, data.id)
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
        presenter.getCityList()
        requestContentList()
    }

    override fun bindListener() {
        search_tv.setOnClickListener {
            searchLauncher.launch(Intent(this, SearchAllActivity::class.java).putExtra(Constant.PARAM_TYPE, 4))
            //  makerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        type_iv.setOnClickListener {

            if (contentIsMap) {
                type_iv.setImageResource(R.mipmap.ic_type_map)
                makerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                type_iv.setImageResource(R.mipmap.ic_type_list)
                makerBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            contentIsMap = !contentIsMap

        }


        back_iv.setOnClickListener {
            finish()
        }

        delete_iv.setOnClickListener {
            searchInput = null
            search()
        }

    }

    override fun bindSecondCategory(response: ArrayList<Category>) {
        categoryFilterAdapter.setNewInstance(response)
    }


    private fun search() {
        delete_iv.visibility = if (searchInput.isNullOrBlank()) {
            View.GONE
        } else {
            View.VISIBLE
        }
        search_tv.text = searchInput
        requestMakerList()

        page = Constant.DEFAULT_FIRST_PAGE
        requestContentList()
    }

    /** 请求内容列表  */
    private fun requestContentList() =
        presenter.getContentList(page, currentRequestCategoryId, searchInput, requestLatitude, requestLongitude, requestAreaId)


    // #############  地图相关 ####################

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

    }


    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return

        // 在地图上显示当前位置，镜头移动到当前位置
        PermissionUtils.permissionAny(
            this, {
                if (it) {
                    map.isMyLocationEnabled = true
                    getMyLocationInfo()
                }
            }, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        requestMakerList()
    }


    private fun requestMakerList() {
        if (!::map.isInitialized) return
        presenter.getMakerData(currentRequestCategoryId, searchInput, requestLatitude, requestLongitude)
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
                CacheUtil.getShareDefault().put(Constant.SP_MY_LATITUDE, lat.toFloat())
                CacheUtil.getShareDefault().put(Constant.SP_MY_LONGITUDE, lng.toFloat())

                // 镜头移动到当前位置
                myLocation = CameraPosition.Builder().target(LatLng(lat, lng))
                    .zoom(20f)
                    .bearing(0f)
                    .tilt(0f)
                    .build()
                animateCamera(LatLng(lat, lng))

            }
        }
    }


    override fun bindMakerList(response: MutableList<TravelChild>) {
        if (!::map.isInitialized) return

        if (response.isEmpty()) {
            ToastUtils.showShort(R.string.search_result_empty_hint)
            map.clear()
            makers.clear()
            return
        }

        // 相机移动到maker范围
        val bounds = LatLngBounds.Builder()

        response.forEach {
            bounds.include(LatLng(it.lat, it.lng))
        }

//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 300))


        // 在地图上添加大量Marker
        addMarkersToMap(response)

        // 展示marker 列表数据
        mMarkerContentAdapter.setNewInstance(response)

        if (needMoveCamera) {
            animateCamera(LatLng(requestLatitude, requestLongitude))
            needMoveCamera = false
        }
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
        animateCamera(marker.position)
        return false
    }

    private fun animateCamera(location: LatLng) {
        Handler(mainLooper).postDelayed({
            val targetCamera = CameraPosition.Builder().target(location)
                .zoom(13f)
                .bearing(0f)
                .tilt(0f)
                .build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(targetCamera))

        }, 10)
    }

    override fun onMyLocationButtonClick(): Boolean {
        if (!::myLocation.isInitialized) return false
        map.animateCamera(CameraUpdateFactory.newCameraPosition(myLocation))
        return false
    }

    override fun onMyLocationClick(location: Location) {
//        ToastUtils.showShort("Current location : \nlatitude:${location.latitude}" + "\nlongitude:${location.longitude}  ")
    }


    override fun onInfoWindowClick(marker: Marker) = Unit


    // ####################  筛选 ###############


    override fun bindContentList(response: MutableList<TravelChild>, lastPage: Boolean) {
        val loadMoreModule = mAdapter.loadMoreModule
        if (response.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyContainer()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter.setNewInstance(response)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()

        } else {
            mAdapter.addData(response)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun bindCityList(cityList: MutableList<CityPickerResponse.CityChild>) {
        cityFilterAdapter.setNewInstance(cityList)
    }

}