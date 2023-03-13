package com.jiechengsheng.city.features.feedback.record

import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.feedback.FeedbackRecord
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.feedback.form.FeedBackPostPresenter
import com.jiechengsheng.city.features.feedback.form.FeedBackPostView
import com.jiechengsheng.city.features.feedback.record.det.FeedbackDetActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_refresh_list.*


/**
 * Created by Wangsw  2022/10/14 14:16.
 * 反馈记录
 */
class FeedbackRecordActivity : BaseMvpActivity<FeedBackPostPresenter>(), FeedBackPostView {


    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: FeedbackRecordAdapter
    private lateinit var emptyView: EmptyView
    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refresh_list

    override fun initView() {
        mJcsTitle.setMiddleTitle(R.string.feedback_record)

        initList()

    }

    private fun initList() {
        emptyView = EmptyView(this)
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)

        mAdapter = FeedbackRecordAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getRecord(page)
            }
            setOnItemClickListener { _, _, position ->
                val item = mAdapter.data[position]
                FeedbackDetActivity.navigation(this@FeedbackRecordActivity, item.image, item.content, item.tel)
            }
        }


        val gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), 1, SizeUtils.dp2px(8f), 0)
        recycler.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }
    }

    override fun initData() {
        presenter = FeedBackPostPresenter(this)
        presenter.getRecord(page)
    }

    override fun bindListener() {
        swipe_layout.setOnRefreshListener {
            page = Constant.DEFAULT_FIRST_PAGE
            presenter.getRecord(page)
        }
    }

    override fun bindRecord(toMutableList: MutableList<FeedbackRecord>, lastPage: Boolean) {
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