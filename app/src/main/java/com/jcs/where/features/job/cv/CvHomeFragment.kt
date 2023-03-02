package com.jcs.where.features.job.cv

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
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
import com.jcs.where.features.job.pdf.CvPdfActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.list.DividerDecoration
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_job_cv_home.*

/**
 * Created by Wangsw  2022/12/15 11:18.
 * 个人简历
 */
class CvHomeFragment : BaseMvpFragment<CvHomePresenter>(), CvHomeView, OnItemClickListener {

    private lateinit var mAdapter: JobExperienceEduAdapter

    /** 个人信息 */
    private var profileDetail: ProfileDetail? = null

    private lateinit var avatarIv: CircleImageView

    override fun getLayoutId() = R.layout.fragment_job_cv_home

    override fun initView(view: View) {
        initContent(view)
    }

    private fun initContent(view: View) {

        avatarIv = view.findViewById(R.id.avatar_iv)
        mAdapter = JobExperienceEduAdapter().apply {
            setOnItemClickListener(this@CvHomeFragment)
        }

        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        content_rv.apply {
            adapter = mAdapter
            layoutManager = manager
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
        presenter.checkIsNeedUpdatePdf()
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            activity?.finish()
        }
        profile_rl.setOnClickListener {
            startActivityAfterLogin(CvFormProfileActivity::class.java, Bundle().apply {
                putParcelable(Constant.PARAM_DATA, profileDetail)
            })
        }
        check_resume_fl.setOnClickListener {
            presenter.checkResumeComplete()
        }
    }

    override fun getProfile(response: ProfileDetail?) {

        response?.let {
            GlideUtil.load(requireContext(), response.avatar, avatarIv, 24)
            if (it.pdf_time.isNullOrBlank()) {
                refresh_time_tv.visibility = View.GONE
            } else {
                refresh_time_tv.text = getString(R.string.refresh_time_format, it.pdf_time)
                refresh_time_tv.visibility = View.VISIBLE
            }

        }
        profileDetail = response

        if (response == null || response.id == 0) {
            create_cv_rl.visibility = View.VISIBLE
            info_ll.visibility = View.GONE
            return
        }

        create_cv_rl.visibility = View.GONE
        info_ll.visibility = View.VISIBLE

        (response.first_name + " " + response.last_name).also { name_tv.text = it }

        val gender = when (response.gender) {
            1 -> getString(R.string.male)
            2 -> getString(R.string.female)
            else -> ""
        }

        (gender + " | " + response.city).also { gender_tv.text = it }
    }

    override fun bindJobExperience(toMutableList: MutableList<JobExperience>) {
        mAdapter.setNewInstance(toMutableList)
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val item = mAdapter.data[position]

        when (adapter.getItemViewType(position)) {

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

    override fun resumeComplete(model: Int) = if (model == 0) {
        startActivity(CvPdfActivity::class.java, Bundle().apply {
            putParcelable(Constant.PARAM_DATA, profileDetail)
            putParcelableArrayList(Constant.PARAM_CV, ArrayList(mAdapter.data))
        })
    } else {
        showDegree(model)
    }

    /**
     * @param model 0 完整 1个人信息 不完善 2 教育经历 不完善 3工作经验 不完善
     */
    private fun showDegree(model: Int) {
        val timeDialog = BottomSheetDialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_resume_complete, null)
        timeDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val contentTv = view.findViewById<TextView>(R.id.content_tv)

        val modelStr = when (model) {
            1 -> getString(R.string.complete_personal)
            2 -> getString(R.string.complete_edu)
            3 -> getString(R.string.complete_job)
            else -> ""
        }


        val result = getString(R.string.complete_format, modelStr)
        val start = result.indexOf(modelStr)

        val builder = SpannableStringBuilder(result)
        builder.setSpan(ForegroundColorSpan(ColorUtils.getColor(R.color.blue_377BFF)),
            start,
            start + modelStr.length,
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
        contentTv.text = builder

        view.findViewById<Button>(R.id.ok).setOnClickListener {
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    override fun checkIsNeedUpdatePdf(isUpdate: Boolean) {

        if (isUpdate) {
            update_hint_ll.visibility = View.VISIBLE
        } else {
            update_hint_ll.visibility = View.INVISIBLE
        }
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)

        var toastStr = getString(R.string.job_save_success)
        when (baseEvent.code) {
            EventCode.EVENT_LOGIN_SUCCESS -> {
                if (isViewCreated) {
                    loadOnVisible()
                }
            }
            EventCode.EVENT_REFRESH_CV_PROFILE -> {
                presenter.getProfile()
                presenter.checkIsNeedUpdatePdf()
                showCvToast(toastStr)
            }
            EventCode.EVENT_REFRESH_CV_EXPERIENCE,
            EventCode.EVENT_REFRESH_CV_EDU,
            EventCode.EVENT_REFRESH_CV_CERTIFICATE,
            -> {
                presenter.getJobExperience()
                presenter.checkIsNeedUpdatePdf()
                showCvToast(toastStr)
            }
            EventCode.EVENT_DELETE_CV_EXPERIENCE -> {
                // 删除工作经历
                val draftExperienceId = baseEvent.data as Int
                deleteTypeItem(draftExperienceId, JobExperience.TYPE_JOB_EXPERIENCE)
                toastStr = getString(R.string.job_delete_success)
                presenter.checkIsNeedUpdatePdf()
                showCvToast(toastStr)
            }
            EventCode.EVENT_DELETE_CV_EDU -> {
                // 删除教育背景
                val draftEduId = baseEvent.data as Int
                deleteTypeItem(draftEduId, JobExperience.TYPE_EDU_BACKGROUND)
                toastStr = getString(R.string.job_delete_success)
                presenter.checkIsNeedUpdatePdf()
                showCvToast(toastStr)
            }
            EventCode.EVENT_DELETE_CV_CERTIFICATE -> {
                // 删除资格证书
                val draftEduId = baseEvent.data as Int
                deleteTypeItem(draftEduId, JobExperience.TYPE_CERTIFICATION)
                toastStr = getString(R.string.job_delete_success)
                presenter.checkIsNeedUpdatePdf()
                showCvToast(toastStr)
            }
            EventCode.EVENT_PDF_GENERATE_SUCCESS -> {
                presenter.getProfile()
                presenter.checkIsNeedUpdatePdf()
                showCvToast(toastStr)
            }
            else -> {}
        }

    }

    private fun showCvToast(toastStr: String) {
        ToastUtils.make()
            .setMode(ToastUtils.MODE.DARK)
            .setGravity(Gravity.CENTER, 0, 0)
            .show(toastStr)
    }

}
