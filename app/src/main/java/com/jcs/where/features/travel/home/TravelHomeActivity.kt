package com.jcs.where.features.travel.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.currency.WebViewActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.home.HomeRecommendAdapter
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.hotel.activity.HotelDetailActivity
import com.jcs.where.news.NewsDetailActivity
import com.jcs.where.travel.TouristAttractionDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_travel_home.*
import kotlinx.android.synthetic.main.activity_travel_home.bottom_cl
import kotlinx.android.synthetic.main.activity_travel_home.ll_banner
import kotlinx.android.synthetic.main.activity_travel_home.rv_home
import kotlinx.android.synthetic.main.activity_travel_home.swipeLayout
import kotlinx.android.synthetic.main.activity_travel_home.top_banner
import kotlinx.android.synthetic.main.fragment_home3.*

import pl.droidsonroids.gif.GifImageView
import java.util.*

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


    override fun getLayoutId() = R.layout.activity_travel_home

    override fun initView() {

        categories = intent.getIntegerArrayListExtra(Constant.PARAM_CATEGORY_ID)!!
        initModule()
        initBanner()
        initScroll()
    }

    private fun initModule() {
        mModuleAdapter = TravelModuleAdapter().apply {
            setOnItemClickListener { _, _, position ->
                mModuleAdapter.data[position]




            }
        }

        module_rv.apply {
               layoutManager = object : GridLayoutManager(context, 5, RecyclerView.VERTICAL, false) {
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
    }

    /** 轮播图 */
    private fun initBanner() {
        val bannerParams = ll_banner.layoutParams.apply {
            height = ScreenUtils.getScreenWidth() * 142 / 343
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
                override fun loadImages(context: Context, url: String, image: ImageView) = GlideUtil.load(context, url, image, 4)

                override fun loadGifs(context: Context, url: String, gifImageView: GifImageView, scaleType: ImageView.ScaleType) =
                    GlideUtil.load(context, url, gifImageView, 4)
            })
    }

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
        val emptyView = EmptyView(this).apply {
            showEmptyDefault()
        }
        mRecommendAdapter.apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getRecommendList(page)
            }
            setOnItemClickListener { _, _, position ->
                val data = mRecommendAdapter.data[position]
                val itemViewType = mRecommendAdapter.getItemViewType(position + mRecommendAdapter.headerLayoutCount)
                when (itemViewType) {
                    HomeRecommendResponse.MODULE_TYPE_1_HOTEL -> {
                        val dialog = JcsCalendarDialog()
                        dialog.initCalendar(this@TravelHomeActivity)
                        HotelDetailActivity.goTo(this@TravelHomeActivity, data.id, dialog.startBean, dialog.endBean, 1, "", "", 1)
                    }
                    HomeRecommendResponse.MODULE_TYPE_2_SERVICE -> {
                        startActivity(MechanismActivity::class.java, Bundle().apply {
                            putInt(Constant.PARAM_ID, data.id)
                        })
                    }
                    HomeRecommendResponse.MODULE_TYPE_3_FOOD -> {
                        startActivity(RestaurantDetailActivity::class.java, Bundle().apply {
                            putString(Constant.PARAM_ID, data.id.toString())
                        })
                    }
                    HomeRecommendResponse.MODULE_TYPE_4_TRAVEL -> {
                        TouristAttractionDetailActivity.goTo(this@TravelHomeActivity, data.id)
                    }
                }

            }
        }
    }


    override fun initData() {
        presenter = TravelHomePresenter(this)

        presenter.getTopBanner()
        presenter.getCategories(categories)
        presenter.getRecommendList(page)
    }

    override fun bindListener() {

    }


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
                    WebViewActivity.goTo(this@TravelHomeActivity, data.h5_link)
                    return
                }

                if (data.redirect_type == 2) {

                    when (data.target_type) {
                        1 -> {
                            val dialog = JcsCalendarDialog()
                            dialog.initCalendar(this@TravelHomeActivity)
                            HotelDetailActivity.goTo(
                                this@TravelHomeActivity,
                                data.target_id,
                                dialog.startBean,
                                dialog.endBean,
                                1,
                                "",
                                "",
                                1
                            )
                        }
                        2 -> {
                            TouristAttractionDetailActivity.goTo(this@TravelHomeActivity, data.target_id)
                        }
                        3 -> {
                            startActivity(NewsDetailActivity::class.java, Bundle().apply {
                                putString(Constant.PARAM_NEWS_ID, data.target_id.toString())
                            })
                        }
                        4 -> {
                            startActivity(MechanismActivity::class.java, Bundle().apply {
                                putInt(Constant.PARAM_ID, data.target_id)
                            })
                        }
                        5 -> {
                            startActivity(RestaurantDetailActivity::class.java, Bundle().apply {
                                putString(Constant.PARAM_ID, data.target_id.toString())
                            })
                        }
                    }
                    return
                }
            }

        }).start()
    }

    override fun bindPlateData(toMutableList: MutableList<Category>) {
        mModuleAdapter.setNewInstance(toMutableList)
    }

    override fun bindRecommendData(data: MutableList<HomeRecommendResponse>, lastPage: Boolean) {
        swipeLayout.isRefreshing = false
        val loadMoreModule = mRecommendAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                loadMoreModule.loadMoreComplete()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mRecommendAdapter.setNewInstance(data)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mRecommendAdapter.addData(data)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun onRefresh() {
        top_banner.pause()
        presenter.getTopBanner()

        swipeLayout.isRefreshing = true
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getRecommendList(page)
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
        top_banner?.start()
    }


}