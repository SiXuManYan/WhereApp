package com.jcs.where.features.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.jcs.where.R
import com.jcs.where.adapter.ModulesAdapter
import com.jcs.where.api.response.ModulesResponse
import com.jcs.where.api.response.home.HomeNewsResponse
import com.jcs.where.api.response.home.TabEntity
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import com.jcs.where.api.response.version.VersionResponse
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.convenience.activity.ConvenienceServiceActivity
import com.jcs.where.features.bills.PayBillsActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.gourmet.restaurant.list.RestaurantListActivity
import com.jcs.where.features.message.MessageCenterActivity
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.features.upgrade.UpgradeActivity
import com.jcs.where.government.activity.GovernmentMapActivity
import com.jcs.where.government.activity.MechanismDetailActivity
import com.jcs.where.home.activity.TravelStayActivity
import com.jcs.where.home.decoration.HomeModulesItemDecoration
import com.jcs.where.hotel.activity.CityPickerActivity
import com.jcs.where.hotel.activity.HotelDetailActivity
import com.jcs.where.integral.child.task.HomeRecommendAdapter
import com.jcs.where.news.NewsActivity
import com.jcs.where.news.NewsDetailActivity
import com.jcs.where.news.NewsVideoActivity
import com.jcs.where.travel.TouristAttractionDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideRoundTransform
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.widget.list.DividerDecoration
import com.jcs.where.yellow_page.activity.YellowPageActivity
import kotlinx.android.synthetic.main.fragment_home2.*
import pl.droidsonroids.gif.GifImageView
import java.util.*


/**
 * Created by Wangsw  2021/4/12 13:53.
 * 首页
 *
 */
class HomeFragment2 : BaseMvpFragment<HomePresenter2>(), HomeView2, SwipeRefreshLayout.OnRefreshListener {

    /** 推荐列表请求 */
    private var recommedRequestPage = Constant.DEFAULT_FIRST_PAGE

    /** 选择城市 */
    private val REQ_SELECT_CITY = 100

    /** 功能区(金刚区) */
    private lateinit var mModulesAdapter: ModulesAdapter

    /** 首页推荐 */
    private lateinit var mHomeRecommendAdapter: HomeRecommendAdapter

    /** 首页新闻 */
    private lateinit var mNewsAdapter: HomeNewsAdapter

    private lateinit var rxTimer: RxTimer

    /** 新闻 tab 数据 */
    private val mNewsTabDataList: ArrayList<CustomTabEntity> = ArrayList()

    /** 新闻列表数据 */
    private val mNewsAdapterDataList: ArrayList<HomeNewsResponse> = ArrayList()


    override fun getLayoutId() = R.layout.fragment_home2

    override fun initView(view: View) {
        BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.rl_title))
        initBanner()
        initPlate()
        initNews()
        initRecommend()
        initScroll()
    }

    private fun initCity() {
        val currentAreaId = presenter.getCurrentAreaId()
        if (currentAreaId == "3") {
            // 默认巴郎牙
            city_tv.text = getString(R.string.default_city_name)
            return
        }
        val currentCity = presenter.getCurrentCity(currentAreaId)
        if (currentCity == null) {
            city_tv.text = getString(R.string.default_city_name)
        } else {
            city_tv.text = currentCity.name
        }
    }


    /** 轮播图 */
    private fun initBanner() {
        val bannerParams = ll_banner.layoutParams.apply {
            height = ScreenUtils.getScreenWidth() * 194 / 345
        }
        ll_banner.layoutParams = bannerParams

        val options = RequestOptions()
                .centerCrop()
                .error(R.mipmap.ic_glide_default)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(GlideRoundTransform(4))

        top_banner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
                .setTitleHeight(50)
                .isAutoPlay(true)
                .setDelay(5000)
                .setUpIndicators(R.drawable.ic_selected, R.drawable.ic_unselected)
                .setUpIndicatorSize(20, 6)
                .setIndicatorGravity(XBanner.INDICATOR_CENTER)
                .setImageLoader(object : AbstractUrlLoader() {
                    override fun loadImages(context: Context, url: String, image: ImageView) {
                        Glide.with(context).load(url).apply(options).into(image)
                    }

                    override fun loadGifs(context: Context, url: String, gifImageView: GifImageView, scaleType: ImageView.ScaleType) {
                        Glide.with(context).load(url).apply(options).into(gifImageView)
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
                    1 -> startActivity(GovernmentMapActivity::class.java)
                    2 -> {
                        startActivity(YellowPageActivity::class.java, Bundle().apply {
                            putIntegerArrayList(YellowPageActivity.K_CATEGORIES, data.categories as (ArrayList<Int>))
                        })
                    }
                    3 -> {
                        startActivity(TravelStayActivity::class.java, Bundle().apply {
                            putIntegerArrayList(TravelStayActivity.K_CATEGORY_IDS, data.categories as (ArrayList<Int>))
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
                    9 -> startActivity(RestaurantListActivity::class.java)
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
                    rxTimer.cancel()
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

    }


    /** 推荐列表 */
    private fun initRecommend() {
        swipeLayout.setOnRefreshListener(this)
        mHomeRecommendAdapter = HomeRecommendAdapter(true)
        rv_home.apply {
            adapter = mHomeRecommendAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(1f), SizeUtils.dp2px(128f), SizeUtils.dp2px(15f)).apply {
                setDrawHeaderFooter(false)
            })
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
        val emptyView = EmptyView(context).apply {
            showEmptyDefault()
        }
        mHomeRecommendAdapter.apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener {
                recommedRequestPage++
                presenter.getRecommendList(recommedRequestPage)
            }
            setOnItemClickListener { _, _, position ->
                val data = mHomeRecommendAdapter.data[position]
                val itemViewType = mHomeRecommendAdapter.getItemViewType(position + mHomeRecommendAdapter.headerLayoutCount)
                when (itemViewType) {
                    HomeRecommendResponse.MODULE_TYPE_1_HOTEL -> {
                        val dialog = JcsCalendarDialog()
                        dialog.initCalendar(this@HomeFragment2.activity)
                        HotelDetailActivity.goTo(this@HomeFragment2.activity, data.id, dialog.startBean, dialog.endBean, 1, "", "", 1)
                    }
                    HomeRecommendResponse.MODULE_TYPE_2_SERVICE -> {
                        startActivity(MechanismDetailActivity::class.java, Bundle().apply {
                            putString(MechanismDetailActivity.K_MECHANISM_ID, data.id.toString())
                        })
                    }
                    HomeRecommendResponse.MODULE_TYPE_3_FOOD -> {
                        startActivity(RestaurantDetailActivity::class.java,Bundle().apply {
                            putString(Constant.PARAM_ID , data.id.toString())
                        })
                    }
                    HomeRecommendResponse.MODULE_TYPE_4_TRAVEL -> {
                        TouristAttractionDetailActivity.goTo(this@HomeFragment2.activity, data.id)
                    }
                    else -> {
                    }
                }

            }
        }
    }


    private fun initScroll() {
        moduleRecycler.isNestedScrollingEnabled = true
        rv_home.isNestedScrollingEnabled = true
        nested_scroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ -> // 滑到的底部
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                mHomeRecommendAdapter.loadMoreModule.loadMoreToLoading()
            }
        })
    }


    override fun initData() {
        presenter = HomePresenter2(this)
        rxTimer = RxTimer()
        initCity()
        presenter.getMessageCount()
        presenter.getTopBanner()
        presenter.getPlateData()
        presenter.getNewsList()
        presenter.getRecommendList(recommedRequestPage)
        presenter.checkAppVersion()
    }

    override fun bindListener() {
        city_tv.setOnClickListener {
            startActivityForResult(CityPickerActivity::class.java, REQ_SELECT_CITY, null)
        }
        search_ll.setOnClickListener {
            startActivityAfterLogin(SearchAllActivity::class.java)
        }
        message_view.setOnClickListener {
            startActivityAfterLogin(MessageCenterActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isViewCreated) {
            presenter.getMessageCount()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            presenter.getMessageCount()
        }
    }

    override fun onDestroy() {
        rxTimer.cancel()
        super.onDestroy()
        top_banner?.releaseBanner()
    }

    override fun onRefresh() {

        // 推荐
        swipeLayout.isRefreshing = true
        recommedRequestPage = Constant.DEFAULT_FIRST_PAGE
        presenter.getRecommendList(recommedRequestPage)
        presenter.getMessageCount()
        presenter.getTopBanner()
        presenter.getPlateData()
        presenter.getNewsList()
    }

    override fun bindRecommendData(data: MutableList<HomeRecommendResponse>, lastPage: Boolean) {
        swipeLayout.isRefreshing = false
        val loadMoreModule = mHomeRecommendAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (recommedRequestPage == Constant.DEFAULT_FIRST_PAGE) {
                loadMoreModule.loadMoreComplete()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (recommedRequestPage == Constant.DEFAULT_FIRST_PAGE) {
            mHomeRecommendAdapter.setNewInstance(data)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mHomeRecommendAdapter.addData(data)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        when (requestCode) {
            REQ_SELECT_CITY -> {
                // 选择城市
                city_tv.text = data.getStringExtra(CityPickerActivity.EXTRA_CITY)
                onRefresh()
            }
            else -> {
            }
        }

    }

    override fun setMessageCount(i: Int) = message_view.setMessageCount(i)

    override fun bindTopBannerData(bannerUrls: ArrayList<String>) {
        top_banner.setImageUrls(bannerUrls)
        top_banner.start()
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
            news_split_v.visibility = View.GONE
            return
        }
        news_rl.visibility = View.VISIBLE
        news_rv.visibility = View.VISIBLE
        news_split_v.visibility = View.VISIBLE

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
        if (mNewsAdapter.data.size <= 1) {
            return
        }
        rxTimer.interval(3000) {
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

}