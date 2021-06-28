package com.jcs.where.features.order

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.order.OrderListResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.gourmet.order.detail.FoodOrderDetailActivity
import com.jcs.where.features.gourmet.takeaway.order.TakeawayOrderDetailActivity
import com.jcs.where.features.store.order.detail.StoreOrderDetailActivity
import com.jcs.where.home.adapter.OrderListAdapter2
import com.jcs.where.home.decoration.MarginTopDecoration
import com.jcs.where.hotel.activity.HotelOrderDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.fragment_order_child.*

/**
 * Created by Wangsw  2021/5/12 14:08.
 * 订单列表
 */
class OrderChildFragment : BaseMvpFragment<OrderChildPresenter>(), OrderChildView, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    companion object {
        @JvmStatic
        fun getInstance(type: Int): OrderChildFragment {
            return OrderChildFragment().apply {
                orderType = type
            }
        }
    }

    private lateinit var mAdapter: OrderListAdapter2

    private var page = Constant.DEFAULT_FIRST_PAGE

    /**
     * 0.全部
     * 1.待支付
     * 2.待使用
     * 3.待评价
     * 4.退款售后
     */
    public var orderType: Int = 0


    override fun getLayoutId() = R.layout.fragment_order_child

    override fun initView(view: View?) {

        swipe_layout.setOnRefreshListener(this)
        val emptyView = EmptyView(context).apply {
            showEmptyDefault()
        }
        mAdapter = OrderListAdapter2().apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener(this@OrderChildFragment)
            setOnItemClickListener(this@OrderChildFragment)
        }
        recycler_view.apply {
            adapter = mAdapter
            addItemDecoration(object : MarginTopDecoration() {
                override fun getMarginTop(): Int = 10
            })

        }


    }

    override fun initData() {
        presenter = OrderChildPresenter(this)
    }

    override fun bindListener() {

    }

    override fun loadOnVisible() {
        presenter.getList(orderType, page)
    }

    override fun bindList(toMutableList: MutableList<OrderListResponse>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
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

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getList(orderType, page)
    }

    override fun onLoadMore() {
        page++
        presenter.getList(orderType, page)
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {

        if (baseEvent == null) {
            return
        }
        if (baseEvent.code == EventCode.EVENT_REFRESH_ORDER_LIST) {
            onRefresh()
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val viewType = mAdapter.getItemViewType(position)
        val data = mAdapter.data[position]
        when (viewType) {
            OrderListResponse.ORDER_TYPE_HOTEL_1 -> {
                HotelOrderDetailActivity.goTo(context, data.id.toString())
            }
            OrderListResponse.ORDER_TYPE_DINE_2 -> {
                startActivity(FoodOrderDetailActivity::class.java, Bundle().apply {
                    putString(Constant.PARAM_ORDER_ID, data.id.toString())
                })
            }

            OrderListResponse.ORDER_TYPE_TAKEAWAY_3 -> {
                startActivity(TakeawayOrderDetailActivity::class.java, Bundle().apply {
                    putString(Constant.PARAM_ORDER_ID, data.id.toString())
                })
            }

            OrderListResponse.ORDER_TYPE_STORE_4 -> {
                startActivity(StoreOrderDetailActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ORDER_ID, data.id)
                })
            }
            else -> {
            }
        }

    }


}