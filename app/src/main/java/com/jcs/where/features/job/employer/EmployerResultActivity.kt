package com.jcs.where.features.job.employer

import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import kotlinx.android.synthetic.main.activity_employer_result.*

/**
 * Created by Wangsw  2022/10/27 15:30.
 *  提交成功
 */
class EmployerResultActivity :BaseActivity() {

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_employer_result

    override fun initView() {

    }

    override fun initData() = Unit

    override fun bindListener() {
        finish_tv.setOnClickListener {
            finish()
        }


    }
}