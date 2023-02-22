package com.jcs.where.features.job.form

import com.blankj.utilcode.util.RegexUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.UploadFileResponse
import com.jcs.where.api.response.UploadFileResponse2
import com.jcs.where.api.response.job.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

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
    fun bindEduDet(response: EduDet) {}

    /**
     * 设置学历列表
     */
    fun bindDegreeList(response: ArrayList<Degree>) {}

    /**
     * 工作经历删除成功
     */
    fun deleteJobExperienceSuccess() {}

    /**
     * 教育背景删除成功
     */
    fun deleteEducationSuccess() {}

}


class CvFormPresenter(private var view: CvFormView) : BaseMvpPresenter(view) {


    /**
     * 处理个人信息
     */
    fun handleAvatar(lastProfileId: Int, apply: CreateProfileDetail, currentAvatarUrlOrUriPath: String?) {

        if (!currentAvatarUrlOrUriPath.isNullOrBlank() && !RegexUtils.isURL(currentAvatarUrlOrUriPath)) {


            val file: File = File(currentAvatarUrlOrUriPath)
            val requestFile = RequestBody.create(MediaType.parse("image/jpg"), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val type = "1"
            val description = RequestBody.create(MediaType.parse("multipart/form-data"), type)


            requestApi(mRetrofit.uploadFile(description, body), object : BaseMvpObserver<UploadFileResponse>(view) {
                override fun onSuccess(response: UploadFileResponse) {
                    val link = response.link
                    apply.avatar = link
                    handleProfile(lastProfileId,apply)
                }

                override fun onError(errorResponse: ErrorResponse?) {
                    handleProfile(lastProfileId,apply)
                }
            })



        } else {
            apply.avatar = currentAvatarUrlOrUriPath
            handleProfile(lastProfileId, apply)
        }

    }

    private fun handleProfile(lastProfileId: Int, apply: CreateProfileDetail) {
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
     * 处理工作经历 创建和编辑
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

    /**
     * 处理教育背景
     */
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


    /**
     * 删除工作经历
     */
    fun deleteJobExperience(draftExperienceId: Int) {
        requestApi(mRetrofit.deleteExperiences(draftExperienceId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.deleteJobExperienceSuccess()
            }

        })
    }


    /**
     * 删除教育经历
     */
    fun deleteEducation(draftEduId: Int) {
        requestApi(mRetrofit.deleteEducation(draftEduId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.deleteEducationSuccess()
            }

        })
    }


}