package com.jcs.where.features.order

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.order.OrderListResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.gourmet.order.detail2.DelicacyOrderDetailActivity
import com.jcs.where.features.gourmet.takeaway.order2.TakeawayOrderDetailActivity
import com.jcs.where.features.hotel.order.HotelOrderDetailActivity
import com.jcs.where.features.mall.order.MallOrderDetailActivity
import com.jcs.where.features.store.order.detail.StoreOrderDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_order_child.*

/**
 * Created by Wangsw  2021/5/12 14:08.
 * 订单列表
 */
class OrderChildFragment : BaseMvpFragment<OrderChildPresenter>(), OrderChildView, OnLoadMoreListener,
    SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, ConfirmReceipt {

    companion object {
        @JvmStatic
        fun getInstance(type: Int): OrderChildFragment {
            return OrderChildFragment().apply {
                orderType = type
            }
        }
    }

    private lateinit var mAdapter: OrderListAdapter

    private var page = Constant.DEFAULT_FIRST_PAGE

    /**
     * 0.全部
     * 1.待支付
     * 2.待使用
     * 3.待评价
     * 4.退款售后
     */
    var orderType: Int = 0


    override fun getLayoutId() = R.layout.fragment_order_child

    override fun initView(view: View?) {

        swipe_layout.setOnRefreshListener(this)
        swipe_layout.setBackgroundColor(ColorUtils.getColor(R.color.white))
        val emptyView = EmptyView(requireContext()).apply {
            showEmptyDefault()
        }
        mAdapter = OrderListAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener(this@OrderChildFragment)
            setOnItemClickListener(this@OrderChildFragment)
            confirmReceipt = this@OrderChildFragment
            orderTimeOut = object : OrderTimeOut {
                override fun timeOutRefresh() {
                    onRefresh()
                }
            }
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
        presenter = OrderChildPresenter(this)
    }

    override fun bindListener() {

    }

    override fun loadOnVisible() {
        onRefresh()
    }

    override fun bindList(toMutableList: MutableList<OrderListResponse>, lastPage: Boolean) {


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

    override fun confirmReceipt() {
        onRefresh()
    }

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        if (isViewCreated) {
            presenter.getList(orderType, page)
        }
    }

    override fun onLoadMore() {
        page++
        presenter.getList(orderType, page)
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

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val viewType = mAdapter.getItemViewType(position)
        val data = mAdapter.data[position]
        when (viewType) {
            OrderListResponse.ORDER_TYPE_HOTEL_1 -> {
                startActivity(HotelOrderDetailActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ORDER_ID, data.id)
                })
            }
            OrderListResponse.ORDER_TYPE_DINE_2 -> {
                startActivity(DelicacyOrderDetailActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ORDER_ID, data.id)
                })
            }

            OrderListResponse.ORDER_TYPE_TAKEAWAY_3 -> {
                startActivity(TakeawayOrderDetailActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ORDER_ID, data.id)
                })
            }

            OrderListResponse.ORDER_TYPE_STORE_4 -> {
                startActivity(StoreOrderDetailActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ORDER_ID, data.id)
                })
            }
            OrderListResponse.ORDER_TYPE_STORE_MALL_5 -> {
                startActivity(MallOrderDetailActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ORDER_ID, data.id)
                })
            }
            else -> {
            }
        }

    }

    override fun onConfirmReceiptClick(orderId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.prompt)
            .setMessage(R.string.confirm_receipt_hint)
            .setCancelable(false)
            .setPositiveButton(R.string.ensure) { dialogInterface, i ->
                presenter.confirmReceipt(orderId)
                dialogInterface.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.dismiss() }
            .create().show()
    }


}