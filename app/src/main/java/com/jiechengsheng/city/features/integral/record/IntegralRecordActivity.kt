package com.jiechengsheng.city.features.integral.record

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.integral.IntegralRecord
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.integral.order.IntegralOrderDetailActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_refresh_list.*


/**
 * Created by Wangsw  2022/9/26 15:08.
 * 兑换记录
 */
class IntegralRecordActivity : BaseMvpActivity<IntegralRecordPresenter>(), IntegralRecordView, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {


    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: IntegralRecordAdapter
    private lateinit var emptyView: EmptyView

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refresh_list

    override fun initView() {
        mJcsTitle.setMiddleTitle(getString(R.string.exchange_record))
        initContent()
    }

    private fun initContent() {
        swipe_layout.setOnRefreshListener(this)
        emptyView = EmptyView(this)
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)


        mAdapter = IntegralRecordAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getData(page)
            }
            setOnItemClickListener(this@IntegralRecordActivity)
        }


        val gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
        }

    }

    override fun initData() {
        presenter = IntegralRecordPresenter(this)
        onRefresh()
    }

    override fun bindListener() = Unit

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getData(page)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val integralRecord = mAdapter.data[position]
        startActivity(IntegralOrderDetailActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ID, integralRecord.id)
        })

    }


    override fun bindData(toMutableList: MutableList<IntegralRecord>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }
        val loadMoreModule = mAdapter.loadMoreModule
        if (toMutableList.isEmpty()) {
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


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_INTEGRAL
            -> {
                onRefresh()
            }
            else -> {}
        }
    }

}


interface IntegralRecordView : BaseMvpView {
    fun bindData(toMutableList: MutableList<IntegralRecord>, lastPage: Boolean)

}


class IntegralRecordPresenter(private var view: IntegralRecordView) : BaseMvpPresenter(view) {

    fun getData(page: Int) {
        requestApi(mRetrofit.integralRecord(page), object : BaseMvpObserver<PageResponse<IntegralRecord>>(view) {
            override fun onSuccess(response: PageResponse<IntegralRecord>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }
        })
    }

}


