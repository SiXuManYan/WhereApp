package com.jiechengsheng.city.features.home.child

import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.response.BannerResponse
import com.jiechengsheng.city.api.response.mall.MallCategory
import com.jiechengsheng.city.api.response.mall.MallGood
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.features.home.child.header.HomeChildHeader
import com.jiechengsheng.city.features.home.child.header.OnChildCategoryClick
import com.jiechengsheng.city.features.mall.home.child.MallRecommendAdapter
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_home_child.*

/**
 * Created by Wangsw  2022/6/22 11:56.
 * 首页estore
 */
class HomeMallFragment : BaseMvpFragment<HomeChildPresenter>(), HomeChildView, OnChildCategoryClick {


    /** 轮播图 */
    var banner = ArrayList<BannerResponse>()

    /** 子分类 */
    var category = ArrayList<MallCategory>()


    private lateinit var mHeader: HomeChildHeader

    private lateinit var emptyView: EmptyView

    /** 推荐列表请求 */
    private var page = Constant.DEFAULT_FIRST_PAGE

    private var categoryId = 0

    /** 商品推荐 */
    private lateinit var mAdapter: MallRecommendAdapter

    companion object {

        fun newInstance(banner: ArrayList<BannerResponse>, category: ArrayList<MallCategory>): HomeMallFragment {
            val fragment = HomeMallFragment()
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

        emptyView = EmptyView(requireContext()).apply {
            showEmptyDefault()
            addEmptyList(this)
        }


        mAdapter = MallRecommendAdapter().apply {
            setEmptyView(emptyView)
            headerWithEmptyEnable = true
            footerWithEmptyEnable = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener {
                page++
                loadData()
            }
        }
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0)
        home_child_rv.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }
    }


    private fun initHeader() {
        mHeader = HomeChildHeader(this@HomeMallFragment.requireActivity())
        mAdapter.addHeaderView(mHeader)
        mHeader.bindTopBannerData(banner)
        mHeader.bindCategory(category)
        mHeader.onChildCategoryClick = this@HomeMallFragment
    }

    override fun initData() {
        presenter = HomeChildPresenter(this)
        page = Constant.DEFAULT_FIRST_PAGE
        loadData()

    }


    override fun bindListener() = Unit


    private fun loadData() = presenter.getMallRecommend(categoryId, page)


    override fun bindMallRecommend(data: MutableList<MallGood>, lastPage: Boolean) {
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
        loadData()
    }


    override fun onError(errorResponse: ErrorResponse?) {

        val errCode = errorResponse?.getErrCode()
        if (errCode == null || errCode <= 0) {
            ToastUtils.showLong(errorResponse?.getErrMsg())
            emptyView.showNetworkError {
                page = Constant.DEFAULT_FIRST_PAGE
                loadData()
            }
        }
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_SCROLL_TO_TOP -> {
                home_child_rv.scrollToPosition(0)
            }
            else -> {}
        }

    }
}