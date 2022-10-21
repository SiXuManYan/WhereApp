package com.jcs.where.features.job.form

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.job.*
import java.util.ArrayList

/**
 * Created by Wangsw  2022/9/29 15:01.
 *
 */
interface CvFormView : BaseMvpView {

    /**
     * 简历个人信息、工作经历、教育背景
     * 修改、创建成功。
     */
    fun handleSuccess() {}

    /**
     * 设置教育背景
     */
    fun bindEduDet(response: EduDet){}

    /**
     * 设置学历列表
     */
    fun bindDegreeList(response: ArrayList<Degree>){}

}


class CvFormPresenter(private var view: CvFormView) : BaseMvpPresenter(view) {


    /**
     * 处理个人信息
     */
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


    /**
     * 处理工作经历
     */
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


    /**
     * 获取教育背景详情
     */
    fun getEduDet(eduId: Int) {
        if (eduId == 0) {
            return
        }
        requestApi(mRetrofit.eduBackgroundDet(eduId), object : BaseMvpObserver<EduDet>(view) {
            override fun onSuccess(response: EduDet) {
                view.bindEduDet(response)
            }

        })
    }

    /**
     * 获取学历列表
     */
    fun getDegreeList() {
        requestApi(mRetrofit.degreelList(), object : BaseMvpObserver<ArrayList<Degree>>(view) {
            override fun onSuccess(response: ArrayList<Degree>) {

                view.bindDegreeList(response)
            }
        })
    }

    fun handleSaveEdu(eduId: Int, eduRequest: EduRequest) {

        if (eduId == 0) {
            // 创建
            requestApi(mRetrofit.addEduBackground(eduRequest), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.handleSuccess()
                }

            })

        } else {
            // 修改
            requestApi(mRetrofit.editEduBackground(eduId, eduRequest), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.handleSuccess()
                }

            })
        }
    }


}