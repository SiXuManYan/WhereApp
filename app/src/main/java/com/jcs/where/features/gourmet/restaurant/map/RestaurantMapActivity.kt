package com.jcs.where.features.gourmet.restaurant.map

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse
import com.jcs.where.api.response.search.SearchResultResponse
import com.jcs.where.api.response.search.SearchResultResponse.TYPE_4_RESTAURANT
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.hotel.map.HotelMapViewPagerTransformer
import com.jcs.where.features.search.result.SearchAllAdapter
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocationUtil
import com.jcs.where.utils.LocationUtil.AddressCallback
import kotlinx.android.synthetic.main.activity_restaurant_map.*

/**
 * Created by Wangsw  2021/5/14 15:18.
 *
 */
class RestaurantMapActivity : BaseMvpActivity<RestaurantMapPresenter>(), RestaurantMapView,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, OnItemClickListener {

    private var mCategoryId: String = ""
    private var mMap: GoogleMap? = null
    lateinit var mPagerAdapter: RestaurantMapAdapter
    private val mMarkerRainbow: ArrayList<Marker> = ArrayList()
    private val views: ArrayList<View> = ArrayList()
    private var lastPostition = 0
    private var lastScrollPosition = 0
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mMyPosition: LatLng? = null

    private lateinit var mAdapter: SearchAllAdapter


    override fun getLayoutId() = R.layout.activity_restaurant_map

    override fun isStatusDark() = true

    override fun initView() {
        intent.getStringExtra(Constant.PARAM_ID)?.let {
            mCategoryId = it
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initPager()

        myLocationIcon.visibility = View.VISIBLE

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient?.connect()

        mAdapter = SearchAllAdapter()
        mAdapter.setOnItemClickListener(this)
        recycler.adapter = mAdapter

    }

    private fun initPager() {
        viewPager.offscreenPageLimit = 2
        viewPager.clipToPadding = false
        viewPager.setPadding(SizeUtils.dp2px(35f), 0, SizeUtils.dp2px(35f), 0)
        viewPager.pageMargin = SizeUtils.dp2px(15f)

        val transformer = HotelMapViewPagerTransformer()
        viewPager.setPageTransformer(false, transformer)

        mPagerAdapter = RestaurantMapAdapter(this)
        viewPager.adapter = mPagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {
                mMarkerRainbow[position].showInfoWindow()


                // 选中
                val view = views[position]
                val makerIv = view.findViewById<ImageView>(R.id.maker_iv)
                makerIv.setImageResource(R.mipmap.ic_food_maker_selected)
                (view.parent as ViewGroup).removeView(view)
                mMarkerRainbow[position].setIcon(fromView(this@RestaurantMapActivity, view))

                // 未选中
                val lastView = views[lastScrollPosition]
                val lastMakerIv = lastView.findViewById<ImageView>(R.id.maker_iv)
                lastMakerIv.setImageResource(R.mipmap.ic_food_maker)
                (lastView.parent as ViewGroup).removeView(lastView)

                mMarkerRainbow[lastScrollPosition].setIcon(fromView(this@RestaurantMapActivity, lastView))
                lastScrollPosition = position

                val position1 = mMarkerRainbow[position].position
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position1, 15f))
            }


        })

    }


    /**
     * GoogleMap：根据传入的 view，创建 BitmapDescriptor 对象
     */
    fun fromView(context: Context?, view: View?): BitmapDescriptor? {
        val frameLayout = FrameLayout(context!!)
        frameLayout.addView(view)
        frameLayout.isDrawingCacheEnabled = true
        val bitmap: Bitmap = getBitmapFromView(frameLayout)!!
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
        bitmap.recycle()
        return bitmapDescriptor
    }


    /**
     * Convert a view to bitmap
     */
    fun getBitmapFromView(view: View): Bitmap? {
        return try {
            banTextViewHorizontallyScrolling(view)
            view.destroyDrawingCache()
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            val bitmap = view.drawingCache
            bitmap?.copy(Bitmap.Config.ARGB_8888, false)
        } catch (ex: Throwable) {
            null
        }
    }

    /**
     * 禁止 TextView 水平滚动
     */
    private fun banTextViewHorizontallyScrolling(view: View) {
        if (view is ViewGroup) {
            for (index in 0 until view.childCount) {
                banTextViewHorizontallyScrolling(view.getChildAt(index))
            }
        } else if (view is TextView) {
            view.setHorizontallyScrolling(false)
        }
    }


    override fun initData() {
        presenter = RestaurantMapPresenter(this)
        presenter.getList(mCategoryId, null)
        mMyPosition = CacheUtil.getMyCacheLocation()
    }

    override fun bindListener() {

        myLocationIcon.setOnClickListener {
            backMyPosition()
        }

        back_iv.setOnClickListener {
            finish()
        }

        listIv.setOnClickListener {
            finish()
        }

        close_search_iv.setOnClickListener {
            recycler.visibility = View.GONE
        }

        clearIv.setOnClickListener {
            search_aet.setText("")
            clearIv.visibility = View.GONE
            presenter.getList(mCategoryId, null)
        }

        search_aet.setOnClickListener {
            recycler.visibility = View.VISIBLE
        }

        search_aet.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                recycler.visibility = View.VISIBLE
            }else{
                recycler.visibility = View.GONE
            }

        }

        search_aet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable) {
                val finalInput = s.toString().trim()
                handleSearch(finalInput)
            }

        })

    }

    private fun handleSearch(finalInput: String) {
        if (TextUtils.isEmpty(finalInput)) {
            mAdapter.setNewInstance(null)
            clearIv.visibility = View.GONE
            return
        }
        clearIv.visibility = View.VISIBLE
        mAdapter.keyWord = finalInput
        presenter.search(finalInput)
    }

    override fun bindList(response: ArrayList<RestaurantResponse>) {

        mMap?.clear()
        mMarkerRainbow.clear()

        viewPager?.removeAllViews()
        views.clear()

        if (response.isEmpty()) {
            return
        }

        lastPostition = 0
        lastScrollPosition = 0

        // Center camera on Adelaide marker
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mMyPosition, 15f))

        for (i in response.indices) {
            val view = LayoutInflater.from(this@RestaurantMapActivity).inflate(R.layout.custom_marker_layout_2, null)
            views.add(view)
            val bitmapDescriptor = fromView(this@RestaurantMapActivity, view)

            mMap?.let {
                val marker = it.addMarker(MarkerOptions()
                        .position(LatLng(
                                response[i].lat,
                                response[i].lng)) // .title("₱" + list.get(i).getPrice() + "起")
                        .icon(bitmapDescriptor))
                mMarkerRainbow.add(marker)
            }


        }


        val view = views[0]
        val makerIv = view.findViewById<ImageView>(R.id.maker_iv)
        makerIv.setImageResource(R.mipmap.ic_food_maker_selected)

        (view.parent as ViewGroup).removeView(view)
        mMarkerRainbow[0].setIcon(fromView(this@RestaurantMapActivity, view))

        mPagerAdapter.setData(response)
        viewPager.adapter = mPagerAdapter


        mMap?.addMarker(MarkerOptions()
                .position(mMyPosition!!)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marklocation)))


    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mMyPosition, 15f))


        mMap?.let {
            it.uiSettings.isMapToolbarEnabled = false
            it.setOnMarkerClickListener(this)
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {

        if (marker == null) {
            return false
        }

        val handler = Handler(Looper.getMainLooper())
        val start = SystemClock.uptimeMillis()
        val duration: Long = 1500
        val interpolator: Interpolator = BounceInterpolator()

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = Math.max(
                        1 - interpolator.getInterpolation(elapsed.toFloat() / duration), 0f)
                marker!!.setAnchor(0.5f, 1.0f + 2 * t)
                if (t > 0.0) {
                    handler.postDelayed(this, 16)
                }
            }
        })


        if (marker!!.id == mMarkerRainbow[lastPostition].id) {

        } else {
            for (i in mMarkerRainbow.indices) {
                if (marker.id == mMarkerRainbow.get(i).getId()) {
                    viewPager.currentItem = i


                    // 选中
                    val view = views[i]
                    val makerIv = view.findViewById<ImageView>(R.id.maker_iv)

                    makerIv.setImageResource(R.mipmap.ic_food_maker_selected)
                    (view.parent as ViewGroup).removeView(view)
                    marker!!.setIcon(fromView(this@RestaurantMapActivity, view))

                    // 未选中
                    val lastView = views[lastPostition]
                    val lastMaker = lastView.findViewById<ImageView>(R.id.maker_iv)
                    lastMaker.setImageResource(R.mipmap.ic_food_maker)
                    (lastView.parent as ViewGroup).removeView(lastView)
                    mMarkerRainbow[lastPostition].setIcon(fromView(this@RestaurantMapActivity, lastView))
                    lastPostition = i
                }
            }
        }

        val zIndex = marker.zIndex + 1.0f
        marker.zIndex = zIndex
        return false


    }

    override fun onConnectionFailed(p0: ConnectionResult) = Unit

    override fun onConnected(p0: Bundle?) {

        com.jcs.where.utils.PermissionUtils.permissionAny(this, { granted: Boolean ->
            if (granted) {
                LocationUtil.getInstance().addressCallback = object : AddressCallback {
                    override fun onGetAddress(address: Address) {}
                    override fun onGetLocation(lat: Double, lng: Double) {}
                }
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)


    }

    override fun onConnectionSuspended(p0: Int) = Unit

    fun backMyPosition() {
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mMyPosition, 15f))
    }

    override fun onDestroy() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.disconnect()
        }
        super.onDestroy()
    }

    override fun bindSearchResult(response: ArrayList<SearchResultResponse>) {
        if (response.isEmpty()) {
            mAdapter.setNewInstance(null)
            return
        }
        mAdapter.setNewInstance(response)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]

        if (data.type != TYPE_4_RESTAURANT) {
            return
        }
        recycler.visibility = View.GONE
        presenter.getList(mCategoryId, data.name)
    }


}