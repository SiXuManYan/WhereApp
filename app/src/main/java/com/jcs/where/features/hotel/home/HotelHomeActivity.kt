package com.jcs.where.features.hotel.home

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.gson.Gson
import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelHomeRecommend
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.city.CityPickerActivity
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.hotel.map.HotelMapActivity
import com.jcs.where.home.dialog.HotelStarDialog
import com.jcs.where.utils.Constant
import com.jcs.where.utils.SPKey
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.NumberView2
import com.jcs.where.widget.calendar.JcsCalendarAdapter
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_hotel_home.*

/**
 * Created by Wangsw  2021/9/13 14:47.
 * 酒店首页
 */
class HotelHomeActivity : BaseMvpActivity<HotelDetailPresenter>(), HotelHomeView, NumberView2.OnValueChangeListener,
    HotelStarDialog.HotelStarCallback, JcsCalendarDialog.OnDateSelectedListener {

    /** 酒店分类 id ,用户获取酒店下的子分类 */
    private var hotelCategoryId = 0

    private var isToolbarDark = false

    /** 房间数量 */
    private var roomNumber = 1

    /** 价格 */
    private var priceRange: String? = null

    /** 星级 */
    private var starLevel: String? = null

    /** 酒店分数 */
    private var grade: String? = null

    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: HotelHomeRecommendAdapter
    private lateinit var mJcsCalendarDialog: JcsCalendarDialog
    private lateinit var mHotelStarDialog: HotelStarDialog

    override fun isStatusDark() = isToolbarDark

    override fun getLayoutId() = R.layout.activity_hotel_home

    /** 处理城市信息 */
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bundle = it.data?.extras
            if (bundle != null) {
                val city = bundle.getString(Constant.PARAM_SELECT_AREA_NAME)
                name_tv.text = city
            }
        }
    }

    override fun initView() {
        initExtra()
        initList()
        initScroll()
        number_view.apply {
            alwaysEnableCut = true
            MIN_GOOD_NUM = 1
            MAX_GOOD_NUM = 20
            cut_iv.setImageResource(R.mipmap.ic_cut_black_transparent)
            add_iv.setImageResource(R.mipmap.ic_add_black)
            cutResIdCommon = R.mipmap.ic_cut_black
            cutResIdMin = R.mipmap.ic_cut_black_transparent
            addResIdCommon = R.mipmap.ic_add_black
            addResIdMax = R.mipmap.ic_add_black_transparent
            updateNumberJudgeMin(roomNumber)
            cut_iv.visibility = View.VISIBLE
            valueChangeListener = this@HotelHomeActivity
        }
        val city = SPUtils.getInstance().getString(SPKey.SELECT_AREA_NAME, "")
        if (city.isNotBlank()) {
            name_tv.text = city
        } else {
            name_tv.text = getString(R.string.please_choose_the_city)
        }

    }

    private fun initExtra() {
        val bundle = intent.extras
        bundle?.let {
            hotelCategoryId = bundle.getInt(Constant.PARAM_CATEGORY_ID, 0)
        }


    }


    private fun initList() {
        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            addEmptyList(this)
        }

        mAdapter = HotelHomeRecommendAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener { _, _, position ->
                val hotel = mAdapter.data[position]
                HotelDetailActivity2.navigation(
                    this@HotelHomeActivity,
                    hotel.id,
                    mJcsCalendarDialog.startBean,
                    mJcsCalendarDialog.endBean,
                    starLevel,
                    priceRange,
                    grade,
                    roomNumber
                )
            }
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
        mJcsCalendarDialog = JcsCalendarDialog().apply {
            initCalendar()
            setOnDateSelectedListener(this@HotelHomeActivity)
        }


        mHotelStarDialog = HotelStarDialog().apply {
            setCallback(this@HotelHomeActivity)
        }


        setDate()


    }


    override fun bindListener() {


        score_tv.setOnClickListener(object : ClickUtils.OnDebouncingClickListener(500) {
            override fun onDebouncingClick(v: View?) {
                mHotelStarDialog.show(supportFragmentManager)
            }
        })

        date_tv.setOnClickListener(object : ClickUtils.OnDebouncingClickListener(500) {
            override fun onDebouncingClick(v: View?) {
                mJcsCalendarDialog.show(supportFragmentManager)
            }
        })

        inquire_tv.setOnClickListener {
            HotelMapActivity.navigation(
                this,
                hotelCategoryId,
                starLevel,
                priceRange,
                grade,
                roomNumber,
                mJcsCalendarDialog.startBean,
                mJcsCalendarDialog.endBean
            )
        }
        back_iv.setOnClickListener {
            finish()
        }

        city_rl.setOnClickListener {
            launcher.launch(Intent(this, CityPickerActivity::class.java))
        }

    }

    override fun bindData(response: ArrayList<HotelHomeRecommend>) {
        if (response.isEmpty()) {
            emptyView.showEmptyContainer()
        }
        mAdapter.setNewInstance(response)
    }

    override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
        roomNumber = goodNum
    }

    override fun selectPriceOrStar(show: String?) = Unit


    override fun selectResult(
        priceBeans: HotelStarDialog.PriceIntervalBean,
        selectStarBean: HotelStarDialog.StarBean, scoreBean: HotelStarDialog.ScoreBean,
    ) {

        val priceShow = priceBeans.priceShow

        val starShow = if (selectStarBean.starShow.isEmpty()) {
            getString(R.string.star_default)
        } else {
            selectStarBean.starShow
        }
        val scoreString = if (scoreBean.scoreString.isEmpty()) {
            getString(R.string.score_default)
        } else {
            scoreBean.scoreString
        }


        ("$priceShow / $starShow / $scoreString").also { score_tv.text = it }


        // 价格
        val startPrice = priceBeans.startPrice
        val endPrice = priceBeans.endPrice
        val priceList = ArrayList<Int>().apply {
            clear()
            add(startPrice)
            add(endPrice)
        }
        priceRange = Gson().toJson(priceList)

        // 星级
        val star = selectStarBean.starValue
        starLevel = if (star != 0) {
            val scoreList = ArrayList<Int>().apply {
                clear()
                if (star == 2) {
                    add(1)
                }
                add(star)
            }
            Gson().toJson(scoreList)
        } else {
            null
        }


        // 评分
        val score = scoreBean.score
        grade = if (score == 0.0f) {
            null
        } else {
            score.toString()
        }

    }

    override fun onDateSelected(startDate: JcsCalendarAdapter.CalendarBean, endDate: JcsCalendarAdapter.CalendarBean) {

        mJcsCalendarDialog.startBean = startDate
        mJcsCalendarDialog.endBean = endDate
        setDate()
    }

    private fun setDate() {
        val startBean = mJcsCalendarDialog.startBean
        val endBean = mJcsCalendarDialog.endBean
        (startBean.showMonthDayDate + " - " + endBean.showMonthDayDate).also {
            date_tv.text = it
        }
        val span = (endBean.time - startBean.time) / (1000 * 60 * 60 * 24)
        total_time_tv.text = getString(R.string.total_date_format, span.toString())
    }


}