package com.jcs.where.features.job.cv

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.job.JobExperience
import com.jcs.where.api.response.job.ProfileDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.job.form.CvFormJobExperienceActivity
import com.jcs.where.features.job.form.CvFormProfileActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_job_cv_home.*

/**
 * Created by Wangsw  2022/9/28 15:25.
 * 简历首页
 */
class CvHomeActivity : BaseMvpActivity<CvHomePresenter>(), CvHomeView {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: JobExperienceAdapter
    private lateinit var emptyView: EmptyView

    /** 个人信息 */
    private var profileDetail: ProfileDetail? = null

    override fun getLayoutId() = R.layout.activity_job_cv_home

    override fun initView() {
        initContent()
    }

    private fun initContent() {
        emptyView = EmptyView(this)
        emptyView.showEmptyDefault()
        emptyView.setEmptyHint(R.string.empty_job_experience)
        addEmptyList(emptyView)


        mAdapter = JobExperienceAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener { _, _, position ->
                val jobExperience = mAdapter.data[position]
                startActivity(CvFormJobExperienceActivity::class.java, Bundle().apply {
                    putParcelable(Constant.PARAM_DATA, jobExperience)
                })
            }
        }


        val gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        content_rv.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.grey_F5F5F5),
                1,
                SizeUtils.dp2px(16f),
                SizeUtils.dp2px(16f)))
        }

    }

    override fun initData() {
        presenter = CvHomePresenter(this)
        presenter.getProfile()
        presenter.getJobExperience()
    }

    override fun bindListener() {
        create_cv_iv.setOnClickListener {
            // 处理个人信息
            startActivity(CvFormProfileActivity::class.java, Bundle().apply {
                putParcelable(Constant.PARAM_DATA, profileDetail)
            })
        }
        name_tv.setOnClickListener {
            create_cv_iv.performClick()
        }

        add_job_experience_rl.setOnClickListener {
            // 添加工作经历
            startActivity(CvFormJobExperienceActivity::class.java)
        }


    }

    override fun getProfile(response: ProfileDetail?) {
        if (response == null) {
            create_cv_rl.visibility = View.VISIBLE
            info_ll.visibility = View.GONE
            return
        }
        profileDetail = response

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

    override fun bindJobExperience(toMutableList: MutableList<JobExperience>) {
        mAdapter.setNewInstance(toMutableList)
        if (toMutableList.isEmpty()) {
            emptyView.showEmptyContainer()
        }
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_CV_PROFILE -> presenter.getProfile()
            EventCode.EVENT_REFRESH_CV_EXPERIENCE -> presenter.getJobExperience()
            else -> {}
        }


    }

}