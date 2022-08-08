package com.jcs.where.features.collection.good

import android.graphics.Color
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.collection.MallGoodCollection
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.mall.detail.MallDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2021/12/28 10:58.
 * 商品收藏
 */
class CollectionGoodFragment : BaseMvpFragment<CollectionGoodPresenter>(), CollectionGoodView, OnItemClickListener {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: CollectionGoodAdapter
    private lateinit var emptyView: EmptyView


    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View) {
        swipe_layout.apply {
            setBackgroundColor(Color.WHITE)
            setOnRefreshListener {
                page = Constant.DEFAULT_FIRST_PAGE
                loadOnVisible()
            }
        }
        emptyView = EmptyView(requireContext()).apply {
            setEmptyImage(R.mipmap.ic_empty_favorite)
            setEmptyHint(R.string.empty_favorite)
            addEmptyList(this)
        }

        mAdapter = CollectionGoodAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@CollectionGoodFragment)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                loadOnVisible()
            }
        }
        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(15f), 0, 0))
        }
    }

    override fun initData() {
        presenter = CollectionGoodPresenter(this)
    }

    override fun loadOnVisible() {
        presenter.getData(page)
    }

    override fun bindListener() = Unit

    override fun bindData(data: MutableList<MallGoodCollection>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }

        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyContainer()
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

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val itemData = mAdapter.data[position]

        if (itemData.delete_status == 1 || itemData.good_status == 0) {
            // 已删除或下架，不处理
            return
        }
        MallDetailActivity.navigation(requireContext(), itemData.good_id)
    }


}

interface CollectionGoodView : BaseMvpView {
    fun bindData(data: MutableList<MallGoodCollection>, lastPage: Boolean)
}

class CollectionGoodPresenter(private var view: CollectionGoodView) : BaseMvpPresenter(view) {

    fun getData(page: Int) {
        requestApi(mRetrofit.collectionGoodList(page), object : BaseMvpObserver<PageResponse<MallGoodCollection>>(view) {
            override fun onSuccess(response: PageResponse<MallGoodCollection>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }

        })

    }
}