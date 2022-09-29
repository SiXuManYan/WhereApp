package com.jcs.where.features.job.form

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity

/**
 * Created by Wangsw  2022/9/29 14:58.
 * 简历-个人信息表单
 */
class CvFormProfileActivity :BaseMvpActivity<CvFormPresenter>(),CvFormView{

    override fun getLayoutId() = R.layout.activity_job_cv_profile

    override fun initView() {

    }

    override fun initData() {
        presenter = CvFormPresenter(this)
    }

    override fun bindListener() {

    }


}