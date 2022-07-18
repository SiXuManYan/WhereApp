package com.jcs.where.features.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.*
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.google.android.material.appbar.AppBarLayout
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.api.response.ModulesResponse
import com.jcs.where.api.response.home.HomeChild
import com.jcs.where.api.response.home.HomeNewsResponse
import com.jcs.where.api.response.home.TabEntity
import com.jcs.where.api.response.version.VersionResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.bills.PayBillsActivity
import com.jcs.where.features.city.CityPickerActivity
import com.jcs.where.features.complex.ConvenienceServiceActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.gourmet.restaurant.list.RestaurantHomeActivity
import com.jcs.where.features.home.child.ComplexChildFragment
import com.jcs.where.features.home.child.HomeMallFragment
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.mall.detail.MallDetailActivity
import com.jcs.where.features.mall.home.MallHomeActivity
import com.jcs.where.features.map.government.GovernmentActivity
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.message.MessageCenterActivity
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.features.travel.detail.TravelDetailActivity
import com.jcs.where.features.travel.home.TravelHomeActivity
import com.jcs.where.features.upgrade.UpgradeActivity
import com.jcs.where.features.web.WebViewActivity
import com.jcs.where.home.decoration.HomeModulesItemDecoration
import com.jcs.where.news.NewsActivity
import com.jcs.where.news.NewsDetailActivity
import com.jcs.where.news.NewsVideoActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.SPKey
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.yellow_page.activity.YellowPageActivity
import kotlinx.android.synthetic.main.fragment_home4.*
import org.greenrobot.eventbus.EventBus
import pl.droidsonroids.gif.GifImageView


/**
 * Created by Wangsw  2021/4/12 13:53.
 * 首页
 *
 */
class HomeFragment : BaseMvpFragment<HomePresenter>(), HomeView, SwipeRefreshLayout.OnRefreshListener {


    /** 选择城市 */
    private val REQ_SELECT_CITY = 100

    /** 功能区(金刚区) */
    private lateinit var mModulesAdapter: ModulesAdapter


    /** 首页新闻 */
    private lateinit var mNewsAdapter: HomeNewsAdapter

    private lateinit var rxTimer: RxTimer

    /** 新闻 tab 数据 */
    private val mNewsTabDataList: ArrayList<CustomTabEntity> = ArrayList()

    /** 新闻列表数据 */
    private val mNewsAdapterDataList: ArrayList<HomeNewsResponse> = ArrayList()


    private var mType: ArrayList<HomeChild> = ArrayList()

    override fun getLayoutId() = R.layout.fragment_home4

    override fun initView(view: View) {
        view.let {
            BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.parent_abl))
        }
        initBanner()
        initPlate()
        initNews()
        initScroll()
    }



    /** 轮播图 */
    private fun initBanner() {
        message_view.setMessageImageResource(R.mipmap.ic_home_message)
        val bannerParams = ll_banner.layoutParams.apply {
            height = ScreenUtils.getScreenWidth() * 194 / 345
        }
        ll_banner.layoutParams = bannerParams

        top_banner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
            .setTitleHeight(50)
            .isAutoPlay(true)
            .setDelay(5000)
            .setUpIndicators(R.drawable.ic_selected, R.drawable.ic_unselected)
            .setUpIndicatorSize(6, 6)
            .setIndicatorGravity(XBanner.INDICATOR_CENTER)
            .setImageLoader(object : AbstractUrlLoader() {
                override fun loadImages(context: Context, url: String, image: ImageView) {
                    GlideUtil.load(context, url, image, 4)
                }

                override fun loadGifs(context: Context, url: String, gifImageView: GifImageView, scaleType: ImageView.ScaleType) {
                    GlideUtil.load(context, url, gifImageView, 4)
                }
            })
    }

    /** 金刚区相关 */
    private fun initPlate() {
        mModulesAdapter = ModulesAdapter()
        moduleRecycler.apply {
            addItemDecoration(HomeModulesItemDecoration())
            layoutManager = object : GridLayoutManager(context, 5, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }

            }
            adapter = mModulesAdapter
        }

        // 点击
        mModulesAdapter.setOnItemClickListener { _, _, position ->
            val data = mModulesAdapter.data[position]

            if (data.dev_status == 2) {
                showComing()
                return@setOnItemClickListener
            }
            if (data.dev_status == 1) {
                when (data.id) {
                    1 -> startActivity(GovernmentActivity::class.java)
//                    1 -> startActivity(GovernmentMapActivity::class.java)
                    2 -> {
                        startActivity(YellowPageActivity::class.java, Bundle().apply {
                            putIntegerArrayList(YellowPageActivity.K_CATEGORIES, data.categories as (ArrayList<Int>))
                        })
                    }
                    3 -> {
//                        startActivity(TravelStayActivity::class.java, Bundle().apply {
//                            putIntegerArrayList(TravelStayActivity.K_CATEGORY_IDS, data.categories as (ArrayList<Int>))
//                        })
                        startActivity(TravelHomeActivity::class.java, Bundle().apply {
                            putIntegerArrayList(Constant.PARAM_CATEGORY_ID, data.categories as (ArrayList<Int>))
                        })
                    }
                    4, 5, 6, 7 -> {
                        val convenienceCategoryId = data.categories.toString()
                        val name = data.name
                        startActivity(ConvenienceServiceActivity::class.java, Bundle().apply {
                            putString(ConvenienceServiceActivity.K_CATEGORIES, convenienceCategoryId)
                            putString(ConvenienceServiceActivity.K_SERVICE_NAME, name)
                        })
                    }
                    8 -> startActivity(PayBillsActivity::class.java)
                    9 -> {
                        startActivity(RestaurantHomeActivity::class.java, Bundle().apply {
                            putInt(Constant.PARAM_PID, 89)
                            putString(Constant.PARAM_PID_NAME, getString(R.string.filter_food))
                        })
                    }
                    10 -> {
//                        startActivity(StoreRecommendActivity::class.java)
                        startActivity(MallHomeActivity::class.java)
                    }

                    else -> showComing()
                }
            }


        }
    }


    /**
     * 新闻相关
     */
    private fun initNews() {

        mNewsAdapter = HomeNewsAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val child = data[position]
                when (child.content_type) {
                    1 -> startActivity(NewsDetailActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_NEWS_ID, child.id.toString())
                    })
                    2 -> startActivity(NewsVideoActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_NEWS_ID, child.id.toString())
                    })
                    else -> {
                    }
                }
            }
        }
        news_rv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mNewsAdapter
        }
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(news_rv)

        // tab
        tabs_type.setOnTabSelectListener(object : OnTabSelectListener {

            override fun onTabSelect(position: Int) {
                if (mNewsAdapterDataList.isNotEmpty()) {

                    scrollPosition = 0
                    news_rv.scrollToPosition(0)
                    mNewsAdapter.setNewInstance(mNewsAdapterDataList[position].news_list)
                    startScroll()
                }
            }

            override fun onTabReselect(position: Int) = Unit
        })


        // 更多新闻
        more_news_iv.setOnClickListener {
            startActivity(NewsActivity::class.java)
        }
        more_news_tv.setOnClickListener {
            startActivity(NewsActivity::class.java)
        }

    }


    private fun initScroll() {

        swipeLayout.setOnRefreshListener(this)
        swipeLayout.setColorSchemeColors(ColorUtils.getColor(R.color.blue_377BFF))

        moduleRecycler.isNestedScrollingEnabled = false

        child_abl.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, expanded: State, verticalOffset: Int) {
                when (expanded) {
                    State.EXPANDED -> {

                        swipeLayout.isEnabled = true
                    }

                    State.COLLAPSED -> {

                        swipeLayout.isEnabled = false
                    }

                    State.IDLE -> {
                        swipeLayout.isEnabled = false
                    }
                }
            }
        })

        swipeLayout.postDelayed({ swipeLayout.isEnabled = false }, 200)

    }


    override fun initData() {
        presenter = HomePresenter(this)
        initCity()
        rxTimer = RxTimer()
        requestData()
    }

    private fun requestData() {
        presenter.getCityData()
        presenter.getMessageCount()
        presenter.getTopBanner()
        presenter.getPlateData()
        presenter.getNewsList()
        presenter.getHomeChild()
        presenter.checkAppVersion()
        presenter.connectRongCloud()


    }

    override fun bindListener() {
        city_tv.setOnClickListener {
            startActivityForResult(CityPickerActivity::class.java, REQ_SELECT_CITY, null)
        }
        search_ll.setOnClickListener {
            startActivity(SearchAllActivity::class.java)
        }
        message_view.setOnClickListener {
            startActivityAfterLogin(MessageCenterActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isViewCreated) {
            presenter.getMessageCount()
            startScroll()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            rxTimer.cancel()
            top_banner.pause()
        } else {
            startScroll()
            top_banner.start()
            presenter.getMessageCount()
        }
    }

    override fun onDestroy() {
        rxTimer.cancel()
        top_banner?.releaseBanner()
        super.onDestroy()
    }

    override fun onRefresh() {
        rxTimer.cancel()
        top_banner.pause()
        swipeLayout.isRefreshing = true
        presenter.getMessageCount()
        presenter.getTopBanner()
        presenter.getPlateData()
        presenter.getNewsList()
        presenter.getHomeChild()
    }

    override fun onPause() {
        rxTimer.cancel()
        top_banner.pause()
        super.onPause()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        when (requestCode) {
            REQ_SELECT_CITY -> {
                // 选择城市
                city_tv.text = data.getStringExtra(Constant.PARAM_SELECT_AREA_NAME)
                onRefresh()
            }
            else -> {
            }
        }
    }

    override fun setMessageCount(i: Int) = message_view.setMessageCount(i)

    override fun bindTopBannerData(bannerUrls: ArrayList<String>, response: List<BannerResponse>) {

        top_banner.setImageUrls(bannerUrls)
        top_banner.setBannerPageListener(object : XBanner.BannerPageListener {

            override fun onBannerDragging(item: Int) = Unit

            override fun onBannerIdle(item: Int) = Unit

            override fun onBannerClick(item: Int) {
                val data = response[item]


                if (data.redirect_type == 0) {
                    return
                }
                if (data.redirect_type == 1 && data.h5_link.isNotBlank()) {
                    WebViewActivity.goTo(this@HomeFragment.activity, data.h5_link)
                    return
                }

                if (data.redirect_type == 2) {

                    when (data.target_type) {
                        1 -> {
                            val dialog = JcsCalendarDialog()
                            dialog.initCalendar(this@HomeFragment.activity)
                            HotelDetailActivity2.navigation(requireContext(), data.target_id, dialog.startBean, dialog.endBean)
                        }
                        2 -> TravelDetailActivity.navigation(requireContext(), data.target_id)
                        3 -> startActivity(NewsDetailActivity::class.java, Bundle().apply {
                            putString(Constant.PARAM_NEWS_ID, data.target_id.toString())
                        })
                        4 -> startActivity(MechanismActivity::class.java, Bundle().apply {
                            putInt(Constant.PARAM_ID, data.target_id)
                        })
                        5 -> startActivity(RestaurantDetailActivity::class.java, Bundle().apply {
                            putInt(Constant.PARAM_ID, data.target_id)
                        })
                        6 -> MallDetailActivity.navigation(requireContext(), data.target_id)
                        //  7 -> startActivityAfterLogin(CouponCenterActivity::class.java)
                    }
                    return
                }
            }

        }).start()

    }

    override fun bindPlateData(toMutableList: MutableList<ModulesResponse>) {
        mModulesAdapter.setNewInstance(toMutableList)
    }

    var scrollPosition = 0

    override fun bindNewsData(newsData: List<HomeNewsResponse>?) {
        scrollPosition = 0
        if (newsData.isNullOrEmpty()) {
            news_rl.visibility = View.GONE
            news_rv.visibility = View.GONE
            return
        }
        news_rl.visibility = View.VISIBLE
        news_rv.visibility = View.VISIBLE

        mNewsTabDataList.clear()
        mNewsAdapterDataList.clear()
        mNewsAdapterDataList.addAll(newsData)

        // title
        newsData.forEach {
            mNewsTabDataList.add(TabEntity(it.category_name, 0, 0))
        }
        tabs_type.setTabData(mNewsTabDataList)

        // adapter
        mNewsAdapter.setNewInstance(newsData[0].news_list)


        startScroll()
    }

    private fun startScroll() {
        rxTimer.cancel()
        if (mNewsAdapter.data.size <= 1) {
            return
        }
        rxTimer.interval(5000) {
            scrollPosition++
            if (scrollPosition <= mNewsAdapter.data.size - 1) {
                news_rv?.smoothScrollToPosition(scrollPosition)
            } else {
                scrollPosition = 0
                news_rv?.scrollToPosition(0)
            }
        }
    }

    override fun checkAppVersion(response: VersionResponse) {
        val bundle = Bundle().apply {
            putString(Constant.PARAM_NEW_VERSION_CODE, response.new_version)
            putString(Constant.PARAM_DOWNLOAD_URL, response.download_url)
            putString(Constant.PARAM_UPDATE_DESC, response.update_desc)
            putBoolean(Constant.PARAM_IS_FORCE_INSTALL, response.is_force_install)
        }
        startActivity(UpgradeActivity::class.java, bundle)
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_SIGN_OUT -> {
                message_view.setMessageCount(0)
            }
            else -> {
            }
        }

    }

    var isInit = true

    override fun bindHomeChild(response: ArrayList<HomeChild>, titles: ArrayList<String>) {
        swipeLayout.isRefreshing = false
        isInit = when {
            mType.isEmpty() -> {
                true
            }
            mType == response -> {
                false
            }
            else -> {
                true
            }
        }
        mType.clear()
        mType.addAll(response)


        if (isInit) {
            home_vp.offscreenPageLimit = response.size
            val innerPagerAdapter = InnerPagerAdapter(childFragmentManager, 0)
            innerPagerAdapter.notifyDataSetChanged()
            home_vp.adapter = innerPagerAdapter
            recommend_tabs.setViewPager(home_vp, titles.toTypedArray())
            recommend_tabs.currentTab = 0
        }
    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = mType[position].name

        override fun getItem(position: Int): Fragment {

            val homeChild = mType[position]
            val typeValue = homeChild.type

            return if (typeValue == 1) {
                ComplexChildFragment.newInstance(homeChild.banner, homeChild.category)
            } else {
                HomeMallFragment.newInstance(homeChild.banner, homeChild.category)
            }
        }

        override fun getCount(): Int = mType.size
    }

    override fun onError(errorResponse: ErrorResponse?) {
        super.onError(errorResponse)
        swipeLayout.isRefreshing = false
    }


    private fun initCity() {
        val cityName = SPUtils.getInstance().getString(SPKey.SELECT_AREA_NAME, StringUtils.getString(R.string.default_city_name))
        city_tv.text = cityName
    }


    override fun bindDefaultCity(cityName : String) {
        city_tv.text = cityName
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_HOME_NEARBY))
    }


}