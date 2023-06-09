package com.jiechengsheng.city.features.store.detail.comment.chiild

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.hotel.HotelComment
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.features.hotel.comment.child.HotelCommentAdapter
import com.jiechengsheng.city.features.store.detail.comment.StoreCommentActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2021/7/16 15:24.
 * 商城评论
 */
class StoreCommentChildFragment : BaseMvpFragment<StoreCommentPresenter>(), StoreCommentView, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {


    /** 商家id */
    private var shop_id = 0

    /**
     * 是否展示全部
     */
    private var showAll = true

    /**
     * 类型（1-全部，2-最新，3-有图）
     */
    private var listType = 1

    private lateinit var mAdapter: HotelCommentAdapter
    private lateinit var emptyView: EmptyView
    private var page = Constant.DEFAULT_FIRST_PAGE
    private var headerView: View? = null


    companion object {

        /**
         * 美食评论
         * @param shop_id 商家ID
         * @param showAll 是否展示全部
         * @param listType 类型（1-全部，2-最新，3-有图）
         */
        fun newInstance(shop_id: Int, showAll: Boolean? = true, listType: Int? = 1): StoreCommentChildFragment {
            val fragment = StoreCommentChildFragment()
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_SHOP_ID, shop_id)
                showAll?.let {
                    putBoolean(Constant.PARAM_ACCOUNT, it)
                }
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
            shop_id = it.getInt(Constant.PARAM_SHOP_ID, 0)
            showAll = it.getBoolean(Constant.PARAM_ACCOUNT, true)
            listType = it.getInt(Constant.PARAM_ACCOUNT, 1)
        }


        // list
        if (showAll) {
            swipe_layout.setOnRefreshListener(this)
        } else {
            swipe_layout.isEnabled = false
        }

        emptyView = EmptyView(context).apply {
            showEmptyDefault()
            addEmptyList(this)
        }


        headerView = LayoutInflater.from(context).inflate(R.layout.layout_store_comment_header, null)
        val footerView = LayoutInflater.from(context).inflate(R.layout.layout_store_comment_footer, null)

        footerView.setOnClickListener {
            // 详情页
            startActivity(StoreCommentActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_SHOP_ID, shop_id)
            })
        }

        headerView?.setOnClickListener {
            footerView.performClick()
        }


        mAdapter = HotelCommentAdapter().apply {
            footerWithEmptyEnable = true
            headerWithEmptyEnable = true
            if (showAll) {
                loadMoreModule.isAutoLoadMore = true
                loadMoreModule.isEnableLoadMoreIfNotFullPage = false
                loadMoreModule.setOnLoadMoreListener(this@StoreCommentChildFragment)
            } else {
                setHeaderView(headerView!!)
                setFooterView(footerView)
            }
            setEmptyView(emptyView)
        }

        recycler.apply {
            adapter = mAdapter
        }

    }

    override fun initData() {
        presenter = StoreCommentPresenter(this)
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
        presenter.getStoreCommentList(shop_id, page, showAll, listType)
    }


    override fun bindCommentData(data: MutableList<HotelComment>, isLastPage: Boolean, total: Int) {
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
            if (isLastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }

        val header_count_tv = headerView?.findViewById<TextView>(R.id.header_count_tv)
        header_count_tv?.text = getString(R.string.comment_format, total)

    }

}