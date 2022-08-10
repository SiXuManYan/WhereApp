package com.jcs.where.features.merchant.record

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.merchant.MerchantApplyRecord
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_refresh_list.*

/**
 * Created by Wangsw  2022/1/11 15:02.
 * 申请记录
 */
class MerchantRecordActivity :BaseMvpActivity<MerchantRecordPresenter>(),MerchantRecordView, SwipeRefreshLayout.OnRefreshListener {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: MerchantRecordAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refresh_list

    override fun initView() {
        mJcsTitle.setMiddleTitle(getString(R.string.apply_record))
        swipe_layout.apply {
            setOnRefreshListener(this@MerchantRecordActivity)
            setColorSchemeColors(ColorUtils.getColor(R.color.blue_4C9EF2))
        }
        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            addEmptyList(this)
        }
        mAdapter = MerchantRecordAdapter().apply {
            setEmptyView(emptyView)
        }
        recycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun initData() {
        presenter = MerchantRecordPresenter(this)

        onRefresh()
    }

    override fun bindListener() {


    }

    override fun bindData(data: MutableList<MerchantApplyRecord>, lastPage: Boolean) {
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

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getData(page)
    }

}


interface MerchantRecordView:BaseMvpView{
    fun bindData(toMutableList: MutableList<MerchantApplyRecord>, lastPage: Boolean)

}

class MerchantRecordPresenter(private var view:MerchantRecordView):BaseMvpPresenter(view){

    fun getData(page: Int) {
        requestApi(mRetrofit.merchantApplyRecord(page), object : BaseMvpObserver<PageResponse<MerchantApplyRecord>>(view,page){
            override fun onSuccess(response: PageResponse<MerchantApplyRecord>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }
        })
    }
}