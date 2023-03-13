package com.jiechengsheng.city.features.travel.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.appbar.AppBarLayout
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.BannerResponse
import com.jiechengsheng.city.api.response.category.Category
import com.jiechengsheng.city.api.response.recommend.HomeRecommendResponse
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jiechengsheng.city.features.home.AppBarStateChangeListener
import com.jiechengsheng.city.features.home.HomeRecommendAdapter
import com.jiechengsheng.city.features.hotel.detail.HotelDetailActivity2
import com.jiechengsheng.city.features.hotel.home.HotelHomeActivity
import com.jiechengsheng.city.features.mechanism.MechanismActivity
import com.jiechengsheng.city.features.travel.detail.TravelDetailActivity
import com.jiechengsheng.city.features.travel.map.TravelMapActivity
import com.jiechengsheng.city.features.web.WebViewActivity
import com.jiechengsheng.city.frames.common.Html5Url
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.GlideUtil
import com.jiechengsheng.city.view.XBanner.AbstractUrlLoader
import com.jiechengsheng.city.view.XBanner.XBanner
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.calendar.JcsCalendarDialog
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_travel_home.*
import pl.droidsonroids.gif.GifImageView

/**
 * Created by Wangsw  2021/9/13 11:14.
 * 旅游首页
 */
class TravelHomeActivity : BaseMvpActivity<TravelHomePresenter>(), TravelHomeView, SwipeRefreshLayout.OnRefreshListener {


    private var categories: ArrayList<Int> = ArrayList()

    /** 推荐列表请求 */
    private var page = Constant.DEFAULT_FIRST_PAGE

    /** 推荐 */
    private lateinit var mRecommendAdapter: HomeRecommendAdapter

    /** 功能区 */
    private lateinit var mModuleAdapter: TravelModuleAdapter

    override fun isStatusDark() = true


    override fun getLayoutId() = R.layout.activity_travel_home

    override fun initView() {

        categories = intent.getIntegerArrayListExtra(Constant.PARAM_CATEGORY_ID)!!
        initModule()
        initBanner()
        initScroll()
        initRecommend()
    }

    private fun initModule() {
        mModuleAdapter = TravelModuleAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val category = mModuleAdapter.data[position]
                val childId = category.id
                if (category.nativeIsWebType) {
                    WebViewActivity.navigation(this@TravelHomeActivity, Html5Url.TOURISM_MANAGEMENT_URL)
                } else {

                    when (category.type) {
                        1 -> {
                            // 酒店
                            startActivity(HotelHomeActivity::class.java, Bundle().apply {
                                putInt(Constant.PARAM_CATEGORY_ID, childId)
                            })
                        }
                        2 -> {
                            TravelMapActivity.navigation(this@TravelHomeActivity, childId)
                        }
                    }
                }


            }
        }

        module_rv.apply {
            layoutManager = object : GridLayoutManager(context, 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = mModuleAdapter
        }

    }

    private fun initScroll() {
        rv_home.isNestedScrollingEnabled = true
        bottom_cl.isNestedScrollingEnabled = true

        child_abl.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, expanded: State, verticalOffset: Int) {
                swipeLayout.isEnabled = verticalOffset >= 0 // 滑动到顶部时开启
            }
        })
        swipeLayout.postDelayed({ swipeLayout.isEnabled = false }, 200)

    }

    /** 轮播图 */
    private fun initBanner() {
        top_banner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
            .setTitleHeight(50)
            .isAutoPlay(true)
            .setDelay(5000)
            .setUpIndicators(R.drawable.ic_selected, R.drawable.ic_unselected)
            .setUpIndicatorSize(6, 6)
            .setIndicatorGravity(XBanner.INDICATOR_CENTER)
            .setImageLoader(object : AbstractUrlLoader() {
                override fun loadImages(context: Context, url: String, image: ImageView) = GlideUtil.load(context, url, image, 4)

                override fun loadGifs(context: Context, url: String, gifImageView: GifImageView, scaleType: ImageView.ScaleType) =
                    GlideUtil.load(context, url, gifImageView, 4)
            })
    }

    private lateinit var emptyView: EmptyView

    /** 推荐列表 */
    private fun initRecommend() {
        swipeLayout.setOnRefreshListener(this)
        swipeLayout.setColorSchemeColors(ColorUtils.getColor(R.color.blue_377BFF))

        mRecommendAdapter = HomeRecommendAdapter()
        rv_home.apply {
            adapter = mRecommendAdapter
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
        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            addEmptyList(this)
        }
        mRecommendAdapter.apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getRecommendList()
            }
            setOnItemClickListener { _, _, position ->
                val data = mRecommendAdapter.data[position]
                val itemViewType = mRecommendAdapter.getItemViewType(position + mRecommendAdapter.headerLayoutCount)
                when (itemViewType) {
                    HomeRecommendResponse.MODULE_TYPE_1_HOTEL -> {
                        val dialog = JcsCalendarDialog()
                        dialog.initCalendar()
                        HotelDetailActivity2.navigation(this@TravelHomeActivity, data.id, dialog.startBean, dialog.endBean)

                    }
                    HomeRecommendResponse.MODULE_TYPE_2_SERVICE -> {
                        startActivity(MechanismActivity::class.java, Bundle().apply {
                            putInt(Constant.PARAM_ID, data.id)
                        })
                    }
                    HomeRecommendResponse.MODULE_TYPE_3_FOOD -> {
                        startActivity(RestaurantDetailActivity::class.java, Bundle().apply {
                            putInt(Constant.PARAM_ID, data.id)
                        })
                    }
                    HomeRecommendResponse.MODULE_TYPE_4_TRAVEL -> {
                        TravelDetailActivity.navigation(this@TravelHomeActivity, data.id)
                    }
                }

            }
        }
    }


    override fun initData() {
        presenter = TravelHomePresenter(this)

        presenter.getTopBanner()
        presenter.getCategories(categories)
        presenter.getRecommendList()
    }

    override fun bindListener() {

    }


    override fun bindTopBannerData(bannerUrls: ArrayList<String>, response: ArrayList<BannerResponse>) {
        top_banner.setImageUrls(bannerUrls)
        top_banner.setBannerPageListener(object : XBanner.BannerPageListener {


            override fun onBannerDragging(item: Int) = Unit

            override fun onBannerIdle(item: Int) = Unit

            override fun onBannerClick(item: Int) {
                val data = response[item]
                BusinessUtils.handleBannerClick(this@TravelHomeActivity, data)
            }

        }).start()
    }

    override fun bindPlateData(toMutableList: MutableList<Category>) {
        mModuleAdapter.setNewInstance(toMutableList)
    }

    override fun bindRecommendData(data: MutableList<HomeRecommendResponse>, lastPage: Boolean) {
        swipeLayout.isRefreshing = false
        val loadMoreModule = mRecommendAdapter.loadMoreModule
        if (data.isNullOrEmpty()) {
            emptyView.showEmptyContainer()
        }
        mRecommendAdapter.setNewInstance(data)
        loadMoreModule.loadMoreEnd()
    }

    override fun onRefresh() {
        top_banner.pause()
        presenter.getTopBanner()

        swipeLayout.isRefreshing = true
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getRecommendList()
    }


    override fun onPause() {
        super.onPause()
        top_banner?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        top_banner?.releaseBanner()
    }

    override fun onResume() {
        super.onResume()
        top_banner?.startPlayIfNeeded()
    }


}