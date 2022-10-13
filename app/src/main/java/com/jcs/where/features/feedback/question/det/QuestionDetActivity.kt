package com.jcs.where.features.feedback.question.det

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.feedback.question.QuestionPresenter
import com.jcs.where.features.feedback.question.QuestionView

/**
 * Created by Wangsw  2022/10/13 10:50.
 * 问题详情
 */
class QuestionDetActivity  : BaseMvpActivity<QuestionPresenter>(), QuestionView{

    override fun getLayoutId() = R.layout.activity_feedback_question_detail

    override fun initView() {

    }

    override fun initData() {
        presenter = QuestionPresenter(this)
    }

    override fun bindListener() {

    }
}