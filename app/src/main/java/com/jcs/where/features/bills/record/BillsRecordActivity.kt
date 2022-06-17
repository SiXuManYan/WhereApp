package com.jcs.where.features.bills.record

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.bills.BillsRecord
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.order.BillsOrderDetailActivity
import com.jcs.where.features.bills.record.result.BillsRecommitResultActivity
import com.jcs.where.features.gourmet.refund.ComplexRefundActivity
import com.jcs.where.features.gourmet.refund.ComplexRefundPresenter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_refresh_list.*

/**
 * Created by Wangsw  2022/6/11 13:36.
 * 缴费记录
 */
class BillsRecordActivity : BaseMvpActivity<BillsRecordPresenter>(), BillsRecordView, SwipeRefreshLayout.OnRefreshListener,
    OnLoadMoreListener, OnItemClickListener, OnItemChildClickListener {

    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: BillsRecordAdapter

    override fun isStatusDark(): Boolean = true
    override fun getLayoutId() = R.layout.activity_refresh_list

    override fun initView() {
        mJcsTitle.setMiddleTitle(getString(R.string.payment_record))
        initList()
    }

    private fun initList() {
        swipe_layout.setOnRefreshListener(this)
        emptyView = EmptyView(this).apply {
            setEmptyMessage(R.string.empty_data_cart)
        }
        mAdapter = BillsRecordAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener(this@BillsRecordActivity)
            addChildClickViewIds(R.id.cancel_tv, R.id.resubmit_tv)
            setOnItemClickListener(this@BillsRecordActivity)
            setOnItemChildClickListener(this@BillsRecordActivity)
        }
        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f),
                SizeUtils.dp2px(15f),
                0))
        }

    }

    override fun initData() {
        presenter = BillsRecordPresenter(this)
        onRefresh()
    }

    override fun bindListener() {

    }

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getData(page)
    }

    override fun onLoadMore() {
        page++
        presenter.getData(page)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        // 跳转至详情
        val billsRecord = mAdapter.data[position]
        startActivity(BillsOrderDetailActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ORDER_ID, billsRecord.id)
        })

    }

    override fun bindList(toMutableList: MutableList<BillsRecord>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }

        val loadMoreModule = mAdapter.loadMoreModule
        if (toMutableList.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
                emptyView.showEmptyDefault()
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

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        val billsRecord = mAdapter.data[position]
        val orderId = billsRecord.id

        when (view.id) {
            R.id.cancel_tv -> {
                ComplexRefundActivity.navigation(this,
                    orderId,
                    billsRecord.refund_price.toPlainString(),
                    billsRecord.total_price.toPlainString(),
                    ComplexRefundPresenter.TYPE_BILL)
            }
            R.id.resubmit_tv -> {
                presenter.recommit(orderId)
            }
            else -> {}
        }
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                onRefresh()
            }
        }

    }

    override fun recommitSuccess(orderId: Int) {
        startActivity(BillsRecommitResultActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ORDER_ID, orderId)
        })
    }


}