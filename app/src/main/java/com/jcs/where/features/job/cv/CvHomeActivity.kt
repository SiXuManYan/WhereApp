package com.jcs.where.features.job.cv

import android.view.View
import com.jcs.where.R
import com.jcs.where.api.response.job.ProfileDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_job_cv_home.*

/**
 * Created by Wangsw  2022/9/28 15:25.
 * 简历首页
 */
class CvHomeActivity : BaseMvpActivity<CvHomePresenter>(), CvHomeView {

    override fun getLayoutId() = R.layout.activity_job_cv_home

    override fun initView() {

    }

    override fun initData() {
        presenter = CvHomePresenter(this)
        presenter.getData()
    }

    override fun bindListener() {
        create_cv_iv.setOnClickListener {
            // 创建编辑
        }
        name_tv.setOnClickListener {
            // 简历编辑
        }


    }

    override fun bindData(response: ProfileDetail?) {
        if (response == null) {

            create_cv_rl.visibility = View.VISIBLE
            info_ll.visibility = View.GONE
            return
        }
        create_cv_rl.visibility = View.GONE
        info_ll.visibility = View.VISIBLE

        (response.first_name + response.last_name).also { name_tv.text = it }

        gender_tv.text = when (response.gender) {
            1 -> getString(R.string.male)
            2 -> getString(R.string.female)
            else -> ""
        }
        city_tv.text = response.city
        email_tv.text = response.email
        school_tv.text = response.school
        major_tv.text = response.major
        education_tv.text = response.education



    }


}