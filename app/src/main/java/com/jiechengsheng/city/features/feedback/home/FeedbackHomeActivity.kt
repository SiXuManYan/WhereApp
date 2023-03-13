package com.jiechengsheng.city.features.feedback.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.feedback.FeedbackCategoryAndQuestion
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.feedback.form.FeedBackPostActivity
import com.jiechengsheng.city.features.feedback.question.QuestionActivity
import com.jiechengsheng.city.features.feedback.record.FeedbackRecordActivity
import com.jiechengsheng.city.features.search.SearchAllActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_feed_back_home.*

/**
 * Created by Wangsw  2022/10/12 16:07.
 * 意见反馈首页（分类列表）
 */
class FeedbackHomeActivity : BaseMvpActivity<FeedbackHomePresenter>(), FeedbackHomeView, OnItemClickListener {

    private lateinit var mAdapter: CategoryQuestionAdapter
    private lateinit var emptyView: EmptyView


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_feed_back_home


    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        initContent()
    }

    private fun initContent() {
        emptyView = EmptyView(this)
        emptyView.showEmptyDefault()
        addEmptyList(emptyView)


        mAdapter = CategoryQuestionAdapter().apply {
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
        presenter.feedbackCategory()
    }

    override fun bindListener() {
        record_tv.setOnClickListener {
            startActivity(FeedbackRecordActivity::class.java)
        }

        search_ll.setOnClickListener {
            startActivity(SearchAllActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 10)
            })
        }
        bottom_rl.setOnClickListener {
            startActivity(FeedBackPostActivity::class.java)
        }


    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        val item = mAdapter.data[position]
        QuestionActivity.navigation(this,item.id,item.name)
    }

    override fun bindView(response: ArrayList<FeedbackCategoryAndQuestion>) {
        if (response.isNullOrEmpty()) {
            emptyView.showEmptyContainer()
        }
        mAdapter.setNewInstance(response)
    }

}
