package com.jcs.where.features.gourmet.comment

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.comment.CommentResponse
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2021/5/27 16:05.
 *  美食、商城评论列表
 */
class FoodCommentFragment : BaseMvpFragment<FoodCommentPresenter>(), FoodCommentView, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    /** 餐厅id */
    private var restaurant_id: String = ""

    /** 商家id */
    private var shop_id: String = ""

    private var listType = 0

    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var mAdapter: FoodCommentAdapter
    private lateinit var emptyView: EmptyView

    companion object {

        /**
         * 美食评论
         * @param restaurantId 餐厅id
         * @param listType  列表类型（0：全部，1：有图，2：好评，3：差评）
         */
        fun newInstance(restaurantId: String, listType: Int): FoodCommentFragment {
            val fragment = FoodCommentFragment()
            val bundle = Bundle().apply {
                putString(Constant.PARAM_ID, restaurantId)
                putInt(Constant.PARAM_TYPE, listType)
            }
            fragment.arguments = bundle
            return fragment
        }

        /**
         * 美食评论
         * @param shop_id 商家ID
         */
        fun newInstance(shop_id: String): FoodCommentFragment {
            val fragment = FoodCommentFragment()
            val bundle = Bundle().apply {
                putString(Constant.PARAM_SHOP_ID, shop_id)
            }
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View) {
        arguments?.let {
            restaurant_id = it.getString(Constant.PARAM_ID, "")
            shop_id = it.getString(Constant.PARAM_SHOP_ID, "")
            listType = it.getInt(Constant.PARAM_TYPE, 0)
        }

        // list
        swipe_layout.setOnRefreshListener(this)
        if (shop_id.isNotEmpty()) {
            swipe_layout.isEnabled = false
        }
        emptyView = EmptyView(context).apply {
            showEmptyNothing()
        }
        mAdapter = FoodCommentAdapter().apply {
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            loadMoreModule.setOnLoadMoreListener(this@FoodCommentFragment)
            setEmptyView(emptyView)
        }
        recycler.adapter = mAdapter
    }

    override fun initData() {
        presenter = FoodCommentPresenter(this)
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
        if (restaurant_id.isNotEmpty()) {
            presenter.getFoodCommentList(restaurant_id, page, listType)
        }
        if (shop_id.isNotEmpty()) {
            presenter.getStoreCommentList(shop_id, page)
        }


    }

    override fun bindCommentData(data: MutableList<CommentResponse>, isLastPage: Boolean) {
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
            if (isLastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

}