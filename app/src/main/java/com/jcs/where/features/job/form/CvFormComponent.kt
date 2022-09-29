package com.jcs.where.features.job.form

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.job.CreateJobExperience
import com.jcs.where.api.response.job.CreateProfileDetail

/**
 * Created by Wangsw  2022/9/29 15:01.
 *
 */
interface CvFormView : BaseMvpView {

    /**
     * 简历个人信息修改、创建成功
     */
    fun handleSuccess() {}

}


class CvFormPresenter(private var view: CvFormView) : BaseMvpPresenter(view) {


    fun handleProfile(lastProfileId: Int, apply: CreateProfileDetail) {

        if (lastProfileId == 0) {
            // 创建
            requestApi(mRetrofit.createCvProfile(apply), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.handleSuccess()
                }

            })

        } else {
            // 修改
            requestApi(mRetrofit.modifyCvProfile(lastProfileId, apply), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.handleSuccess()
                }

            })
        }

    }






    fun handleExperiences(lastId: Int, apply: CreateJobExperience) {

        if (lastId == 0) {
            // 创建
            requestApi(mRetrofit.createCvExperiences(apply), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.handleSuccess()
                }

            })

        } else {
            // 修改
            requestApi(mRetrofit.modifyCvExperiences(lastId, apply), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.handleSuccess()
                }

            })
        }

    }









}