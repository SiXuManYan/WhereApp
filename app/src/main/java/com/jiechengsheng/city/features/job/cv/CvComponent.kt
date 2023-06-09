package com.jiechengsheng.city.features.job.cv

import com.blankj.utilcode.util.StringUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.job.CheckIsNeedUpdatePdf
import com.jiechengsheng.city.api.response.job.CheckResume
import com.jiechengsheng.city.api.response.job.JobExperience
import com.jiechengsheng.city.api.response.job.ProfileDetail

/**
 * Created by Wangsw  2022/9/28 17:06.
 *
 */
interface CvHomeView : BaseMvpView {
    fun getProfile(response: ProfileDetail?)
    fun bindJobExperience(toMutableList: MutableList<JobExperience>)
    fun resumeComplete(model: Int)

    /**
     * 检查是否需要更新pdf
     * @param isUpdate true 不需更新 false需要更新
     */
    fun checkIsNeedUpdatePdf(isUpdate: Boolean)

}


class CvHomePresenter(private var view: CvHomeView) : BaseMvpPresenter(view) {


    /**
     * 个人信息
     */
    fun getProfile() {
        requestApi(mRetrofit.profileDetail(), object : BaseMvpObserver<ProfileDetail>(view) {
            override fun onSuccess(response: ProfileDetail?) {
                view.getProfile(response)
            }

        })
    }

    /**
     * 检查简历是否完善性
     */
    fun checkResumeComplete(){
        requestApi(mRetrofit.checkResume(), object : BaseMvpObserver<CheckResume>(view) {
            override fun onSuccess(response: CheckResume) {
                view.resumeComplete(response.model)
            }

        })
    }



    /**
     * 检查简历是否完善性
     */
    fun checkIsNeedUpdatePdf(){
        requestApi(mRetrofit.checkIsNeedUpdatePdf(), object : BaseMvpObserver<CheckIsNeedUpdatePdf>(view) {
            override fun onSuccess(response: CheckIsNeedUpdatePdf) {
                view.checkIsNeedUpdatePdf(response.is_update)
            }

        })
    }

    /**
     * 工作经历、教育背景
     */
    fun getJobExperience() {
        requestApi(mRetrofit.jobExperienceList(), object : BaseMvpObserver<ArrayList<JobExperience>>(view) {
            override fun onSuccess(response: ArrayList<JobExperience>) {

                response.forEach {
                    it.nativeItemViewType = JobExperience.TYPE_JOB_EXPERIENCE
                }


                getEduBackground(response)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                getEduBackground()
            }

        })
    }


    /**
     * 工作经历
     */
    private fun getEduBackground(experience: ArrayList<JobExperience> = ArrayList()) {

        // 添加工作经历标题
        val jobTitle = JobExperience().apply {
            nativeTitleValue = StringUtils.getString(R.string.job_experience)
            nativeTitleType = JobExperience.TYPE_JOB_EXPERIENCE
            nativeItemViewType = JobExperience.TYPE_TITLE
        }
        experience.add(0, jobTitle)


        requestApi(mRetrofit.eduBackgroundList(), object : BaseMvpObserver<ArrayList<JobExperience>>(view) {
            override fun onSuccess(response: ArrayList<JobExperience>) {

                response.forEach {
                    it.nativeItemViewType = JobExperience.TYPE_EDU_BACKGROUND
                }

                // 添加教育背景标题
                val eduTitle = JobExperience().apply {
                    nativeTitleValue = StringUtils.getString(R.string.edu_background)
                    nativeTitleType = JobExperience.TYPE_EDU_BACKGROUND
                    this.nativeItemViewType = JobExperience.TYPE_TITLE
                }
                response.add(0, eduTitle)


                experience.addAll(response)
                // view.bindJobExperience(experience)
                getCertification(experience)

            }

            override fun onError(errorResponse: ErrorResponse?) {
                // view.bindJobExperience(experience)
                getCertification(experience)
            }

        })

    }

    /**
     * 获取资格证书
     */
    private fun getCertification(experience: ArrayList<JobExperience> = ArrayList()) {

        requestApi(mRetrofit.cvCertificate, object : BaseMvpObserver<ArrayList<JobExperience>>(view) {
            override fun onSuccess(response: ArrayList<JobExperience>) {
                response.forEach {
                    it.nativeItemViewType = JobExperience.TYPE_CERTIFICATION
                }

                // 添加资格证书标题
                val eduTitle = JobExperience().apply {
                    nativeTitleValue = StringUtils.getString(R.string.certification)
                    nativeTitleType = JobExperience.TYPE_CERTIFICATION
                    this.nativeItemViewType = JobExperience.TYPE_TITLE
                }
                response.add(0, eduTitle)

                experience.addAll(response)
                view.bindJobExperience(experience)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                view.bindJobExperience(experience)
            }

        })

    }


}