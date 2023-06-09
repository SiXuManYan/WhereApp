package com.jiechengsheng.city.features.feedback.question

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.jiechengsheng.city.features.feedback.home.CategoryQuestionAdapter
import com.jiechengsheng.city.features.feedback.question.det.QuestionDetActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_feedback_question_and_search_result.*

/**
 * Created by Wangsw  2022/10/12 17:03.
 * 问题列表、搜索问题结果列表
 */
class QuestionActivity : BaseMvpActivity<QuestionPresenter>(), QuestionView, OnItemClickListener {


    private var categoryId = 0
    private var categoryName = ""

    /** 搜索内容 */
    private var searchName = ""
    private lateinit var mAdapter: CategoryQuestionAdapter
    private lateinit var emptyView: EmptyView


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_feedback_question_and_search_result

    companion object {

        fun navigation(context: Context, categoryId: Int, categoryName: String) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, categoryId)
                putString(Constant.PARAM_NAME, categoryName)
            }
            val intent = Intent(context, QuestionActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }


        fun navigation4Search(context: Context, searchName: String) {

            val bundle = Bundle().apply {
                putString(Constant.PARAM_SEARCH, searchName)
            }
            val intent = Intent(context, QuestionActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }


    }

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        initContent()
    }

    private fun initContent() {
        intent.extras?.let {
            categoryId = it.getInt(Constant.PARAM_ID, 0)
            categoryName = it.getString(Constant.PARAM_NAME, "")
            searchName = it.getString(Constant.PARAM_SEARCH, "")
        }

        emptyView = EmptyView(this)
        addEmptyList(emptyView)

        // 页面模式区分
        if (searchName.isBlank()) {
            // 问题列表
            title_rl.visibility = View.VISIBLE
            category_name_tv.text = categoryName
            emptyView.showEmptyDefault()
        } else {
            search_ll.visibility = View.VISIBLE
            search_content_tv.text = searchName
            emptyView.setEmptyImage(R.mipmap.ic_empty_search)
            emptyView.setEmptyMessage(R.string.empty_search_message)
            emptyView.setEmptyHint(R.string.empty_search_hint)
        }




        mAdapter = CategoryQuestionAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@QuestionActivity)
        }

        val gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), 1, SizeUtils.dp2px(15f), 0)
        recycler.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }


    }

    override fun initData() {
        presenter = QuestionPresenter(this)
        presenter.feedbackQuestion(categoryId, searchName)
    }

    override fun bindListener() {
        search_ll.setOnClickListener { finish() }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val item = mAdapter.data[position]
        startActivity(QuestionDetActivity::class.java, Bundle().apply {
            putString(Constant.PARAM_URL, item.website)
        })
    }

    override fun bindView(response: ArrayList<FeedbackCategoryAndQuestion>) {
        if (response.isNullOrEmpty()) {
            emptyView.showEmptyContainer()
        }
        mAdapter.setNewInstance(response)
    }

}