package com.jcs.where.features.job.cv

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.job.Job
import com.jcs.where.api.response.job.JobExperience
import com.jcs.where.api.response.job.ProfileDetail
import java.util.ArrayList

/**
 * Created by Wangsw  2022/9/28 17:06.
 *
 */
interface CvHomeView : BaseMvpView {
    fun getProfile(response: ProfileDetail?)
    fun bindJobExperience(toMutableList: MutableList<JobExperience>)

}


class CvHomePresenter(private var view: CvHomeView) : BaseMvpPresenter(view) {


    fun getProfile() {
        requestApi(mRetrofit.profileDetail(), object : BaseMvpObserver<ProfileDetail>(view) {
            override fun onSuccess(response: ProfileDetail?) {
                view.getProfile(response)
            }

        })

    }

    fun getJobExperience() {
        requestApi(mRetrofit.jobExperienceList(),object :BaseMvpObserver<ArrayList<JobExperience>>(view){
            override fun onSuccess(response: ArrayList<JobExperience>) {
                view.bindJobExperience(response.toMutableList())
            }

        })
    }

}