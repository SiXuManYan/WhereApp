package com.jcs.where.features.job.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.job.Job
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_job_home.*

/**
 * Created by Wangsw  2022/9/27 16:03.
 * 招聘首页
 */
class JobHomeActivity : BaseMvpActivity<JobHomePresenter>(), JobHomeView, SwipeRefreshLayout.OnRefreshListener {


    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: JobHomeAdapter
    private lateinit var emptyView: EmptyView

    private var search: String? = null

    override fun getLayoutId() = R.layout.activity_job_home

    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.color_1c1380))
        initContent()
    }

    private fun initContent() {

        swipe_layout.setOnRefreshListener(this)
        emptyView = EmptyView(this)
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)


        mAdapter = JobHomeAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getData(page, search)
            }
        }


        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = manager
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5),
                SizeUtils.dp2px(4f),
                0,
                0))
        }

    }

    override fun initData() {
        presenter = JobHomePresenter(this)
        onRefresh()
    }

    override fun bindListener() {
        search_iv.setOnClickListener {

            startActivity(SearchAllActivity::class.java , Bundle().apply {
                putInt(Constant.PARAM_TYPE , 9)
                putBoolean(Constant.PARAM_HIDE , true)
            })
        }
    }


    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getData(page, search)
    }

    override fun bindData(toMutableList: MutableList<Job>, lastPage: Boolean) {
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

}