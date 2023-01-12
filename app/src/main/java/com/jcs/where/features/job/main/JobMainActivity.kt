package com.jcs.where.features.job.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.jcs.where.R
import com.jcs.where.api.response.home.TabEntity
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.job.collection.JobCollectionFragment
import com.jcs.where.features.job.cv.CvHomeFragment
import com.jcs.where.features.job.home.JobHomeFragment
import com.jcs.where.features.job.record.JobRecordFragment
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_job_main.*

/**
 * Created by Wangsw  2022/12/15 10:14.
 * 招聘页
 */
class JobMainActivity : BaseMvpActivity<JobMainPresenter>(), JobMainView {

    private val frList = ArrayList<Fragment>()
    private val tabs = ArrayList<CustomTabEntity>()
    private var isFromNotice = false

    override fun getLayoutId() = R.layout.activity_job_main

    override fun initView() {
        isFromNotice = intent.getBooleanExtra(Constant.PARAM_FROM_NOTICE, false)

        tabs.apply {
            add(TabEntity(getString(R.string.job_tab_home), R.mipmap.ic_job_home_selected, R.mipmap.ic_job_home_normal))
            add(TabEntity(getString(R.string.job_tab_collection),
                R.mipmap.ic_job_collection_selected,
                R.mipmap.ic_job_collection_normal))
            add(TabEntity(getString(R.string.job_tab_apply), R.mipmap.ic_job_send_selected, R.mipmap.ic_job_send_normal))
            add(TabEntity(getString(R.string.job_tab_mine), R.mipmap.ic_job_mine_selected, R.mipmap.ic_job_mine_normal))
        }

        frList.apply {
            add(JobHomeFragment())
            add(JobCollectionFragment())
            val record = JobRecordFragment()
            record.arguments = Bundle().apply {
                putBoolean(Constant.PARAM_FROM_NOTICE, isFromNotice)
            }

            add(record)
            add(CvHomeFragment())
        }

        tabs_navigator.setTabData(tabs, this, R.id.fl_content, frList)
        tabs_navigator.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {

                if (!User.isLogon()) {
                    tabs_navigator.currentTab = 0
                    startActivity(LoginActivity::class.java)
                }

            }

            override fun onTabReselect(position: Int) {
            }
        })
    }

    override fun initData() {
        presenter = JobMainPresenter(this)
        if (isFromNotice && User.isLogon()) {
            tabs_navigator.currentTab = 2
        }
    }

    override fun bindListener() {

    }
}