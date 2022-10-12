package com.jcs.where.features.feedback.home

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.feedback.FeedbackCategory
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_feed_back_home.*

/**
 * Created by Wangsw  2022/10/12 16:07.
 * 意见反馈首页（分类列表）
 */
class FeedbackHomeActivity : BaseMvpActivity<FeedbackHomePresenter>(), FeedbackHomeView, OnItemClickListener {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: FeedbackCategoryAdapter
    private lateinit var emptyView: EmptyView


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_feed_back_home

    override fun initView() {
        initContent()
    }

    private fun initContent() {
        emptyView = EmptyView(this)
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)


        mAdapter = FeedbackCategoryAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@FeedbackHomeActivity)
        }

        val gridLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), 1, SizeUtils.dp2px(15f), 0)
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }
    }

    override fun initData() {
        presenter = FeedbackHomePresenter(this)
        presenter.feedBackCatgegory()
    }

    override fun bindListener() {
        record_tv.setOnClickListener {

        }
        bottom_rl.setOnClickListener {

        }


    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    override fun bindView(response: ArrayList<FeedbackCategory>) {
        mAdapter.setNewInstance(response)
    }

}
