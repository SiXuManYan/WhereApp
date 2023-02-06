package com.jcs.where.features.job.form.certificate

import com.blankj.utilcode.util.RegexUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.UploadFileResponse2
import com.jcs.where.api.response.job.CreateCertificate
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.util.ArrayList

/**
 * Created by Wangsw  2023/2/6 11:00.
 *
 */
interface CertificateView : BaseMvpView {
    fun handleSuccess()
    fun deleteSuccess()
}


class CertificatePresenter(private var view: CertificateView) : BaseMvpPresenter(view) {

    /**
     * 处理资格证书 创建和编辑
     */
    fun handleCertificate(lastId: Int, apply: CreateCertificate) {

        if (lastId == 0) {
            // 创建
            requestApi(mRetrofit.createCvExperiences(apply), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.handleSuccess()
                }

            })

        } else {
            // 修改
            requestApi(mRetrofit.modifyCvCertificate(lastId, apply), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.handleSuccess()
                }

            })
        }
    }


    /**
     * 删除工作经历
     */
    fun deleteCertificate(draftId: Int) {
        requestApi(mRetrofit.deleteCertificate(draftId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.deleteSuccess()
            }

        })
    }

    fun upLoadImage(draftId: Int, name: String, imageUrls: ArrayList<String>) {


        val map: HashMap<String, RequestBody> = HashMap()
        imageUrls.forEach {
            if (!RegexUtils.isURL(it)) {
                // 只上传本地图片
                val file = File(it)
                val requestFile = RequestBody.create(MediaType.parse("image/jpg"), file)
                // 多图上传的key 由 file改成　file[]
                map["file[]\"; filename=\"" + file.name] = requestFile;
            }
        }
        val type = "2"
        val description = RequestBody.create(MediaType.parse("multipart/form-data"), type)

        requestApi(mRetrofit.uploadMultiImages(description, map), object : BaseMvpObserver<UploadFileResponse2>(view) {
            override fun onSuccess(response: UploadFileResponse2) {
                val link = response.link
                val descImages = Gson().toJson(link)

                val apply = CreateCertificate().apply {
                    title = name
                    images = descImages
                }

                handleCertificate(draftId , apply)

            }
        })






    }
}