package com.jcs.where.features.job.collection

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.job.Job
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.job.home.JobHomeAdapter
import com.jcs.where.features.job.home.JobHomePresenter
import com.jcs.where.features.job.home.JobHomeView
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_job_collection.*

/**
 * Created by Wangsw  2022/12/15 11:05.
 * 职位收藏
 */
class JobCollectionFragment : BaseMvpFragment<JobHomePresenter>(), JobHomeView, SwipeRefreshLayout.OnRefreshListener {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: JobHomeAdapter
    private lateinit var emptyView: EmptyView

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.fragment_job_collection
    override fun initView(view: View?) {
        initContent()
    }

    private fun initContent() {
        swipe_layout.setOnRefreshListener(this)
        swipe_layout.setColorSchemeColors(ColorUtils.getColor(R.color.color_1c1380))
        emptyView = EmptyView(requireContext())
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)

        mAdapter = JobHomeAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getJobCollectionList(page)
            }
        }


        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler.apply {
            adapter = mAdapter
            layoutManager = manager
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5), SizeUtils.dp2px(4f), 0, 0))
        }

    }

    override fun initData() {
        presenter = JobHomePresenter(this)

    }

    override fun loadOnVisible() {
        onRefresh()
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onRefresh() {
        if (!User.isLogon()) {
            return
        }
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getJobCollectionList(page)
    }

    override fun bindJobCollectionList(toMutableList: MutableList<Job>, lastPage: Boolean) {
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

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        when (baseEvent.code) {
            EventCode.EVENT_LOGIN_SUCCESS -> {
                if (isViewCreated) {
                    onRefresh()
                }
            }
            else -> {}
        }
    }


}