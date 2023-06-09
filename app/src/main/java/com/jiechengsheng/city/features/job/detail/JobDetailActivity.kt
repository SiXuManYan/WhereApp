package com.jiechengsheng.city.features.job.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonElement
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.MtjClickHomeJob
import com.jiechengsheng.city.api.response.job.JobCollection
import com.jiechengsheng.city.api.response.job.JobDetail
import com.jiechengsheng.city.api.response.job.JobSendCv
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.features.job.company.CompanyActivity
import com.jiechengsheng.city.features.job.report.ReportActivity
import com.jiechengsheng.city.frames.common.Html5Url
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.GlideUtil
import com.jiechengsheng.city.utils.MobUtil
import com.jiechengsheng.city.utils.image.GlideRoundedCornersTransform
import kotlinx.android.synthetic.main.activity_job_detail.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/9/28 14:04.
 *  职位详情
 */
class JobDetailActivity : BaseMvpActivity<JobDetailPresenter>(), JobDetailView {


    var jobId = 0
    var companyId = 0
    var isCollect = false

    /** 添加简历后，刷新本页状态 */
    private var needRefreshForCv = false

    /** 是否通过招聘首页点击 */
    private var isFromJobHome = false

    private var degreeDialog: BottomSheetDialog? = null

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_job_detail


    companion object {
        fun navigation(context: Context, jobId: Int, fromJobHome: Boolean? = false) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, jobId)

                fromJobHome?.let {
                    putBoolean(Constant.PARAM_TYPE, it)
                }
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
        BarUtils.setStatusBarColor(this, Color.WHITE)
        jobId = intent.getIntExtra(Constant.PARAM_ID, 0)
        isFromJobHome = intent.getBooleanExtra(Constant.PARAM_TYPE, false)
    }

    override fun initData() {
        presenter = JobDetailPresenter(this)
        presenter.getData(jobId)
        if (isFromJobHome) {
            presenter.mtjClickHomeJob(jobId)
        }
    }

    override fun bindListener() {

        collect_iv.setOnClickListener {
            if (!User.isLogon()) {
                startActivity(LoginActivity::class.java)
                return@setOnClickListener
            }
            presenter.handleCollection(isCollect, jobId)
        }

        report_iv.setOnClickListener {
            startActivityAfterLogin(ReportActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_ID, jobId)
            })
        }

        share_iv.setOnClickListener {
            val url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_JOB, jobId)
            MobUtil.shareFacebookWebPage(url, this)
        }

        company_rv.setOnClickListener {
            CompanyActivity.navigation(this, companyId)
        }

    }

    override fun bindDetail(response: JobDetail) {
        companyId = response.company_id
        job_title_tv.text = response.job_title
        duty_tv.text = response.duty
        job_rq_tv.text = response.requirement


        salary_tv.text = response.salary
        city_tv.text = response.city
        create_time_tv.text = response.created_at

        isCollect = response.is_collect
        setLikeImage()


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
                if (response.is_complete && response.is_complete_pdf) {
                    // 申请职位
                    presenter.sendCV(jobId)
                } else {
                    if (degreeDialog != null) {
                        degreeDialog?.show()
                    } else {
                        showDegree()
                    }
                }
            }
        }

        response.company_info?.let {
            GlideUtil.load(this, it.logo, logo_iv, 24, GlideRoundedCornersTransform.CornerType.ALL, R.mipmap.ic_company_default_logo)
            company_name_tv.text = it.company_title
            company_desc_tv.text = it.profile
        }


    }

    private fun setSendSuccessUi() {
        apply_tv.text = getString(R.string.applied)
        apply_tv.alpha = 0.5f
        apply_tv.isClickable = false
    }


    override fun sendSuccess(response: JsonElement) {
        ToastUtils.showShort(R.string.successful_delivery)
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_CV_SEND_SUCCESS))
        setSendSuccessUi()
    }

    override fun collectionResult(result: Boolean) {
        isCollect = result
        setLikeImage()
        if (result) {
            ToastUtils.showShort(R.string.collection_success)
        } else {
            ToastUtils.showShort(R.string.cancel_collection_success)
        }

        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_COLLECTION))

    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_LOGIN_SUCCESS -> {
                presenter.getData(jobId)
            }
            EventCode.EVENT_REFRESH_CV_PROFILE,
            EventCode.EVENT_REFRESH_CV_EXPERIENCE,
            -> {
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


    private fun setLikeImage() {

        collect_iv.setImageResource(
            if (isCollect) {
                R.mipmap.ic_like_red_night
            } else {
                R.mipmap.ic_like_normal_night
            }
        )
    }


    private fun showDegree() {
        val timeDialog = BottomSheetDialog(this)
        this.degreeDialog = timeDialog
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_complete_cv, null)
        timeDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        view.findViewById<Button>(R.id.ok).setOnClickListener {
            timeDialog.dismiss()
        }
        timeDialog.show()
    }


}


interface JobDetailView : BaseMvpView {
    fun bindDetail(response: JobDetail)
    fun sendSuccess(response: JsonElement)
    fun collectionResult(result: Boolean)

}


class JobDetailPresenter(var view: JobDetailView) : BaseMvpPresenter(view) {

    fun getData(jobId: Int) {
        requestApi(mRetrofit.jobDetail(jobId, 2), object : BaseMvpObserver<JobDetail>(view) {
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

    /**
     * 统计从招聘首页职位列表入口点击
     */
    fun handleCollection(collect: Boolean, jobId: Int) {
        val apply = JobCollection().apply {
            job_id = jobId
        }

        if (collect) {
            requestApi(mRetrofit.jobDelCollection(apply), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.collectionResult(false)
                }
            })
        } else {
            requestApi(mRetrofit.jobCollection(apply), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.collectionResult(true)
                }
            })
        }
    }

    fun mtjClickHomeJob(jobId: Int) {
        val apply = MtjClickHomeJob().apply {
            job_id = jobId
        }
        requestApi(mRetrofit.mtjClickHomeJob(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {

            }
        })

    }

}
