package com.jiechengsheng.city.features.travel.comment

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.hotel.HotelComment
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.hotel.comment.child.HotelCommentAdapter
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_travel_comment.recycler
import kotlinx.android.synthetic.main.activity_travel_comment.swipe_layout

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
            addEmptyList(this)
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


}


interface TravelCommentView : BaseMvpView, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    fun bindData(data: MutableList<HotelComment>, lastPage: Boolean)

}

class TravelCommentPresenter(private var view: TravelCommentView) : BaseMvpPresenter(view) {
    fun getDetail(page: Int, travelId: Int) {
        requestApi(mRetrofit.travelComment(travelId, page), object : BaseMvpObserver<PageResponse<HotelComment>>(view,page) {

            override fun onSuccess(response: PageResponse<HotelComment>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindData(data.toMutableList(), isLastPage)
            }

        })
    }

}



