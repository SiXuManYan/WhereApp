package com.jcs.where.features.mall.result

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.home.child.MallRecommendAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_mall_search_result.*

/**
 * Created by Wangsw  2021/12/10 10:16.
 * 商城搜索结果
 */
class MallSearchResultActivity : BaseMvpActivity<MallSearchPresenter>(), MallSearchView {

    private var searchInput: String? = null

    /** 商品推荐 */
    private lateinit var mAdapter: MallRecommendAdapter

    private var page = Constant.DEFAULT_FIRST_PAGE

    override fun getLayoutId() = R.layout.activity_mall_search_result

    override fun isStatusDark() = true

    override fun initView() {
        searchInput = intent.getStringExtra(Constant.PARAM_NAME)
        search_tv.text = searchInput
        initContent()
    }

    private fun initContent() {

        val emptyView = EmptyView(this)
        emptyView.showEmptyDefault()

        mAdapter = MallRecommendAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getMallList(page, searchInput)
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
        presenter = MallSearchPresenter(this)
        presenter.getMallList(page, searchInput)
    }

    override fun bindListener() {

    }

    override fun bindData(data: MutableList<MallGood>, lastPage: Boolean) {
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
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

}


interface MallSearchView : BaseMvpView {
    fun bindData(data: MutableList<MallGood>, lastPage: Boolean)

}

class MallSearchPresenter(private var view: MallSearchView) : BaseMvpPresenter(view) {

    fun getMallList(page: Int, title: String? = null) {
        requestApi(mRetrofit.getMallGoodList(
            page,
            null,
            title,
            null,
            null,
            null,
            null,
            null
        ), object : BaseMvpObserver<PageResponse<MallGood>>(view) {
            override fun onSuccess(response: PageResponse<MallGood>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)

            }
        })
    }
}
