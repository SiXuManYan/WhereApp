package com.jcs.where.features.bills.hydropower.record

import android.graphics.Color
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.hydropower.PaymentRecord
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_refresh_list.*

/**
 * Created by Wangsw  2021/6/28 16:16.
 * 水电缴费记录
 */
class PaymentRecordActivity : BaseMvpActivity<PaymentRecordPresenter>(), PaymentRecordView, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, OnItemClickListener {


    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: PaymentAdapter
    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.activity_refresh_list

    override fun isStatusDark(): Boolean = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        mJcsTitle.setMiddleTitle(getString(R.string.payment_record))
        container_ll.setBackgroundColor(ColorUtils.getColor(R.color.grey_F5F5F5))

        swipe_layout.setOnRefreshListener(this@PaymentRecordActivity)

        emptyView = EmptyView(this).apply {
            showEmptyNothing()
        }

        mAdapter = PaymentAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener(this@PaymentRecordActivity)
            setOnItemClickListener(this@PaymentRecordActivity)
        }

        recycler.apply {
            adapter = mAdapter
        }



    }

    override fun initData() {
        presenter = PaymentRecordPresenter(this)
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


    override fun bindList(toMutableList: MutableList<PaymentRecord>, lastPage: Boolean) {
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

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }
}