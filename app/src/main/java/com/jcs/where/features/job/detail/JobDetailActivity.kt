package com.jcs.where.features.job.detail

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.job.JobDetail
import com.jcs.where.api.response.job.JobSendCv
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.job.cv.CvHomeActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_job_detail.*

/**
 * Created by Wangsw  2022/9/28 14:04.
 *  职位详情
 */
class JobDetailActivity : BaseMvpActivity<JobDetailPresenter>(), JobDetailView {


    var jobId = 0

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_detail


    companion object {
        fun navigation(context: Context, jobId: Int) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, jobId)
            }
            val intent = Intent(context, JobDetailActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun initView() {
        jobId = intent.getIntExtra(Constant.PARAM_ID, 0)
    }

    override fun initData() {
        presenter = JobDetailPresenter(this)
        presenter.getData(jobId)
    }

    override fun bindListener() {

    }

    override fun bindDetail(response: JobDetail) {
        job_title_tv.text = response.job_title
        job_desc_tv.text = response.job_desc
        salary_tv.text = response.salary
        city_tv.text = response.city
        create_time_tv.text = response.created_at
        company_name_tv.text = response.company
        company_desc_tv.text = response.company_desc

        if (response.is_send) {
            setSendSuccessUi()
        } else {
            apply_tv.text = getString(R.string.apply_now)
            apply_tv.alpha = 1.0f
            apply_tv.isClickable = true
            apply_tv.setOnClickListener {

                if (!User.isLogon()) {
                    startActivity(LoginActivity::class.java)
                    return@setOnClickListener
                }

                if (response.is_complete) {
                    // 申请职位
                    presenter.sendCV(jobId)
                } else {
                    AlertDialog.Builder(this, R.style.JobAlertDialogTheme)
                        .setCancelable(false)
                        .setTitle(R.string.hint)
                        .setMessage(R.string.completing_cv)
                        .setPositiveButton(R.string.confirm) { dialog: DialogInterface, which: Int ->
                            startActivityAfterLogin(CvHomeActivity::class.java)
                            dialog.dismiss()
                        }
                        .setNegativeButton(R.string.cancel) { dialog: DialogInterface, which: Int ->
                            dialog.dismiss()
                        }
                        .create().show()
                }
            }
        }


    }

    private fun setSendSuccessUi() {
        apply_tv.text = getString(R.string.applied)
        apply_tv.alpha = 0.5f
        apply_tv.isClickable = false
    }


    override fun sendSuccess(response: JsonElement) {
        ToastUtils.showShort(R.string.successful_delivery)
        setSendSuccessUi()
    }

    /** 添加简历后，刷新本页状态 */
    private var needRefreshForCv= false

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_LOGIN_SUCCESS,-> {
                presenter.getData(jobId)
            }
            EventCode.EVENT_REFRESH_CV_PROFILE,
            EventCode.EVENT_REFRESH_CV_EXPERIENCE->{
                needRefreshForCv = true
            }

            else -> {}
        }

    }


    override fun onResume() {
        super.onResume()
        if (needRefreshForCv) {
            presenter.getData(jobId)
            needRefreshForCv = false
        }

    }


}


interface JobDetailView : BaseMvpView {
    fun bindDetail(response: JobDetail)
    fun sendSuccess(response: JsonElement)

}


class JobDetailPresenter(var view: JobDetailView) : BaseMvpPresenter(view) {
    fun getData(jobId: Int) {
        requestApi(mRetrofit.jobDetail(jobId), object : BaseMvpObserver<JobDetail>(view) {
            override fun onSuccess(response: JobDetail) {
                view.bindDetail(response)
            }

        })
    }


    /**
     * 发送简历 ， 申请职位
     */
    fun sendCV(jobId: Int) {

        val apply = JobSendCv().apply {
            job_id = jobId
        }

        requestApi(mRetrofit.jobSendCv(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.sendSuccess(response)
            }

        })
    }

}
