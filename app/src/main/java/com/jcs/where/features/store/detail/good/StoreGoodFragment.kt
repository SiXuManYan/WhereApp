package com.jcs.where.features.store.detail.good

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.store.StoreGoods
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.fragment_refresh_list_no_refresh.*

/**
 * Created by Wangsw  2021/6/16 15:27.
 * 商城 商品列表
 */
class StoreGoodFragment : BaseMvpFragment<StoreGoodPresenter>(), StoreGoodView, OnLoadMoreListener {




    /** 商家id */
    private var shop_id: String = ""

    private var page = Constant.DEFAULT_FIRST_PAGE

    private  lateinit var mAdapter: StoreGoodAdapter
    private lateinit var emptyView: EmptyView


    companion object {

        /**
         * 美食评论
         * @param shop_id 商家ID
         */
        fun newInstance(shop_id: String): StoreGoodFragment {
            val fragment = StoreGoodFragment()
            val bundle = Bundle().apply {
                putString(Constant.PARAM_SHOP_ID, shop_id)
            }
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutId() = R.layout.fragment_refresh_list_no_refresh

    override fun initView(view: View) {

        arguments?.let {
            shop_id = it.getString(Constant.PARAM_SHOP_ID, "")
        }

        emptyView = EmptyView(context).apply {
            showEmptyNothing()
        }
        mAdapter = StoreGoodAdapter().apply {
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener(this@StoreGoodFragment)
            setEmptyView(emptyView)
        }
        recycler.adapter = mAdapter


    }

    override fun initData() {
        presenter = StoreGoodPresenter(this)
    }

    override fun bindListener() {

    }

    override fun onLoadMore() {
        page++
        loadOnVisible()
    }

    override fun loadOnVisible() {
        presenter.getGood( shop_id,page)
    }

    override fun bindData(data: MutableList<StoreGoods>, lastPage: Boolean) {
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyDefault()
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