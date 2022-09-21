package com.jcs.where.features.integral.activitys.child

import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.integral.IntegralGood
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2022/9/21 14:40.
 *
 */
class IntegralChildFragment : BaseMvpFragment<IntegralChildPresenter>(), IntegralChildView, SwipeRefreshLayout.OnRefreshListener,
    OnItemClickListener {

    var type = 0

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: IntegralGoodAdapter
    private lateinit var emptyView: EmptyView


    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View) {
        initContent()
    }

    override fun initData() {
        presenter = IntegralChildPresenter(this)
    }

    private fun initContent() {
        swipe_layout.setOnRefreshListener(this)

        emptyView = EmptyView(requireContext())
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)

        mAdapter = IntegralGoodAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getData(page, type)
            }
            setOnItemClickListener(this@IntegralChildFragment)
        }

        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(16f), 0, 0)
        recycler.apply {
            setPadding(SizeUtils.dp2px(8f), 0, SizeUtils.dp2px(8f), 0)
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }

    }


    override fun bindListener() = Unit

    override fun loadOnVisible() = onRefresh()

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getData(page, type)
    }

    override fun bindData(toMutableList: MutableList<IntegralGood>, lastPage: Boolean) {
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

    }


}