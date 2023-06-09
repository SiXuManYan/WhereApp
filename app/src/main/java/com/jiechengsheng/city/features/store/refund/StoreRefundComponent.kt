package com.jiechengsheng.city.features.store.refund

import com.blankj.utilcode.util.RegexUtils
import com.google.gson.JsonElement
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.store.MallRefundModifyRequest
import com.jiechengsheng.city.api.request.store.MallRefundRequest
import com.jiechengsheng.city.api.request.store.StoreRefundModifyRequest
import com.jiechengsheng.city.api.request.store.StoreRefundRequest
import com.jiechengsheng.city.api.response.UploadFileResponse2
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

    /**
     * 修改售后申请成功
     */
    fun modifyApplicationSuccess()

    /**
     * 图片上传成功
     */
    fun upLoadImageSuccess(link: java.util.ArrayList<String>, orderId: Int, desc: String)

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
     * 修改申请售后
     */
    fun modifyRefundAgain(order_Id: Int, description: String, descImages: String? = null) {

        val apply = StoreRefundModifyRequest().apply {
            desc = description
            images = descImages
        }

        requestApi(mRetrofit.storeRefundModify(order_Id, apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.modifyApplicationSuccess()
            }

        })
    }


    /**
     * 多图上传
     */
    fun upLoadImage(imageUrls: ArrayList<String>, orderId: Int, desc: String) {

        val map: HashMap<String, RequestBody> = HashMap()
        imageUrls.forEach {
            if (!RegexUtils.isURL(it)) {
                // 只上传相册
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
//                val link = response.link
//                val descImages = Gson().toJson(link)
//                storeRefund(orderId, desc, descImages)
                view.upLoadImageSuccess(response.link, orderId , desc)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
            }

        })
    }


    /**
     * 获取所有已经上传过的图片
     */
    fun getAllAlreadyUploadImage(mImageAdapter: StoreRefundAdapter):java.util.ArrayList<String>{

        val link = java.util.ArrayList<String>()
        mImageAdapter.data.forEach {
            if (RegexUtils.isURL(it)) {
                link.add(it)
            }
        }
        return link
    }

}


class MallRefundPresenter(private var view: StoreRefundView) : BaseMvpPresenter(view) {

    /**
     * 申请售后
     */
    fun storeRefund(order_Id: Int, description: String, descImages: String? = null) {

        val apply = MallRefundRequest().apply {
            cancel_reason = description
            cancel_images = descImages
        }

        requestApi(mRetrofit.mallRefund(order_Id,apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.applicationSuccess()
            }

        })
    }

    /**
     * 修改申请售后
     */
    fun modifyRefundAgain(order_Id: Int, description: String, descImages: String? = null) {

        val apply = MallRefundModifyRequest().apply {
            desc = description
            images = descImages
        }

        requestApi(mRetrofit.mallRefundModify(order_Id, apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.modifyApplicationSuccess()
            }

        })
    }


    /**
     * 多图上传
     */
    fun upLoadImage(imageUrls: ArrayList<String>, orderId: Int, desc: String) {

        val map: HashMap<String, RequestBody> = HashMap()
        imageUrls.forEach {
            if (!RegexUtils.isURL(it)) {
                // 只上传相册
                val file = File(it)
                val requestFile = RequestBody.create(MediaType.parse("image/jpg"), file)
                // 多图上传的key 由 file改成　file[]
                map["file[]\"; filename=\"" + file.name] = requestFile;
            }
        }

        val type = "2"
        val description = RequestBody.create(MediaType.parse("multipart/form-data"), type)

        if (map.isEmpty()){
            view.upLoadImageSuccess(ArrayList<String>(), orderId , desc)
            return
        }

        requestApi(mRetrofit.uploadMultiImages(description, map), object : BaseMvpObserver<UploadFileResponse2>(view) {
            override fun onSuccess(response: UploadFileResponse2) {
//                val link = response.link
//                val descImages = Gson().toJson(link)
//                storeRefund(orderId, desc, descImages)
                view.upLoadImageSuccess(response.link, orderId , desc)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
            }

        })
    }


    /**
     * 获取所有已经上传过的图片
     */
    fun getAllAlreadyUploadImage(mImageAdapter: StoreRefundAdapter):java.util.ArrayList<String>{

        val link = java.util.ArrayList<String>()
        mImageAdapter.data.forEach {
            if (RegexUtils.isURL(it)) {
                link.add(it)
            }
        }
        return link
    }

}