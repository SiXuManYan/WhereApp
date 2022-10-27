package com.jcs.where.features.job.report

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity

/**
 * Created by Wangsw  2022/10/27 17:38.
 * 职位举报
 */
class ReportActivity :BaseMvpActivity<ReportPresenter>(),ReportView{

    override fun getLayoutId() = R.layout.activity_report

    override fun initView() {

    }

    override fun initData() {
       presenter = ReportPresenter(this)
    }

    override fun bindListener() {

    }


}