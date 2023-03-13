package com.jiechengsheng.city.features.job.result

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.job.Job
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.job.home.JobHomeAdapter
import com.jiechengsheng.city.features.job.home.JobHomePresenter
import com.jiechengsheng.city.features.job.home.JobHomeView
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_job_search_result.*

/**
 * Created by Wangsw  2022/9/28 10:08.
 * 职位搜素结果
 */
class JobSearchResultActivity : BaseMvpActivity<JobHomePresenter>(), JobHomeView, SwipeRefreshLayout.OnRefreshListener {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: JobHomeAdapter
    private lateinit var emptyView: EmptyView

    private var search: String? = null

    override fun isStatusDark() = true


    override fun getLayoutId() = R.layout.activity_job_search_result

    override fun initView() {
        search = intent.getStringExtra(Constant.PARAM_NAME)
        search_tv.text = search

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
                presenter.getJobList(page, search)
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
        search_ll.setOnClickListener { finish() }
    }


    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getJobList(page, search)
    }

    override fun bindJobList(toMutableList: MutableList<Job>, lastPage: Boolean) {
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

    override fun bindJobCollectionList(toMutableList: MutableList<Job>, lastPage: Boolean) {

    }

}