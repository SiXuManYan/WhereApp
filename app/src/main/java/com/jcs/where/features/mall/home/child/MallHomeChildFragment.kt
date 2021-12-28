package com.jcs.where.features.mall.home.child

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.api.response.mall.MallBannerCategory
import com.jcs.where.api.response.mall.MallCategory
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.currency.WebViewActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.mall.detail.MallDetailActivity
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.travel.detail.TravelDetailActivity
import com.jcs.where.news.NewsDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.widget.list.DividerDecoration

import kotlinx.android.synthetic.main.fragment_mall_home_child.*
import pl.droidsonroids.gif.GifImageView

/**
 * Created by Wangsw  2021/11/30 17:01.
 * 商城首页商品
 */
class MallHomeChildFragment : BaseMvpFragment<MallHomeChildPresenter>(), MallHomeChildView {

    /** 当前页面对应的一级分类 */
    lateinit var targetFirstCategory: MallCategory

    /** 二级分类轮播 */
    private lateinit var mBannerAdapter: MallHomeChildBannerAdapter

    /** 商品推荐 */
    private lateinit var mAdapter: MallRecommendAdapter

    override fun getLayoutId() = R.layout.fragment_mall_home_child

    override fun initView(view: View) {
        initAd()
        initBanner()
        initContent()
    }

    private fun initAd() {
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


    private fun initBanner() {
        if (!::targetFirstCategory.isInitialized) return

        point_view.apply {
            selectedDrawableResId = R.drawable.shape_point_selected_377bff
            commonDrawableResId = R.drawable.shape_point_selected_d8d8d8
        }

        mBannerAdapter = MallHomeChildBannerAdapter().apply {
            setEmptyView(R.layout.view_empty_data_brvah_default)
        }
        banner_rv.apply {
            adapter = mBannerAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(banner_rv)

        banner_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val firstItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        point_view.onPageSelected(firstItemPosition)
                    }
                }
            }
        })


    }

    private fun initContent() {

        val emptyView = EmptyView(requireContext())
        emptyView.showEmptyDefault()

        mAdapter = MallRecommendAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener {
               loadMoreModule.loadMoreEnd()
            }
        }
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0)
        content_rv.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)

        }
    }

    override fun initData() {
        presenter = MallHomeChildPresenter(this)

    }


    override fun loadOnVisible() {
        presenter.handleBanner(targetFirstCategory)
        presenter.getRecommend(targetFirstCategory.id)
        presenter.getTopBanner()
    }


    override fun bindListener() {

    }

    override fun bindBannerData(result: ArrayList<MallBannerCategory>) {
        mBannerAdapter.setNewInstance(result)
        point_view.setPointCount(result.size)
    }

    override fun bindRecommend(response: ArrayList<MallGood>) {
        mAdapter.setNewInstance(response)
        mAdapter.loadMoreModule.loadMoreEnd()
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
                    WebViewActivity.goTo(this@MallHomeChildFragment.activity, data.h5_link)
                    return
                }

                if (data.redirect_type == 2) {

                    when (data.target_type) {
                        1 -> {
                            val dialog = JcsCalendarDialog()
                            dialog.initCalendar(this@MallHomeChildFragment.activity)
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
                    }
                    return
                }
            }

        }).start()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isViewCreated) {
            return
        }
        if (isVisibleToUser  ) {
            top_banner.start()
        } else {
            top_banner.pause()
        }
    }

    override fun onPause() {
        top_banner.pause()
        super.onPause()
    }

    override fun onDestroy() {
        top_banner?.releaseBanner()
        super.onDestroy()
    }


}