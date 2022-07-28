package com.jcs.where.features.travel.map.child

import android.graphics.Color
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.travel.TravelChild
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.travel.detail.TravelDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.single_recycler_view.*

/**
 * Created by Wangsw  2021/10/18 9:53.
 * 旅游地图子列表
 */
class TravelChildFragment : BaseMvpFragment<TravelChildPresenter>(), TravelChildView, OnLoadMoreListener, OnItemClickListener {

    var categoryId = 0

    var searchInput: String? = null
    private var page = Constant.DEFAULT_FIRST_PAGE

    /** 搜索改变时，延时刷新 */
    private var needRefresh = false

    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: TravelChildAdapter


    override fun getLayoutId() = R.layout.single_recycler_view

    override fun initView(view: View) {
        emptyView = EmptyView(context).apply {
            initEmpty(
                R.mipmap.ic_empty_search, R.string.empty_search,
                R.string.empty_search_hint, R.string.back
            ) {

            }
            action_tv.visibility = View.GONE
        }
        mAdapter = TravelChildAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@TravelChildFragment)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener(this@TravelChildFragment)
        }

        content_rv.adapter = mAdapter
        content_rv.addItemDecoration(
            DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0).apply {
                setDrawHeaderFooter(true)
            })

    }


    override fun loadOnVisible() {
        if (isViewVisible) {
            presenter.getData(page, categoryId, searchInput)
        }
    }


    override fun initData() {
        presenter = TravelChildPresenter(this)
    }

    override fun bindListener() {

    }

    override fun onLoadMore() {
        page++
        loadOnVisible()
    }


    override fun bindData(response: MutableList<TravelChild>, lastPage: Boolean) {
        val loadMoreModule = mAdapter.loadMoreModule
        if (response.isEmpty()) {
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
            mAdapter.setNewInstance(response)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()

        } else {
            mAdapter.addData(response)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        TravelDetailActivity.navigation(requireContext(),data.id)
    }



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser && needRefresh) {
            loadOnVisible()
            needRefresh = false
        }

    }

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }

        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_CHILD -> {
                searchInput = baseEvent.message
                page = Constant.DEFAULT_FIRST_PAGE
                if (isViewVisible) {
                    loadOnVisible()
                } else {
                    needRefresh = true
                }
            }
            // 更新


        }
    }


}