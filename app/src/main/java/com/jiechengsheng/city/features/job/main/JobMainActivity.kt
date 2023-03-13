package com.jiechengsheng.city.features.job.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.home.TabEntity
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.features.job.collection.JobCollectionFragment
import com.jiechengsheng.city.features.job.cv.CvHomeFragment
import com.jiechengsheng.city.features.job.home.JobHomeFragment
import com.jiechengsheng.city.features.job.record.JobRecordFragment
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.Constant
import kotlinx.android.synthetic.main.activity_job_main.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/12/15 10:14.
 * 招聘页
 */
class JobMainActivity : BaseMvpActivity<JobMainPresenter>(), JobMainView {

    private val frList = ArrayList<Fragment>()
    private val tabs = ArrayList<CustomTabEntity>()
    private var isFromNotice = false

    override fun getLayoutId() = R.layout.activity_job_main


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val bundle = intent.extras
        if (bundle != null) {
            // 简历状态更新，跳转至第三个tab
            val isFromNotice = bundle.getBoolean(Constant.PARAM_FROM_NOTICE, false)
            tabs_navigator.currentTab = 2
            EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_NAVIGATION_TO_JOB_INTERVIEWS))
        }
    }

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