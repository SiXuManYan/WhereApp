package com.jcs.where.features.order.refund

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.order.RefundOrder
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.mall.refund.order.MallRefundInfoActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_order_child.*

/**
 * Created by Wangsw  2022/3/26 10:41.
 *
 */
class RefundOrderFragment : BaseMvpFragment<RefundOrderPresenter>(), RefundOrderView, SwipeRefreshLayout.OnRefreshListener,
    OnLoadMoreListener, OnItemClickListener {

    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var mAdapter: RefundOrderAdapter

    override fun getLayoutId() = R.layout.fragment_order_child

    override fun initView(view: View) {
        swipe_layout.setOnRefreshListener(this)
        swipe_layout.setBackgroundColor(ColorUtils.getColor(R.color.white))
        val emptyView = EmptyView(requireContext()).apply {
            showEmptyDefault()
        }


        mAdapter = RefundOrderAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener(this@RefundOrderFragment)
            setOnItemClickListener(this@RefundOrderFragment)

        }


        recycler_view.apply {
            adapter = mAdapter

            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.white),
                SizeUtils.dp2px(32f),
                SizeUtils.dp2px(15f),
                SizeUtils.dp2px(15f)))

        }


    }

    override fun initData() {
        presenter = RefundOrderPresenter(this)

    }

    override fun bindListener() = Unit

    override fun loadOnVisible() = onRefresh()

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        if (isViewCreated) {
            presenter.getList(page)
        }
    }

    override fun onLoadMore() {
        page++
        presenter.getList(page)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        val goodInfo = data.good_info
        MallRefundInfoActivity.navigation(requireContext(), goodInfo.order_id, goodInfo.refund_id)
    }

    override fun bindList(toMutableList: MutableList<RefundOrder>, lastPage: Boolean) {

        if (swipe_layout != null) {
            swipe_layout.isRefreshing = false
        }

        val loadMoreModule = mAdapter.loadMoreModule
        if (toMutableList.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter.setNewInstance(toMutableList)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mAdapter.addData(toMutableList)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {

        if (baseEvent == null) {
            return
        }

        when (baseEvent.code) {
            EventCode.EVENT_LOGIN_SUCCESS,
            EventCode.EVENT_ORDER_COMMIT_SUCCESS,
            -> {
                onRefresh()
            }
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                if (isViewCreated) {
                    onRefresh()
                }
            }
            EventCode.EVENT_SIGN_OUT -> {
                if (isViewCreated) {
                    mAdapter.setNewInstance(null)
                }
            }
            else -> {
            }
        }
    }
}