package com.jiechengsheng.city.features.merchant.form

import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jiechengsheng.city.BaseApplication
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.merchant.MerchantSettledPost
import com.jiechengsheng.city.api.response.UploadFileResponse2
import com.jiechengsheng.city.storage.entity.User
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Wangsw  2021/11/19 16:23.
 *
 */

interface SettledFormView : BaseMvpView {
    fun postResult(result: Boolean)

}


class SettledFormPresenter(private var view: SettledFormView) : BaseMvpPresenter(view) {

    /**
     * 多图上传
     * @param commentType       0 酒店
     *                          1 旅游
     */
    fun upLoadImage(body: MerchantSettledPost, imageUrls: ArrayList<String>, isReCommit: Boolean, reCommitMerchantId: Int) {
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
                val descImages = Gson().toJson(response.link)
                body.business_license = descImages
                postForm(body, isReCommit, reCommitMerchantId)
            }
        })
    }


    private fun postForm(body: MerchantSettledPost, isReCommit: Boolean, reCommitMerchantId: Int) {

        if (isReCommit) {
            requestApi(mRetrofit.repostMerchantSettled(reCommitMerchantId, body), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {


                    val user = User.getInstance()
                    user.merchantApplyStatus = 1

                    val app = Utils.getApp() as BaseApplication
                    val userDao = app.database.userDao()
                    userDao.addUser(user)
                    User.update()

                    view.postResult(true)

                }

                override fun onError(errorResponse: ErrorResponse?) {
                    super.onError(errorResponse)
                    view.postResult(false)
                }
            })
        } else {
            requestApi(mRetrofit.postMerchantSettled(body), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {


                    val user = User.getInstance()
                    user.merchantApplyStatus = 1

                    val app = Utils.getApp() as BaseApplication
                    val userDao = app.database.userDao()
                    userDao.addUser(user)
                    User.update()

                    view.postResult(true)

                }

                override fun onError(errorResponse: ErrorResponse?) {
                    super.onError(errorResponse)
                    view.postResult(false)
                }
            })
        }


    }

}