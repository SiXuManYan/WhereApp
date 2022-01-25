package com.jcs.where.features.collection.shop

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
import com.jcs.where.api.response.collection.MallShopCollection
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.mall.shop.home.MallShopHomeActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2022/1/25 14:33.
 * 店铺收藏
 */
class CollectionShopFragment : BaseMvpFragment<CollectionShopPresenter>(), CollectionShopView, OnItemClickListener {


    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: CollectionShopAdapter
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
            initEmpty(R.mipmap.ic_empty_card_coupon, R.string.no_content)
        }

        mAdapter = CollectionShopAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@CollectionShopFragment)
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
        presenter = CollectionShopPresenter(this)
    }

    override fun loadOnVisible() {
        presenter.getData(page)
    }


    override fun bindListener() = Unit

    override fun bindData(data: MutableList<MallShopCollection>, lastPage: Boolean) {
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
        MallShopHomeActivity.navigation(requireContext(), itemData.shop_id)
    }
}


interface CollectionShopView : BaseMvpView {
    fun bindData(data: MutableList<MallShopCollection>, lastPage: Boolean)
}


class CollectionShopPresenter(private var view: CollectionShopView) : BaseMvpPresenter(view) {

    fun getData(page: Int) {
        requestApi(mRetrofit.collectionShopList(page), object : BaseMvpObserver<PageResponse<MallShopCollection>>(view) {
            override fun onSuccess(response: PageResponse<MallShopCollection>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }

        })

    }
}