package com.jcs.where.features.gourmet.cart

import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_shopping_cart.*

/**
 * Created by Wangsw  2021/4/7 14:49.
 * 购物车
 */
class ShoppingCartActivity : BaseMvpActivity<ShoppingCartPresenter>(), ShoppingCartView, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: ShoppingCartAdapter

    override fun getLayoutId() = R.layout.activity_shopping_cart

    override fun initView() {

        mAdapter = ShoppingCartAdapter()
        mAdapter.apply {
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            setEmptyView(R.layout.view_empty_data_brvah_default)
            loadMoreModule.setOnLoadMoreListener(this@ShoppingCartActivity)
        }

        recycler_view.adapter = mAdapter
        recycler_view.addItemDecoration(getItemDecoration())
    }

    override fun isStatusDark() = true

    override fun initData() {
        swipe_layout.setOnRefreshListener(this)
        presenter = ShoppingCartPresenter(this)
        presenter.getData(page)
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }
        edit_tv.setOnClickListener {
            right_vs.displayedChild = 1
            bottom_vs.displayedChild = 1
        }

        cancel_tv.setOnClickListener {
            right_vs.displayedChild = 0
            bottom_vs.displayedChild = 0
        }
    }

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getData(page)
    }

    override fun onLoadMore() {
        page++
        presenter.getData(page)
    }

    private fun getItemDecoration(): ItemDecoration {
        val itemDecoration = DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(10f), SizeUtils.dp2px(15f), SizeUtils.dp2px(15f))
        itemDecoration.setDrawHeaderFooter(true)
        return itemDecoration
    }

    override fun bindList(data: MutableList<ShoppingCartResponse>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }

        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
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