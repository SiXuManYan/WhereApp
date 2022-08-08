package com.jcs.where.features.home.child

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.api.response.mall.MallCategory
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.home.HomeRecommendAdapter
import com.jcs.where.features.home.child.header.HomeChildHeader
import com.jcs.where.features.home.child.header.OnChildCategoryClick
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.travel.detail.TravelDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_home_child.*

/**
 * Created by Wangsw  2022/6/21 14:25.
 * 首页综合推荐
 */
class ComplexChildFragment : BaseMvpFragment<HomeChildPresenter>(), HomeChildView, OnChildCategoryClick {


    /** 轮播图 */
    var banner = ArrayList<BannerResponse>()

    /** 子分类 */
    var category = ArrayList<MallCategory>()


    private lateinit var mHeader: HomeChildHeader

    private lateinit var emptyView: EmptyView

    /** 推荐列表请求 */
    private var page = Constant.DEFAULT_FIRST_PAGE

    private var categoryId: Int? = null

    /** 首页推荐 */
    private lateinit var mAdapter: HomeRecommendAdapter

    companion object {

        fun newInstance(banner: ArrayList<BannerResponse>, category: ArrayList<MallCategory>): ComplexChildFragment {
            val fragment = ComplexChildFragment()
            fragment.banner.addAll(banner)
            fragment.category.addAll(category)
            return fragment
        }
    }


    override fun getLayoutId() = R.layout.fragment_home_child

    override fun initView(view: View) {
        initContent()
        initHeader()
    }


    private fun initContent() {
        emptyView = EmptyView(context).apply {
            showEmptyDefault()
            addEmptyList(this)
        }
        mAdapter = HomeRecommendAdapter().apply {
            setEmptyView(emptyView)
            headerWithEmptyEnable = true
            footerWithEmptyEnable = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener {
                page++
                getData()
            }
        }
        home_child_rv.apply {
            adapter = mAdapter
            addItemDecoration(
                DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(16f),
                    SizeUtils.dp2px(8f), SizeUtils.dp2px(8f)))
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        mAdapter.apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                getData()
            }
            setOnItemClickListener { _, _, position ->
                val data = mAdapter.data[position]
                val itemViewType = mAdapter.getItemViewType(position + mAdapter.headerLayoutCount)
                when (itemViewType) {
                    HomeRecommendResponse.MODULE_TYPE_1_HOTEL -> {
                        val dialog = JcsCalendarDialog()
                        dialog.initCalendar(this@ComplexChildFragment.activity)
                        HotelDetailActivity2.navigation(requireContext(), data.id, dialog.startBean, dialog.endBean)
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
                        TravelDetailActivity.navigation(requireContext(), data.id)
                    }
                }

            }
        }
    }


    private fun initHeader() {
        mHeader = HomeChildHeader(this@ComplexChildFragment.requireActivity())
        mAdapter.addHeaderView(mHeader)
        mHeader.bindTopBannerData(banner)
        mHeader.bindCategory(category)
        mHeader.onChildCategoryClick = this@ComplexChildFragment
    }

    override fun initData() {
        presenter = HomeChildPresenter(this)
        getData()

    }



    private fun getData() = presenter.getComplexRecommend(page, categoryId)

    override fun bindListener() {

    }


    override fun bindComplexRecommend(data: MutableList<HomeRecommendResponse>, lastPage: Boolean) {
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                emptyView.showEmptyContainer()
                loadMoreModule.loadMoreComplete()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter.setNewInstance(data)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mAdapter.addData(data)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }

    }


    override fun onChildCategoryClick(category: MallCategory) {
        page = Constant.DEFAULT_FIRST_PAGE
        categoryId = category.id
        getData()
    }


    override fun onError(errorResponse: ErrorResponse) {
        val errCode = errorResponse.getErrCode()
        if (errCode <= 0) {
            ToastUtils.showLong(errorResponse.getErrMsg())
            emptyView.showNetworkError { getData() }
        }
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        if (baseEvent.code == EventCode.EVENT_REFRESH_HOME_NEARBY) {
            getData()
        }
    }

}