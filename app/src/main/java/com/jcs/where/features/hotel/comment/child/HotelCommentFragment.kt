package com.jcs.where.features.hotel.comment.child

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelComment
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2021/8/17 10:58.
 * 酒店评论列表
 */
class HotelCommentFragment : BaseMvpFragment<HotelCommentPresenter>(), HotelCommentView, SwipeRefreshLayout.OnRefreshListener,
    OnLoadMoreListener {


    /** 酒店id */
    private var hotel_id = 0


    /**
     * 显示类型（1：晒图，2：低分，3：最新）
     */
    private var listType = 0

    private lateinit var mAdapter: HotelCommentAdapter

    private lateinit var emptyView: EmptyView
    private var page = Constant.DEFAULT_FIRST_PAGE


    companion object {

        /**
         * @param hotel_id 酒店ID
         * @param listType 显示类型（1：晒图，2：低分，3：最新）
         */
        fun newInstance(hotel_id: Int, listType: Int? = 0): HotelCommentFragment {
            val fragment = HotelCommentFragment()
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_SHOP_ID, hotel_id)
                listType?.let {
                    putInt(Constant.PARAM_TYPE, listType)
                }
            }
            fragment.arguments = bundle
            return fragment
        }

    }


    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View) {
        arguments?.let {
            hotel_id = it.getInt(Constant.PARAM_SHOP_ID, 0)
            listType = it.getInt(Constant.PARAM_ACCOUNT, 1)
        }

        swipe_layout.setOnRefreshListener(this)
        emptyView = EmptyView(context).apply {
            showEmptyDefault()
        }

        mAdapter = HotelCommentAdapter().apply {
            footerWithEmptyEnable = true
            headerWithEmptyEnable = true
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener(this@HotelCommentFragment)
            setEmptyView(emptyView)
        }
        recycler.adapter = mAdapter


    }

    override fun initData() {

        presenter = HotelCommentPresenter(this)
    }

    override fun bindListener() {

    }

    override fun onRefresh() {
        swipe_layout.isRefreshing = true
        page = Constant.DEFAULT_FIRST_PAGE
        loadOnVisible()
    }

    override fun onLoadMore() {
        page++
        loadOnVisible()
    }

    override fun loadOnVisible() {
        presenter.getStoreCommentList(hotel_id, page, listType)
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