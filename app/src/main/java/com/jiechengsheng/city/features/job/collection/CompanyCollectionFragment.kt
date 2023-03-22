package com.jiechengsheng.city.features.job.collection

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.job.CompanyInfo
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.features.job.company.CompanyActivity
import com.jiechengsheng.city.features.job.home.JobHomePresenter
import com.jiechengsheng.city.features.job.home.JobHomeView
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2023/3/21 15:06.
 * 公司收藏
 */
class CompanyCollectionFragment : BaseMvpFragment<JobHomePresenter>(), JobHomeView, SwipeRefreshLayout.OnRefreshListener,
    OnItemClickListener {


    private lateinit var mAdapter: CompanyAdapter
    private lateinit var emptyView: EmptyView
    private var page = Constant.DEFAULT_FIRST_PAGE


    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View?) {
        swipe_layout.setOnRefreshListener(this)
        swipe_layout.setColorSchemeColors(ColorUtils.getColor(R.color.color_1c1380))

        emptyView = EmptyView(requireContext()).apply {
            setEmptyImage(R.mipmap.ic_empty_job_collection)
            setEmptyHint(R.string.empty_data_default)
            addEmptyList(this)
        }

        mAdapter = CompanyAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@CompanyCollectionFragment)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getCompanyCollectionList(page)
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

    override fun bindListener() = Unit

    override fun loadOnVisible() = onRefresh()

    override fun onRefresh() {
        if (!User.isLogon()) {
            return
        }
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getCompanyCollectionList(page)
    }


    override fun bindCompanyCollectionList(toMutableList: MutableList<CompanyInfo>, lastPage: Boolean) {
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

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val companyInfo = mAdapter.data[position]
        CompanyActivity.navigation(requireContext(), companyInfo.company_id)
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_COLLECTION,
            EventCode.EVENT_LOGIN_SUCCESS,
            -> {
                if (isViewCreated) {
                    onRefresh()
                }
            }
            else -> {}
        }

    }

}