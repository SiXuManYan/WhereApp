package com.jcs.where.features.map.government

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Location
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.map.MechanismPagerAdapter
import com.jcs.where.features.store.filter.ThirdCategoryAdapter
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocationUtil
import com.jcs.where.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_government.*
import kotlinx.android.synthetic.main.activity_government.tabs_type
import kotlinx.android.synthetic.main.fragment_home3.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by Wangsw  2021/8/24 17:04.
 * 政府机构
 */
class GovernmentActivity : BaseMvpActivity<GovernmentPresenter>(), GovernmentView,
    OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {


    // 内容和 tab二级分类
    private lateinit var mPagerAdapter: MechanismPagerAdapter

    // 三级分类
    private lateinit var mChildTagAdapter: ThirdCategoryAdapter

    override fun getLayoutId() = R.layout.activity_government

    override fun isStatusDark() = true

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        initChild()
        initMap()
    }


    private fun initChild() {
        mPagerAdapter = MechanismPagerAdapter(supportFragmentManager)
        mChildTagAdapter = ThirdCategoryAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val child = mChildTagAdapter.data
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
        }
    }


    override fun initData() {
        presenter = GovernmentPresenter(this)
        presenter.getGovernmentChildCategory()
    }

    override fun bindListener() {

        back_iv.setOnClickListener { finish() }

        content_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {

                mChildTagAdapter.setNewInstance(null)
                val category = mPagerAdapter.category
                if (category.isEmpty()) {
                    return
                }
                val child = category[position]
                if (child.has_children == 2 && child.child_categories.isNotEmpty()) {
                    mChildTagAdapter.setNewInstance(child.child_categories)
                }
            }

        })
    }

    override fun bindGovernmentChildCategory(response: ArrayList<Category>) {

        // pager
        mPagerAdapter.category.addAll(response)
        mPagerAdapter.notifyDataSetChanged()
        content_vp.offscreenPageLimit = response.size
        content_vp.adapter = mPagerAdapter
        tabs_type.setViewPager(content_vp)

    }

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        if (baseEvent.code == EventCode.EVENT_SET_LIST_TOTAL_COUNT) {
            val count = baseEvent.message
            total_count_tv.text = getString(R.string.total_list_count_format, count)
        }
    }

    // 地图相关


    private lateinit var map: GoogleMap

    private lateinit var myLocation: CameraPosition


    private fun initMap() {

        // 获取 SupportMapFragment 并在地图准备好使用时请求通知
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(Constant.LAT, Constant.LNG)))
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()
        // 获得展示在地图上的数据
        // 当前位置


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

                // 移动到当前位置
                map.animateCamera(CameraUpdateFactory.newCameraPosition(myLocation), object : GoogleMap.CancelableCallback {

                    override fun onCancel() = Unit

                    override fun onFinish() = Unit

                })


            }
        }
    }

    /**
     * 移动到我的位置
     */
    override fun onMyLocationButtonClick(): Boolean {
        if (!::myLocation.isInitialized) return false
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(myLocation))
        return false
    }

    /**
     * 点击我的位置 maker
     */
    override fun onMyLocationClick(location: Location) {
        ToastUtils.showShort("Current location:\n$location")
    }


}