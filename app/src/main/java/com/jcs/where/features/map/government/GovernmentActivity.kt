package com.jcs.where.features.map.government

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import biz.laenger.android.vpbs.BottomSheetUtils
import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.jcs.where.R
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.map.CustomInfoWindowAdapter
import com.jcs.where.features.map.MechanismAdapter
import com.jcs.where.features.map.MechanismPagerAdapter
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.features.store.filter.ThirdCategoryAdapter
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocationUtil
import com.jcs.where.utils.PermissionUtils
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_government.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Wangsw  2021/8/24 17:04.
 * 政府机构
 * @see <a href="https://github.com/SiXuManYan/android-samples/blob/main/ApiDemos/kotlin/app/src/gms/java/com/example/kotlindemos/MarkerDemoActivity.kt">MarkerDemoActivity.kt</a>
 * @see <a href="https://github.com/SiXuManYan/android-samples/blob/main/ApiDemos/java/app/src/gms/java/com/example/mapdemo/MarkerDemoActivity.java">MarkerDemoActivity.java</a>
 */
class GovernmentActivity : BaseMvpActivity<GovernmentPresenter>(), GovernmentView {


    /** 内容和 tab二级分类 */
    private lateinit var mPagerAdapter: MechanismPagerAdapter

    /** 三级分类 */
    private lateinit var mChildTagAdapter: ThirdCategoryAdapter

    /** pager Behavior */
    private lateinit var pagerBehavior: ViewPagerBottomSheetBehavior<LinearLayout>

    /** maker  Behavior */
    private lateinit var makerBehavior: BottomSheetBehavior<RecyclerView>

    /** marker 选中后，对应的列表内容 */
    private lateinit var mMarkerContentAdapter: MechanismAdapter

    /** 从分类tab跳转时，根据 categoryId  调整viewPager 位置 */
    private var childCategoryId = 0


    /** 处理搜索 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bundle = it.data?.extras
            val searchName = bundle?.getString(Constant.PARAM_NAME, "")
            search_tv.text = searchName
            EventBus.getDefault().post(BaseEvent<String>(EventCode.EVENT_REFRESH_CHILD, searchName))
        }
    }


    override fun getLayoutId() = R.layout.activity_government

    override fun isStatusDark() = true

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        initExtra()
        initCategoryChild()
        initBehavior()
        initMarkerClickListContent()
    }

    private fun initExtra() {
        childCategoryId = intent.getIntExtra(Constant.PARAM_CHILD_CATEGORY_ID, 0)
    }

    fun getTotalCountView(): TextView {
        return total_count_tv
    }

    /**
     * 三级分类
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun initCategoryChild() {

        mPagerAdapter = MechanismPagerAdapter(supportFragmentManager)

        mChildTagAdapter = ThirdCategoryAdapter().apply {
            setOnItemClickListener { adapter, view, position ->

                val child = mChildTagAdapter.data
                // 设置选中
                child.forEachIndexed { index, category ->
                    category.nativeIsSelected = index == position
                }
                mChildTagAdapter.notifyDataSetChanged()

                // 刷新筛选列表
                val target = mChildTagAdapter.data[position]
                EventBus.getDefault().post(BaseEvent<String>(EventCode.EVENT_REFRESH_MECHANISM, target.id.toString()))
            }
        }
        child_tag_rv.apply {
            adapter = mChildTagAdapter
            layoutManager = LinearLayoutManager(this@GovernmentActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(16f), 0, 0)
            )
        }
    }


    /**
     * 处理底部联动
     */
    private fun initBehavior() {
        BottomSheetUtils.setupViewPager(content_vp)
//        pagerBehavior = from(bottom_sheet_ll)
        pagerBehavior = ViewPagerBottomSheetBehavior.from(bottom_sheet_ll)

        pagerBehavior.state = ViewPagerBottomSheetBehavior.STATE_EXPANDED


        makerBehavior = from(bottom_sheet_rv)
        makerBehavior.state = STATE_HIDDEN
        makerBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    STATE_DRAGGING -> {
                        // 向上或者向下拖动bottom sheet
                    }
                    STATE_SETTLING -> {
                        // 视图从脱离手指自由滑动到最终停下的这一小段时间
                    }
                    STATE_EXPANDED -> {
                        //处于完全展开的状态
                        pagerBehavior.state = ViewPagerBottomSheetBehavior.STATE_COLLAPSED
                    }
                    STATE_COLLAPSED -> {
                        //默认的折叠状态
                    }
                    STATE_HIDDEN -> {
                        //下滑动完全隐藏
                        pagerBehavior.state = ViewPagerBottomSheetBehavior.STATE_EXPANDED
                    }

                    else -> {
                    }
                }


            }

        })

    }


    /**
     * Maker 选中后，对应的列表数据
     */
    private fun initMarkerClickListContent() {

        mMarkerContentAdapter = MechanismAdapter().apply {
            showClose = true
            addChildClickViewIds(R.id.close_iv)
            setOnItemChildClickListener { _, _, _ ->
                makerBehavior.state = STATE_HIDDEN
            }
            setOnItemClickListener { _, _, position ->
                val data = this.data[position]
                startActivity(MechanismActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ID, data.id)
                })
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
        presenter = GovernmentPresenter(this)
        initMap()
        presenter.apply {
            getGovernmentChildCategory()
        }

    }


    override fun bindListener() {

        back_iv.setOnClickListener { finish() }

        // tabLayout viewpager 联动
        content_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {

                mChildTagAdapter.setNewInstance(null)
                val category = mPagerAdapter.category
                if (category.isEmpty()) {
                    return
                }

                // 更新三级分类
                val child = category[position]
                if (child.has_children == 2 && child.child_categories.isNotEmpty()) {
                    mChildTagAdapter.setNewInstance(child.child_categories)
                }
                mPagerAdapter.currentPosition = position

            }
        })

        search_tv.setOnClickListener {
            searchLauncher.launch(Intent(this, SearchAllActivity::class.java).putExtra(Constant.PARAM_TYPE, 3))
        }

        delete_iv.setOnClickListener {
            search_tv.text = ""
            EventBus.getDefault().post(BaseEvent<String>(EventCode.EVENT_REFRESH_CHILD, ""))
        }
    }


    override fun bindSecondCategory(response: ArrayList<Category>) {

        // pager
        mPagerAdapter.category.addAll(response)
        mPagerAdapter.notifyDataSetChanged()
        content_vp.offscreenPageLimit = response.size
        content_vp.adapter = mPagerAdapter
        tabs_type.setViewPager(content_vp)

        // 滚动到目标位置
        if (childCategoryId > 0) {
            var targetIndex = 0
            response.forEachIndexed { index, category ->
                if (category.id == childCategoryId) {
                    targetIndex = index
                    return@forEachIndexed
                }
            }
            content_vp.currentItem = targetIndex
        }

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
            setInfoWindowAdapter(CustomInfoWindowAdapter(this@GovernmentActivity))

            // 点击标记
            setOnMarkerClickListener(this@GovernmentActivity)

            // 点击标记信息窗口
            setOnInfoWindowClickListener(this@GovernmentActivity)

            // 移动到我的位置
            setOnMyLocationButtonClickListener(this@GovernmentActivity)

            // 点击我的位置
            setOnMyLocationClickListener(this@GovernmentActivity)
            // 辅助功能模式，覆盖视图上的默认内容描述
            setContentDescription("Map with lots of markers.");
        }

        map.uiSettings.apply {
            isMapToolbarEnabled = true
            // 隐藏我的位置和图层
//            isMyLocationButtonEnabled = false

        }


        // 当前位置
        enableMyLocation()

        // 获得展示在地图上的数据
        presenter.getMakerData()

    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return

        PermissionUtils.permissionAny(
            this, {
                if (it) {
                    map.isMyLocationEnabled = true
                    moveMyLocation()
                }
            }, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    }

    /**
     * 获取我的位置信息
     */
    private fun moveMyLocation() {
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
                    .zoom(15.5f)
                    .bearing(0f)
                    .tilt(25f)
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
        ToastUtils.showShort("Current location:\n$location")
    }


    override fun bindMakerList(response: ArrayList<MechanismResponse>) {
        if (!::map.isInitialized || response.isEmpty()) return


        // 相机移动到maker范围
        val bounds = LatLngBounds.Builder()

        response.forEach {
            bounds.include(LatLng(it.lat, it.lng))
        }

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50))

        // 在地图上添加大量Marker
        addMarkersToMap(response)

        // 展示marker 列表数据
        mMarkerContentAdapter.setNewInstance(response)

    }

    /**
     * 向地图中添加 Maker
     */
    private fun addMarkersToMap(response: ArrayList<MechanismResponse>) {
        if (!::map.isInitialized || response.isEmpty()) return
        makers.clear()

        response.forEach {
            val maker = map.addMarker(
                MarkerOptions()
                    .position(LatLng(it.lat, it.lng))
                    .title(it.name)
                    .snippet("")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_common))
                // .infoWindowAnchor(0.5f, 0.5f) 调整标题和指针位置
            )

            maker?.tag = it
            makers.add(maker)
        }

    }


    /**
     * 点击标记
     */
    override fun onMarkerClick(marker: Marker): Boolean {

        // 切换 maker 图片
        makers.forEach {
            it?.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_common))
        }
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_select))
        marker.showInfoWindow()

        // 切换底部列表数据
        val index = makers.indexOf(marker)
        if (index != -1) {
            bottom_sheet_rv.scrollToPosition(index)
            makerBehavior.state = STATE_EXPANDED
        }

        Handler(mainLooper).postDelayed({

            // 地图平滑移动到目标位置
            val targetCamera = CameraPosition.Builder().target(marker.position)
                .zoom(15.5f)
                .bearing(0f)
                .tilt(25f)
                .build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(targetCamera))

        }, 10)



        return false
    }

    /**
     * 点击标记上的信息窗口
     */
    override fun onInfoWindowClick(marker: Marker) {

    }


}