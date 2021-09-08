package com.jcs.where.features.map.child

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.map.MechanismAdapter
import com.jcs.where.features.map.government.GovernmentActivity
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.single_recycler_view.*

/**
 * Created by Wangsw  2021/8/26 14:27.
 * 机构列表
 */
class MechanismChildFragment : BaseMvpFragment<MechanismChildPresenter>(), MechanismChildView, OnItemClickListener,
    OnLoadMoreListener {

    private lateinit var mAdapter: MechanismAdapter
    private lateinit var emptyView: EmptyView
    var categoryId = ""
    var search = ""
    private var page = Constant.DEFAULT_FIRST_PAGE
    public var totalCount = 0
    public var totalCountView: TextView? = null


    override fun getLayoutId() = R.layout.single_recycler_view

    override fun initView(view: View?) {

        emptyView = EmptyView(context).apply {
            initEmpty(
                R.mipmap.ic_empty_search, R.string.empty_search,
                R.string.empty_search_hint, R.string.back
            ) {

            }
            action_tv.visibility = View.GONE
        }
        mAdapter = MechanismAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@MechanismChildFragment)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener(this@MechanismChildFragment)
        }

        content_rv.adapter = mAdapter
        content_rv.addItemDecoration(
            DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0).apply {
                setDrawHeaderFooter(true)
            })

    }

    override fun initData() {
        presenter = MechanismChildPresenter(this)
    }

    override fun loadOnVisible() {
        if (isViewVisible) {
            presenter.getData(page, categoryId, search)
        }
    }

    override fun onLoadMore() {
        page++
        loadOnVisible()
    }

    override fun bindListener() = Unit

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        startActivity(MechanismActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ID, data.id)
        })
    }

    override fun bindData(response: MutableList<MechanismResponse>, isLastPage: Boolean, total: Int) {

        totalCount = total
        updateActivityCount()

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
            if (isLastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }

        when (baseEvent.code) {

            EventCode.EVENT_REFRESH_MECHANISM -> {
                page = Constant.DEFAULT_FIRST_PAGE
                categoryId = baseEvent.message
                loadOnVisible()
            }

            EventCode.EVENT_REFRESH_CHILD -> {
                page = Constant.DEFAULT_FIRST_PAGE
                search = baseEvent.message
                loadOnVisible()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            updateActivityCount()
        }
    }

    private fun updateActivityCount(){
        val governmentActivity = activity as GovernmentActivity
        governmentActivity.getTotalCountView().text =  getString(R.string.total_list_count_format, totalCount)
    }




}