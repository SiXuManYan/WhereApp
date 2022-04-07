package com.jcs.where.features.mall.refund

import com.blankj.utilcode.util.RegexUtils
import com.google.gson.JsonElement
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.store.MallRefundRequest
import com.jcs.where.api.response.UploadFileResponse2
import com.jcs.where.api.response.mall.MallRefundInfo
import com.jcs.where.features.store.refund.image.RefundImage
import com.jcs.where.features.store.refund.image.StoreRefundAdapter2
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Wangsw  2022/3/24 15:47.
 *
 */
interface MallRefundEditView : BaseMvpView {
    fun bindDetail(response: MallRefundInfo)

    /** 申请退款提交成功 */
    fun applicationSuccess()

    /** 图片上传成功 */
    fun upLoadImageSuccess(link:ArrayList<String>, orderId: Int, desc: String)
}

class MallRefundEditPresenter(private var view: MallRefundEditView) : BaseMvpPresenter(view) {

    fun getData(orderId: Int, refund_id: Int) {
        requestApi(mRetrofit.mallRefundInfo(orderId, refund_id), object : BaseMvpObserver<MallRefundInfo>(view) {
            override fun onSuccess(response: MallRefundInfo) {
                view.bindDetail(response)
            }

        })
    }


    /**
     * 申请售后
     */
    fun doRefund(orderId: Int, cancelReason: String, cancelImages: String? = null) {

        val apply = MallRefundRequest().apply {
            cancel_reason = cancelReason
            cancel_images = cancelImages
        }

        requestApi(mRetrofit.mallRefund(orderId, apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.applicationSuccess()
            }

        })
    }

    /**
     * 获取相册内的图片资源
     */
    private fun getImageImageUrls(adapter: StoreRefundAdapter2): ArrayList<String> {
        val imageUrl = ArrayList<String>()
        adapter.data.forEach {
            if (it.type != RefundImage.TYPE_ADD) {
                imageUrl.add(it.imageSource)
            }
        }
        return imageUrl
    }

    /**
     * 获取所有已经上传过的图片
     */
    fun getAllAlreadyUploadImage(mImageAdapter: StoreRefundAdapter2):java.util.ArrayList<String>{

        val link = java.util.ArrayList<String>()
        mImageAdapter.data.forEach {
            if (RegexUtils.isURL(it.imageSource)) {
                link.add(it.imageSource)
            }
        }
        return link
    }


    /**
     * 多图上传
     */
    fun upLoadImage(adapter: StoreRefundAdapter2, orderId: Int, desc: String) {

        val map: HashMap<String, RequestBody> = HashMap()
        val imageImageUrls = getImageImageUrls(adapter)

        imageImageUrls.forEach {
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

        if (map.isEmpty()) {
            view.upLoadImageSuccess(ArrayList<String>(), orderId, desc)
            return
        }

        requestApi(mRetrofit.uploadMultiImages(description, map), object : BaseMvpObserver<UploadFileResponse2>(view) {
            override fun onSuccess(response: UploadFileResponse2) {

                view.upLoadImageSuccess(response.link, orderId, desc)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
            }

        })
    }

}
