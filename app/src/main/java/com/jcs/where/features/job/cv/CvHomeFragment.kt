package com.jcs.where.features.job.cv

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.job.JobExperience
import com.jcs.where.api.response.job.ProfileDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.job.form.certificate.CertificateFromActivity
import com.jcs.where.features.job.form.edu.CvFormEduActivity
import com.jcs.where.features.job.form.experience.CvFormJobExperienceActivity
import com.jcs.where.features.job.form.profile.CvFormProfileActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_job_cv_home.*

/**
 * Created by Wangsw  2022/12/15 11:18.
 * 个人简历
 */
class CvHomeFragment : BaseMvpFragment<CvHomePresenter>(), CvHomeView, OnItemClickListener {

    private lateinit var mAdapter: JobExperienceEduAdapter

    /** 个人信息 */
    private var profileDetail: ProfileDetail? = null

    override fun getLayoutId() = R.layout.fragment_job_cv_home

    override fun initView(view: View?) {
        initContent()
    }

    private fun initContent() {

        mAdapter = JobExperienceEduAdapter().apply {
            setOnItemClickListener(this@CvHomeFragment)
        }

        val gridLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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
    }

    override fun loadOnVisible() {
        if (!User.isLogon()) {
            return
        }
        presenter.getProfile()
        presenter.getJobExperience()
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            activity?.finish()
        }
        create_cv_iv.setOnClickListener {
            startActivityAfterLogin(CvFormProfileActivity::class.java, Bundle().apply {
                putParcelable(Constant.PARAM_DATA, profileDetail)
            })
        }
        name_tv.setOnClickListener {
            create_cv_iv.performClick()
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

        (response.first_name + " " + response.last_name).also { name_tv.text = it }

        gender_tv.text = when (response.gender) {
            1 -> getString(R.string.male)
            2 -> getString(R.string.female)
            else -> ""
        }
        city_tv.text = response.city
        email_tv.text = response.email

    }

    override fun bindJobExperience(toMutableList: MutableList<JobExperience>) {
        mAdapter.setNewInstance(toMutableList)
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val item = mAdapter.data[position]

        when (adapter.getItemViewType(position)) {

            EventCode.EVENT_LOGIN_SUCCESS -> {
                if (isViewCreated) {
                    loadOnVisible()
                }
            }

            JobExperience.TYPE_TITLE -> {
                val titleType = item.nativeTitleType

                // 添加工作经历
                if (titleType == JobExperience.TYPE_JOB_EXPERIENCE) {
                    startActivity(CvFormJobExperienceActivity::class.java)
                }

                // 添加教育背景
                if (titleType == JobExperience.TYPE_EDU_BACKGROUND) {
                    startActivity(CvFormEduActivity::class.java)
                }

                // 添加资格证书
                if (titleType == JobExperience.TYPE_CERTIFICATION) {
                    startActivity(CertificateFromActivity::class.java)
                }
            }
            JobExperience.TYPE_JOB_EXPERIENCE -> {
                startActivity(CvFormJobExperienceActivity::class.java, Bundle().apply {
                    putParcelable(Constant.PARAM_DATA, item)
                })
            }
            JobExperience.TYPE_EDU_BACKGROUND -> {
                startActivity(CvFormEduActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ID, item.id)
                })
            }
            JobExperience.TYPE_CERTIFICATION -> {
                CertificateFromActivity.navigation(requireContext(), item)
            }

            else -> {}
        }


    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_CV_PROFILE ->
                presenter.getProfile()

            EventCode.EVENT_REFRESH_CV_EXPERIENCE,
            EventCode.EVENT_REFRESH_CV_EDU,
            EventCode.EVENT_REFRESH_CV_CERTIFICATE,
            -> presenter.getJobExperience()

            EventCode.EVENT_DELETE_CV_EXPERIENCE -> {
                // 删除工作经历
                val draftExperienceId = baseEvent.data as Int
                deleteTypeItem(draftExperienceId, JobExperience.TYPE_JOB_EXPERIENCE)
            }
            EventCode.EVENT_DELETE_CV_EDU -> {
                // 删除教育背景
                val draftEduId = baseEvent.data as Int
                deleteTypeItem(draftEduId, JobExperience.TYPE_EDU_BACKGROUND)
            }
            EventCode.EVENT_DELETE_CV_CERTIFICATE -> {
                // 删除资格证书
                val draftEduId = baseEvent.data as Int
                deleteTypeItem(draftEduId, JobExperience.TYPE_CERTIFICATION)
            }
            else -> {}
        }


    }

    private fun deleteTypeItem(deleteId: Int, itemViewType: Int) {
        var position = -1
        mAdapter.data.forEachIndexed { index, it ->
            if (it.nativeItemViewType == itemViewType && it.id == deleteId) {
                position = index
                return@forEachIndexed
            }
        }
        if (position > -1) {
            mAdapter.removeAt(position)
        }
    }
}
