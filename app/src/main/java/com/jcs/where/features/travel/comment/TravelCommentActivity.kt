package com.jcs.where.features.travel.comment

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.hotel.HotelComment
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.hotel.comment.child.HotelCommentAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_travel_comment.*
import kotlinx.android.synthetic.main.activity_travel_comment.recycler
import kotlinx.android.synthetic.main.activity_travel_comment.swipe_layout
import kotlinx.android.synthetic.main.fragment_refresh_list.*



/**
 * Created by Wangsw  2021/10/19 14:35.
 * 旅游评论
 */
class TravelCommentActivity : BaseMvpActivity<TravelCommentPresenter>(), TravelCommentView {

    private var travelId = 0
    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var mAdapter: HotelCommentAdapter
    private lateinit var emptyView: EmptyView


    override fun getLayoutId() = R.layout.activity_travel_comment

    override fun isStatusDark() = true

    override fun initView() {

        val bundle = intent.extras
        bundle?.let {
            travelId = it.getInt(Constant.PARAM_ID, 0)
        }

        swipe_layout.setOnRefreshListener(this)
        emptyView = EmptyView(this).apply {
            showEmptyDefault()
        }

        mAdapter = HotelCommentAdapter().apply {
            footerWithEmptyEnable = true
            headerWithEmptyEnable = true
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener(this@TravelCommentActivity)
            setEmptyView(emptyView)
        }
        recycler.adapter = mAdapter

    }

    override fun initData() {
        presenter = TravelCommentPresenter(this)
        presenter.getDetail(page, travelId)
    }

    override fun bindListener() = Unit


    override fun onRefresh() {
        swipe_layout.isRefreshing = true
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getDetail(page, travelId)
    }

    override fun onLoadMore() {
        page++
        presenter.getDetail(page, travelId)
    }

    override fun bindData(data: MutableList<HotelComment>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }
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


interface TravelCommentView : BaseMvpView,SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    fun bindData(data: MutableList<HotelComment>, lastPage: Boolean)

}

class TravelCommentPresenter(private var view: TravelCommentView) : BaseMvpPresenter(view) {
    fun getDetail(page: Int, travelId: Int) {
        requestApi(mRetrofit.travelComment(page, travelId), object : BaseMvpObserver<PageResponse<HotelComment>>(view) {

            override fun onSuccess(response: PageResponse<HotelComment>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindData(data.toMutableList(), isLastPage)
            }

        })
    }

}



