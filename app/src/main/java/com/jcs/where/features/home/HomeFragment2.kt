package com.jcs.where.features.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
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
import com.jcs.where.R
import com.jcs.where.adapter.ModulesAdapter
import com.jcs.where.api.response.ModulesResponse
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.convenience.activity.ConvenienceServiceActivity
import com.jcs.where.features.gourmet.restaurant.list.RestaurantListActivity
import com.jcs.where.features.message.MessageCenterActivity
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.government.activity.GovernmentMapActivity
import com.jcs.where.government.activity.MechanismDetailActivity
import com.jcs.where.home.activity.TravelStayActivity
import com.jcs.where.home.decoration.HomeModulesItemDecoration
import com.jcs.where.hotel.activity.CityPickerActivity
import com.jcs.where.hotel.activity.HotelDetailActivity
import com.jcs.where.integral.child.task.HomeRecommendAdapter
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
 *
 */
class HomeFragment2 : BaseMvpFragment<HomePresenter2>(), HomeView2, SwipeRefreshLayout.OnRefreshListener {


    private var page = Constant.DEFAULT_FIRST_PAGE

    /** 选择城市 */
    private val REQ_SELECT_CITY = 100

    private lateinit var mModulesAdapter: ModulesAdapter
    private lateinit var mHomeRecommendAdapter: HomeRecommendAdapter

    override fun getLayoutId() = R.layout.fragment_home2

    override fun initView(view: View) {
        BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.rl_title))
        initBanner()
        initPlate()
        initRecommend()

    }

    /** 推荐列表 */
    private fun initRecommend() {
        swipeLayout.setOnRefreshListener(this)
        mHomeRecommendAdapter = HomeRecommendAdapter()
        rv_home.apply {
            adapter = mHomeRecommendAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(15f), 0, 0).apply {
                setDrawHeaderFooter(false)
            })
        }
        val emptyView = EmptyView(context).apply {
            showEmptyDefault()
        }
        mHomeRecommendAdapter.apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getRecommendList(page)
            }
            setOnItemClickListener { adapter, view, position ->
                val data = mHomeRecommendAdapter.data[position]
                val itemViewType = mHomeRecommendAdapter.getItemViewType(position + mHomeRecommendAdapter.headerLayoutCount)
                when (itemViewType) {
                    HomeRecommendResponse.MODULE_TYPE_1_HOTEL -> {
                        val dialog = JcsCalendarDialog().apply {
                            initCalendar(context)
                        }
                        HotelDetailActivity.goTo(activity, data.id, dialog.startBean, dialog.endBean, 1, "", "", 1)
                    }
                    HomeRecommendResponse.MODULE_TYPE_2_SERVICE -> {
                        startActivity(MechanismDetailActivity::class.java, Bundle().apply {
                            putString(MechanismDetailActivity.K_MECHANISM_ID, data.id.toString())
                        })
                    }
                    HomeRecommendResponse.MODULE_TYPE_3_FOOD -> {
                        showComing()
                    }
                    HomeRecommendResponse.MODULE_TYPE_4_TRAVEL -> {
                        TouristAttractionDetailActivity.goTo(activity, data.id)
                    }
                    else -> {
                    }
                }

            }
        }


    }

    /** 金刚区相关 */
    private fun initPlate() {
        mModulesAdapter = ModulesAdapter()
        moduleRecycler.apply {
            addItemDecoration(HomeModulesItemDecoration())
            layoutManager = GridLayoutManager(context, 5, RecyclerView.VERTICAL, false)
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
                    4, 5, 6, 7, 8 -> {
                        val convenienceCategoryId = data.categories.toString()
                        val name = data.name
                        startActivity(ConvenienceServiceActivity::class.java, Bundle().apply {
                            putString(ConvenienceServiceActivity.K_CATEGORIES, convenienceCategoryId)
                            putString(ConvenienceServiceActivity.K_SERVICE_NAME, name)
                        })
                    }
                    9 -> startActivity(RestaurantListActivity::class.java)
                    else -> showComing()
                }
            }
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
                .transform(GlideRoundTransform(10))

        top_banner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
                .setImageLoader(object : AbstractUrlLoader() {
                    override fun loadImages(context: Context, url: String, image: ImageView) {
                        Glide.with(context).load(url).apply(options).into(image)
                    }

                    override fun loadGifs(context: Context, url: String, gifImageView: GifImageView, scaleType: ImageView.ScaleType) {
                        Glide.with(context).load(url).apply(options).into(gifImageView)
                    }
                })
                .setTitleHeight(50)
                .isAutoPlay(true)
                .setDelay(5000)
                .setUpIndicators(R.drawable.ic_selected, R.drawable.ic_unselected)
                .setUpIndicatorSize(20, 6)
                .setIndicatorGravity(XBanner.INDICATOR_CENTER)
    }

    override fun initData() {
        presenter = HomePresenter2(this)
        presenter.getMessageCount()
        presenter.getTopBanner(top_banner)
        presenter.getPlateData()
        presenter.getRecommendList(page)
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
        super.onDestroy()
        top_banner.releaseBanner()
    }

    override fun onRefresh() {

        // 推荐
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getRecommendList(page)
    }

    override fun bindDetailData(data: MutableList<HomeRecommendResponse>, lastPage: Boolean) {
        swipeLayout.isRefreshing = false


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        if (requestCode == REQ_SELECT_CITY) {
            city_tv.text = data.getStringExtra(CityPickerActivity.EXTRA_CITY)
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

}