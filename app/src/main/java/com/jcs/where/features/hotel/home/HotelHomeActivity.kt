package com.jcs.where.features.hotel.home

import android.Manifest
import android.graphics.Color
import android.location.Address
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelHomeRecommend
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.hotel.map.HotelMapActivity
import com.jcs.where.home.dialog.HotelStarDialog
import com.jcs.where.utils.*
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.NumberView2
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_hotel_home.*
import java.util.*

/**
 * Created by Wangsw  2021/9/13 14:47.
 * 酒店详情
 */
class HotelHomeActivity : BaseMvpActivity<HotelDetailPresenter>(), HotelHomeView, NumberView2.OnValueChangeListener,
    HotelStarDialog.HotelStarCallback {

    /** 酒店分类 id ,用户获取酒店下的子分类 */
    private var hotelCategoryId = 0

    private var isToolbarDark = false
    private var totalRoom = 0

    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: HotelHomeRecommendAdapter
    private lateinit var mJcsCalendarDialog: JcsCalendarDialog
    private lateinit var mHotelStarDialog: HotelStarDialog

    override fun isStatusDark() = isToolbarDark

    override fun getLayoutId() = R.layout.activity_hotel_home

    override fun initView() {
        initExtra()
        initList()
        initScroll()
        number_view.apply {
            alwaysEnableCut = true
            cut_iv.setImageResource(R.mipmap.ic_cut_black)
            add_iv.setImageResource(R.mipmap.ic_add_black)
            updateNumber(1)
            valueChangeListener = this@HotelHomeActivity
        }

    }

    private fun initExtra() {
        val bundle = intent.extras
//        hotelCategoryId = bundle?.getInt(Constant.PARAM_CATEGORY_ID, 0)

    }


    private fun initList() {
        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            hideEmptyContainer()
        }

        mAdapter = HotelHomeRecommendAdapter().apply {
            setEmptyView(emptyView)
        }


        rv_home.apply {
            adapter = mAdapter
            addItemDecoration(
                DividerDecoration(
                    Color.TRANSPARENT,
                    SizeUtils.dp2px(16f),
                    SizeUtils.dp2px(15f),
                    SizeUtils.dp2px(15f)
                )
            )
            layoutManager = object : LinearLayoutManager(this@HotelHomeActivity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            isNestedScrollingEnabled = true
        }
    }


    private fun initScroll() {
        // alpha
        useView.setBackgroundColor(getColor(R.color.white))
        toolbar.setBackgroundColor(getColor(R.color.white))
        useView.background.alpha = 0
        toolbar.background.alpha = 0
        scrollView.setOnScrollChangeListener { _, _, y, _, _ ->
            val headHeight = media_fl.measuredHeight - toolbar.measuredHeight
            var alpha = (y.toFloat() / headHeight * 255).toInt()
            if (alpha >= 255) {
                alpha = 255
                hotel_title_tv.visibility = View.VISIBLE
            }
            if (alpha <= 5) {
                alpha = 0
                hotel_title_tv.visibility = View.GONE
            }
            isToolbarDark = alpha > 130
            back_iv.setImageResource(
                if (isToolbarDark) {
                    R.mipmap.ic_back_black
                } else {
                    R.mipmap.ic_back_light
                }
            )

            useView.background.alpha = alpha
            toolbar.background.alpha = alpha

            changeStatusTextColor()
        }

    }

    override fun initData() {
        presenter = HotelDetailPresenter(this)
        presenter.getData()
        getLocation(false)

        mJcsCalendarDialog = JcsCalendarDialog().apply {
            initCalendar(this@HotelHomeActivity)
        }
        val start = mJcsCalendarDialog.startBean.showMonthDayDate
        val end = mJcsCalendarDialog.startBean.showWeekday
        date_tv.text =    getString(R.string.valid_period_format2 ,start,end)

        mHotelStarDialog = HotelStarDialog().apply {
            setCallback(this@HotelHomeActivity)
        }

    }

    private fun getLocation(handleClick: Boolean) {
        PermissionUtils.permissionAny(
            this, {
                if (it) {
                    LocationUtil.getInstance().addressCallback = object : LocationUtil.AddressCallback {
                        override fun onGetAddress(address: Address) {
                            val locality = address.locality //市
                            name_tv.text = locality
                        }
                        override fun onGetLocation(lat: Double, lng: Double) {
                            CacheUtil.getShareDefault().put(Constant.SP_LATITUDE, lat.toFloat())
                            CacheUtil.getShareDefault().put(Constant.SP_LONGITUDE, lng.toFloat())
                        }
                    }
                } else {
                    if (handleClick) AppUtils.launchAppDetailsSettings()
                    ToastUtils.showShort(R.string.open_permission)
                }
            },
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun bindListener() {
        my_location_tv.setOnClickListener {
            getLocation(true)
        }
        score_tv.setOnClickListener {
            mHotelStarDialog.show(supportFragmentManager)
        }
        date_tv.setOnClickListener {
            mJcsCalendarDialog.show(supportFragmentManager)
        }
        inquire_tv.setOnClickListener {
            HotelMapActivity.navigation(this,107)
        }
    }

    override fun bindData(response: ArrayList<HotelHomeRecommend>) {
        if (response.isEmpty()) {
            emptyView.showEmptyContainer()
        }
        mAdapter.setNewInstance(response)
    }

    override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
        totalRoom = goodNum
    }

    override fun selectPriceOrStar(show: String?) {

    }

    override fun selectResult(
        mPriceBeans: HotelStarDialog.PriceIntervalBean?,
        mSelectStartBean: HotelStarDialog.StarBean?,
        mScoreBean: HotelStarDialog.ScoreBean?
    ) {

    }


}