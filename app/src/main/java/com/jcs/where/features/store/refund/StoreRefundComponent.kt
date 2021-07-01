package com.jcs.where.features.store.refund

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.store.StoreRefundRequest
import com.jcs.where.api.response.UploadFileResponse2
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File


/**
 * Created by Wangsw  2021/7/1 10:27.
 *
 */

interface StoreRefundView : BaseMvpView {
    /**
     * 售后申请成功
     */
    fun applicationSuccess()

}

class StoreRefundPresenter(private var view: StoreRefundView) : BaseMvpPresenter(view) {

    /**
     * 申请售后
     */
    fun storeRefund(order_Id: Int, description: String, descImages: String? = null) {

        val apply = StoreRefundRequest().apply {
            orderId = order_Id
            desc = description
            images = descImages
        }

        requestApi(mRetrofit.storeRefund(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.applicationSuccess()
            }

        })
    }


    /**
     * 多图上传
     */
    fun upLoadImage(imageUrls: ArrayList<String>, orderId: Int, desc: String) {

        val map: HashMap<String, RequestBody> = HashMap()
        imageUrls.forEach {
            val file = File(it)
            val requestFile = RequestBody.create(MediaType.parse("image/jpg"), file)
            // 多图上传的key 由 file改成　file[]
            map["file[]\"; filename=\"" + file.name] = requestFile;
        }

        val type = "2"
        val description = RequestBody.create(MediaType.parse("multipart/form-data"), type)

        requestApi(mRetrofit.uploadMultiImages(description, map), object : BaseMvpObserver<UploadFileResponse2>(view) {
            override fun onSuccess(response: UploadFileResponse2) {
                val link = response.link
                val descImages = Gson().toJson(link)
                storeRefund(orderId, desc, descImages)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
            }

        })


    }

}