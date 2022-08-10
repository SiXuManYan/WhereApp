package com.jcs.where.features.gourmet.restaurant.list

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jcs.where.R
import com.jcs.where.api.response.area.AreaResponse
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.bean.RestaurantListRequest
import com.jcs.where.features.gourmet.cart.ShoppingCartActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.gourmet.restaurant.list.filter.more.MoreFilterFragment.MoreFilter
import com.jcs.where.features.gourmet.takeaway.TakeawayActivity
import com.jcs.where.features.map.HotelCustomInfoWindowAdapter
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.utils.*
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_gourmet_list.*
import kotlinx.android.synthetic.main.activity_gourmet_list.back_iv
import kotlinx.android.synthetic.main.activity_restaurant_detail_2.*
import kotlinx.android.synthetic.main.layout_filter.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * Created by Wangsw  2021/3/24 13:56.
 * 美食首页
 */
class RestaurantHomeActivity : BaseMvpActivity<RestaurantHomePresenter>(), RestaurantHomeView {


    private var mCategoryId = 0
    private var mPidName = ""

    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var emptyView: EmptyView
    private lateinit var mRequest: RestaurantListRequest

    /** 列表 */
    private lateinit var mAdapter: DelicacyAdapter

    /** marker 选中后弹出的 item */
    private lateinit var mMarkerContentAdapter: DelicacyAdapter


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


    override fun getLayoutId() = R.layout.activity_gourmet_list


    /** 处理搜索 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bundle = it.data?.extras
            mRequest.search_input = bundle?.getString(Constant.PARAM_NAME, "")
            search()
        }
    }


    override fun initView() {
        initExtra()
        initFilter()
        initList()
        initMap()
        initBehavior()
        initMarkerClickListContent()
    }


    private fun initExtra() {
        val bundle = intent.extras ?: return
        mCategoryId = bundle.getInt(Constant.PARAM_PID, 89)
        mPidName = bundle.getString(Constant.PARAM_PID_NAME, "")
        food_tv.text = mPidName
    }

    private fun initFilter() {
        filter_pager.offscreenPageLimit = 2
        val adapter = RestaurantPagerAdapter(supportFragmentManager, 0)
        adapter.pid = mCategoryId
        adapter.pidName = mPidName
        filter_pager.adapter = adapter
    }


    private fun initList() {

        // 列表
        emptyView = EmptyView(this).apply {
            setEmptyImage(R.mipmap.ic_empty_search)
            setEmptyMessage(R.string.empty_search_message)
            setEmptyHint(R.string.empty_search_hint)
            addEmptyList(this)
        }

        mAdapter = DelicacyAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            addChildClickViewIds(R.id.takeaway_support_tv)
            setOnItemChildClickListener { _, _, position ->
                onItemChildClick(this, position)
            }
            setOnItemClickListener { _, _, position ->
                onItemClick(this, position)
            }
            loadMoreModule.setOnLoadMoreListener(this@RestaurantHomeActivity)
        }
        recycler.apply {
            adapter = mAdapter
            addItemDecoration(
                DividerDecoration(
                    Color.TRANSPARENT,
                    SizeUtils.dp2px(16f),
                    SizeUtils.dp2px(8f),
                    SizeUtils.dp2px(8f)
                ).apply {
                    setDrawHeaderFooter(false)
                })
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

    }


    private fun initMap() {
        // 获取 SupportMapFragment 并在地图准备好使用时请求通知
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }

    private fun initBehavior() {

        makerBehavior = BottomSheetBehavior.from(bottom_sheet_rv)
        makerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        makerBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            override fun onStateChanged(bottomSheet: View, newState: Int) = when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    recycler.visibility = View.GONE
                    swipe_layout.isEnabled = false
                }
                BottomSheetBehavior.STATE_HIDDEN ->{
                    recycler.visibility = View.VISIBLE
                    swipe_layout.isEnabled =!recycler.canScrollVertically(-1)
                }
                else -> {
                }
            }
        })

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                swipe_layout.isEnabled = (!recycler.canScrollVertically(-1) && !contentIsMap)
            }
        })
    }


    private fun initMarkerClickListContent() {
        mMarkerContentAdapter = DelicacyAdapter().apply {
            addChildClickViewIds(R.id.takeaway_ll)
            setOnItemChildClickListener { _, _, position ->
                onItemChildClick(this, position)
            }
            setOnItemClickListener { _, _, position ->
                onItemClick(this, position)
            }
        }

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(bottom_sheet_rv)

        bottom_sheet_rv.apply {
            adapter = mMarkerContentAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        bottom_sheet_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (newState == RecyclerView.SCROLL_STATE_IDLE && makers.size > firstItemPosition) {
                    val marker = makers[firstItemPosition]
                    marker?.let {
                        onMarkerClick(it)
                    }
                }
            }
        })


    }


    override fun initData() {
        presenter = RestaurantHomePresenter(this)
        mRequest = RestaurantListRequest().apply {
            category_id = mCategoryId

            val latLng = CacheUtil.getSafeSelectLatLng()

            lat = latLng.latitude
            lng = latLng.longitude
        }
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getList(page, mRequest)
    }

    override fun bindListener() {

        swipe_layout.setOnRefreshListener {

            when {
                mMarkerContentAdapter.data.isEmpty() ->{
                    presenter.getMakerData(mRequest)
                }
            }
            page = Constant.DEFAULT_FIRST_PAGE
            presenter.getList(page, mRequest)
        }

        back_iv.setOnClickListener { finish() }
        delete_iv.setOnClickListener { onClearSearchClick() }
        area_filter_ll.setOnClickListener { onAreaFilterClick() }
        food_filter_ll.setOnClickListener { onFoodFilterClick() }
        other_filter_ll.setOnClickListener { onOtherFilterClick() }
        dismiss_view.setOnClickListener { onFilterDismissClick() }
        filter_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageSelected(position: Int) {
                for (i in 0 until category_ll.childCount) {
                    changeFilterTabStyle(position, i)
                }
            }
        })

        search_tv.setOnClickListener {
            searchLauncher.launch(Intent(this, SearchAllActivity::class.java).putExtra(Constant.PARAM_TYPE, 7))
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

        cart_iv.setOnClickListener {
            startActivityAfterLogin(ShoppingCartActivity::class.java)
        }
    }

    private fun search() {
        val searchInput = mRequest.search_input
        delete_iv.visibility = if (searchInput.isNullOrBlank()) {
            View.GONE
        } else {
            View.VISIBLE
        }
        search_tv.text = searchInput
        onRefresh()
    }

    private fun onClearSearchClick() {
        search_tv.text = ""
        if (mRequest.search_input.isNotBlank()) {
            mRequest.search_input = null
            onRefresh()
        }
    }

    fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getList(page, mRequest)
        presenter.getMakerData(mRequest)
    }

    override fun onLoadMore() {
        page++
        presenter.getList(page, mRequest)
    }

    fun onItemChildClick(adapter: DelicacyAdapter, position: Int) {

        val data = adapter.data[position]
        val bundle = Bundle()
        bundle.putString(Constant.PARAM_ID, data.id.toString())
        startActivity(TakeawayActivity::class.java, bundle)
    }

    fun onItemClick(adapter: DelicacyAdapter, position: Int) {
        val data = adapter.data[position]
        startActivity(RestaurantDetailActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ID, data.id)
        })
    }

    override fun bindList(data: MutableList<RestaurantResponse>, isLastPage: Boolean) {
        swipe_layout.isRefreshing = false
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
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
            mAdapter.setNewInstance(data)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mAdapter.addData(data)
            if (isLastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    /**
     * 区域筛选
     */
    private fun onAreaFilterClick() = switchFilterPager(0)

    /**
     * 美食类型筛选
     */
    private fun onFoodFilterClick() = switchFilterPager(1)

    /**
     * 其他筛选
     */
    private fun onOtherFilterClick() = switchFilterPager(2)

    private fun onFilterDismissClick() {
        handleFilterVisible(View.GONE)

        // 清空tab选中状态
        val childTabLL = category_ll.getChildAt(filter_pager.currentItem) as LinearLayout
        val tabText = childTabLL.getChildAt(0) as CheckedTextView
        val tabImage = childTabLL.getChildAt(1) as ImageView
        tabText.isChecked = false
        tabImage.setImageResource(R.mipmap.ic_arrow_filter_black)
    }

    private fun switchFilterPager(index: Int) {
        val currentItem = filter_pager.currentItem
        if (filter_container_ll.visibility == View.GONE) {
            handleFilterVisible(View.VISIBLE)
            // 切换tab状态
            changeFilterTabStyle(currentItem, index)
        } else {
            if (currentItem == index) {
                handleFilterVisible(View.GONE)

                // 清空tab选中状态
                val childTabLL = category_ll.getChildAt(index) as LinearLayout
                val tabText = childTabLL.getChildAt(0) as CheckedTextView
                val tabImage = childTabLL.getChildAt(1) as ImageView
                tabText.isChecked = false
                tabImage.setImageResource(R.mipmap.ic_arrow_filter_black)
            }
        }
        filter_pager.setCurrentItem(index, true)
    }

    private fun handleFilterVisible(visibility: Int) {
        filter_container_ll.visibility = visibility
    }

    private fun changeFilterTabStyle(pagerPosition: Int, tabIndex: Int) {
        val childTabLL = category_ll.getChildAt(tabIndex) as LinearLayout
        val tabText = childTabLL.getChildAt(0) as CheckedTextView
        val tabImage = childTabLL.getChildAt(1) as ImageView
        if (pagerPosition == tabIndex) {
            tabText.isChecked = true
            tabText.paint.isFakeBoldText = true
            tabImage.setImageResource(R.mipmap.ic_arrow_filter_blue)
        } else {
            tabText.isChecked = false
            tabText.paint.isFakeBoldText = false
            tabImage.setImageResource(R.mipmap.ic_arrow_filter_black)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        val data = baseEvent.data
        if (data is AreaResponse) {
            // 区域筛选
            mRequest.trading_area_id = data.id
            val name = data.name
            if (!TextUtils.isEmpty(name)) {
                area_tv.text = name
            }
        } else if (data is Category) {
            // 美食列别筛选
            val id = data.id
            if (id == 0) {
                mRequest.category_id = null
            } else {
                mRequest.category_id = id
            }
            val name = data.name
            if (!TextUtils.isEmpty(name)) {
                food_tv.text = name
            }
        } else if (data is MoreFilter) {
            // 其他筛选
            val more = data
            mRequest.per_price = more.per_price
            mRequest.service = more.service
            mRequest.sort = more.sort
        }
        onRefresh()
        dismiss_view.performClick()
    }

    override fun bindMakerList(response: MutableList<RestaurantResponse>) {
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

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 300))


        // 在地图上添加大量Marker
        addMarkersToMap(response)

        // 展示marker 列表数据
        mMarkerContentAdapter.setNewInstance(response)
    }

    /**
     * 向地图中添加 Maker
     */
    private fun addMarkersToMap(response: MutableList<RestaurantResponse>) {
        if (!::map.isInitialized || response.isEmpty()) return
        map.clear()
        makers.clear()

        response.forEach {

            val view = LayoutInflater.from(this).inflate(R.layout.custom_info_contents_2, null)
            val title_tv = view.findViewById<TextView>(R.id.title_tv)
            val image_iv = view.findViewById<ImageView>(R.id.image_iv)
            title_tv.text = it.title
            image_iv.setImageResource(R.mipmap.ic_marker_common_food)

            val maker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(it.lat, it.lng))
                    .title(it.title)
                    .snippet("")
                    .icon(BitmapDescriptorFactory.fromBitmap(ConvertUtils.view2Bitmap(view)))
            )
            maker?.tag = it
            makers.add(maker)
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        map.apply {

            // 移动到巴朗牙
            moveCamera(CameraUpdateFactory.newLatLng(LatLng(Constant.LAT, Constant.LNG)))


            // 调整内置UI padding 防止logo被遮挡
            setPadding(0, 0, 0, SizeUtils.dp2px(220f))

            // 自定义信息样式
            setInfoWindowAdapter(HotelCustomInfoWindowAdapter(this@RestaurantHomeActivity))

            // 点击标记
            setOnMarkerClickListener(this@RestaurantHomeActivity)

            // 点击标记信息窗口
            setOnInfoWindowClickListener(this@RestaurantHomeActivity)

            // 移动到我的位置
            setOnMyLocationButtonClickListener(this@RestaurantHomeActivity)

            // 点击我的位置
            setOnMyLocationClickListener(this@RestaurantHomeActivity)
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
        presenter.getMakerData(mRequest)

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
            override fun onGetAddress(address: Address) = Unit

            override fun onGetLocation(lat: Double, lng: Double) {
                CacheUtil.getShareDefault().put(Constant.SP_MY_LATITUDE, lat.toFloat())
                CacheUtil.getShareDefault().put(Constant.SP_MY_LONGITUDE, lng.toFloat())

                myLocation = CameraPosition.Builder().target(LatLng(lat, lng))
                    .zoom(20f)
                    .bearing(0f)
                    .tilt(0f)
                    .build()
            }
        }
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

    override fun onMarkerClick(marker: Marker): Boolean {

        // 所有 maker 设置成未选中
        makers.forEach {
            val makerTag = it?.tag as RestaurantResponse
            val view = LayoutInflater.from(this).inflate(R.layout.custom_info_contents_2, null)
            val title_tv = view.findViewById<TextView>(R.id.title_tv)
            title_tv.text = makerTag.title
            val image_iv = view.findViewById<ImageView>(R.id.image_iv)
            image_iv.setImageResource(R.mipmap.ic_marker_common_food)

            it.setIcon(BitmapDescriptorFactory.fromBitmap(ConvertUtils.view2Bitmap(view)))

        }
        marker.hideInfoWindow()

        // 设置当前选中
        val currentMakerTag = marker.tag as RestaurantResponse
        val view = LayoutInflater.from(this).inflate(R.layout.custom_info_contents_2, null)
        val title_tv = view.findViewById<TextView>(R.id.title_tv)
        title_tv.text = currentMakerTag.title
        val image_iv = view.findViewById<ImageView>(R.id.image_iv)
        image_iv.setImageResource(R.mipmap.ic_marker_select_food)
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


}