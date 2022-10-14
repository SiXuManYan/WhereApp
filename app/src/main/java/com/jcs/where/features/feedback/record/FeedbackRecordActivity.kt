package com.jcs.where.features.feedback.record

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.feedback.FeedbackRecord
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.feedback.form.FeedBackPostPresenter
import com.jcs.where.features.feedback.form.FeedBackPostView
import com.jcs.where.features.feedback.record.det.FeedbackDetActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
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
        val layoutParams = recycler.layoutParams
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        recycler.layoutParams = layoutParams
        recycler.setBackgroundColor(Color.WHITE)
        container_ll.setBackgroundColor(ColorUtils.getColor(R.color.colorPrimary))


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
//                emptyView.showEmptyContainer()
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