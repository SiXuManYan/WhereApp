package com.jiechengsheng.city.features.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.response.BannerResponse
import com.jiechengsheng.city.api.response.ModulesResponse
import com.jiechengsheng.city.api.response.home.HomeChild
import com.jiechengsheng.city.api.response.home.HomeNewsResponse
import com.jiechengsheng.city.api.response.home.TabEntity
import com.jiechengsheng.city.api.response.version.VersionResponse
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.features.bills.PayBillsActivity
import com.jiechengsheng.city.features.city.CityPickerActivity
import com.jiechengsheng.city.features.complex.ConvenienceServiceActivity
import com.jiechengsheng.city.features.enterprise.EnterprisePageActivity
import com.jiechengsheng.city.features.gourmet.restaurant.list.RestaurantHomeActivity
import com.jiechengsheng.city.features.home.child.ComplexChildFragment
import com.jiechengsheng.city.features.home.child.HomeMallFragment
import com.jiechengsheng.city.features.job.main.JobMainActivity
import com.jiechengsheng.city.features.mall.home.MallHomeActivity
import com.jiechengsheng.city.features.map.government.GovernmentActivity
import com.jiechengsheng.city.features.message.MessageCenterActivity
import com.jiechengsheng.city.features.search.SearchAllActivity
import com.jiechengsheng.city.features.travel.home.TravelHomeActivity
import com.jiechengsheng.city.home.decoration.HomeModulesItemDecoration
import com.jiechengsheng.city.news.NewsActivity
import com.jiechengsheng.city.news.NewsDetailActivity
import com.jiechengsheng.city.news.NewsVideoActivity
import com.jiechengsheng.city.utils.*
import com.jiechengsheng.city.view.XBanner.AbstractUrlLoader
import com.jiechengsheng.city.view.XBanner.XBanner
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

    var appBarStateChangeListener: AppBarStateChanged? = null

    var systemMessageCount = 0

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
        initEmpty()
    }

    private fun initEmpty() {
        home_empty.apply {
            hideEmptyContainer()
        }
    }


    /** 轮播图 */
    private fun initBanner() {
        message_view.setMessageImageResource(R.mipmap.ic_home_message)

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
                    2 -> EnterprisePageActivity.navigation(requireContext(), data.categories)
                    3 -> {
                        startActivity(TravelHomeActivity::class.java, Bundle().apply {
                            putIntegerArrayList(Constant.PARAM_CATEGORY_ID, data.categories)
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
                        startActivity(MallHomeActivity::class.java)
                    }
                    11 -> {
                        startActivity(JobMainActivity::class.java)
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

        swipeLayout?.setOnRefreshListener(this)
        swipeLayout?.setColorSchemeColors(ColorUtils.getColor(R.color.blue_377BFF))

        moduleRecycler.isNestedScrollingEnabled = false

        child_abl.addOnOffsetChangedListener(object : AppBarStateChangeListener() {

            override fun onStateChanged(appBarLayout: AppBarLayout, expanded: State, verticalOffset: Int) {
                Log.e("onStateChanged", "verticalOffset == $verticalOffset")


                when (expanded) {
                    State.EXPANDED -> {
                        swipeLayout?.isEnabled = true
                        appBarStateChangeListener?.expanded()
                    }

                    State.COLLAPSED -> {
                        swipeLayout?.isEnabled = false
                        appBarStateChangeListener?.scrolling()
                    }

                    State.IDLE -> {
                        swipeLayout?.isEnabled = false
                        appBarStateChangeListener?.scrolling()
                    }
                }


            }
        })

        swipeLayout?.postDelayed({ swipeLayout?.isEnabled = false }, 200)

    }


    override fun initData() {
        presenter = HomePresenter(this)
        initCity()
        rxTimer = RxTimer()

    }


    override fun loadOnVisible() {
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

        city_ll.setOnClickListener(object : ClickUtils.OnDebouncingClickListener(500) {
            override fun onDebouncingClick(v: View?) {
                startActivityForResult(CityPickerActivity::class.java, REQ_SELECT_CITY, null)
            }
        })
        search_ll.setOnClickListener {
            startActivity(SearchAllActivity::class.java)
        }
        message_view.setOnClickListener {
            MessageCenterActivity.navigation(requireContext(), systemMessageCount)
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
            top_banner?.pause()
        } else {
            startScroll()
            top_banner?.start()
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
        swipeLayout?.isRefreshing = true
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

    override fun setMessageCount(i: Int, systemUnreadMessageCount: Int) {
        message_view.setMessageCount(i)
        systemMessageCount = systemUnreadMessageCount
    }

    override fun bindTopBannerData(bannerUrls: ArrayList<String>, response: ArrayList<BannerResponse>) {
        home_empty.visibility = View.GONE
        top_banner.setImageUrls(bannerUrls)
        top_banner.setBannerPageListener(object : XBanner.BannerPageListener {

            override fun onBannerDragging(item: Int) = Unit

            override fun onBannerIdle(item: Int) = Unit

            override fun onBannerClick(item: Int) {
                val data = response[item]
                BusinessUtils.handleBannerClick(context, data)
            }

        }).start()

    }

    var needRequest4Tablet = true

    override fun bindPlateData(toMutableList: MutableList<ModulesResponse>) {
        home_empty.visibility = View.GONE
        mModulesAdapter.setNewInstance(toMutableList)
    }

    var scrollPosition = 0

    override fun bindNewsData(newsData: List<HomeNewsResponse>?) {
        home_empty.visibility = View.GONE
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


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_SIGN_OUT -> {
                message_view.setMessageCount(0)
            }
            EventCode.EVENT_SCROLL_TO_TOP -> {
                child_abl.setExpanded(true, true)

            }
            else -> {
            }
        }

    }

    var isInit = true

    override fun bindHomeChild(response: ArrayList<HomeChild>, titles: ArrayList<String>) {
        swipeLayout?.isRefreshing = false
        home_empty.visibility = View.GONE
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


    private fun initCity() {
        val cityName = SPUtils.getInstance().getString(SPKey.SELECT_AREA_NAME, StringUtils.getString(R.string.default_city_name))
        city_tv.text = cityName
    }


    override fun bindDefaultCity(cityName: String) {
        city_tv.text = cityName
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_HOME_NEARBY))
    }


    override fun onError(errorResponse: ErrorResponse?) {
        super.onError(errorResponse)
        swipeLayout?.isRefreshing = false
        if (presenter.isChildError && presenter.isPlateDataError && presenter.isTopBannerError) {
            if (home_empty.visibility != View.VISIBLE) {
                home_empty.visibility = View.VISIBLE
                home_empty.showNetworkError {
                    requestData()
                    home_empty.visibility = View.GONE
                    swipeLayout?.isRefreshing = true
                }
            }

        }

    }

    override fun showJobNotice() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val view: View = inflater.inflate(R.layout.dialog_job_notice, null)

        val cancelTv = view.findViewById<ImageView>(R.id.close_iv)
        val confirmTv = view.findViewById<Button>(R.id.view_now_bt)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        val window: Window? = alertDialog.window
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setContentView(view)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // 更改默认宽度
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(window.attributes)
            lp.width = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(80f)
            window.attributes = lp
        }
        cancelTv.setOnClickListener {
            alertDialog.dismiss()
        }

        confirmTv.setOnClickListener {
            startActivityAfterLogin(JobMainActivity::class.java, Bundle().apply {
                putBoolean(Constant.PARAM_FROM_NOTICE, true)
            })
            alertDialog.dismiss()
        }
    }


    override fun checkAppVersion(response: VersionResponse) {
        val newVersion = response.new_version
        val updateDesc = response.update_desc
        val isForceInstall = response.is_force_install

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val view: View = inflater.inflate(R.layout.activity_upgrade, null)

        val titleTv = view.findViewById<TextView>(R.id.title_tv)
        val messageTv = view.findViewById<TextView>(R.id.message_tv)

        val cancelIv = view.findViewById<ImageView>(R.id.ic_cancel)
        val upgradeTv = view.findViewById<TextView>(R.id.upgrade_tv)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        val window: Window? = alertDialog.window
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setContentView(view)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // 更改默认宽度
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(window.attributes)
            lp.width = ScreenUtils.getScreenWidth()
            window.attributes = lp
            window.attributes.gravity = Gravity.CENTER
        }

        titleTv.text = newVersion
        messageTv.text = updateDesc
        messageTv.text = updateDesc

        cancelIv.visibility = if (isForceInstall) {
            View.GONE
        } else {
            View.VISIBLE
        }
        cancelIv.setOnClickListener {
            alertDialog.dismiss()
        }
        upgradeTv.setOnClickListener {
            FeaturesUtil.gotoGooglePlay(requireContext())
        }


    }


}