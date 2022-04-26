package com.jcs.where.features.mall.refund.complaint

import com.blankj.utilcode.util.RegexUtils
import com.google.gson.JsonElement
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.hotel.ComplaintRequest
import com.jcs.where.api.response.UploadFileResponse2
import com.jcs.where.features.store.refund.image.StoreRefundAdapter2
import com.jcs.where.utils.BusinessUtils
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Wangsw  2022/3/29 14:46.
 *
 */

interface ComplaintView : BaseMvpView {
    fun upLoadImageSuccess(arrayList: java.util.ArrayList<String>, orderId: Int, desc: String)
    fun applicationSuccess()

}

class ComplaintPresenter(private var view: ComplaintView) : BaseMvpPresenter(view) {


    fun upLoadImage(mImageAdapter: StoreRefundAdapter2, orderId: Int, desc: String) {
        val map: HashMap<String, RequestBody> = HashMap()
        val imageImageUrls = BusinessUtils.getImageImageUrls(mImageAdapter)

        imageImageUrls.forEach {
            if (!RegexUtils.isURL(it)) {
                // 只上传本地相册选中的图片
                val file = File(it)
                val requestFile = RequestBody.create(MediaType.parse("image/jpg"), file)
                // 多图上传的key 由 file改成　file[]
                map["file[]\"; filename=\"" + file.name] = requestFile;
            }
        }

        val description = RequestBody.create(MediaType.parse("multipart/form-data"), "2")
        if (map.isEmpty()) {
            view.upLoadImageSuccess(ArrayList<String>(), orderId, desc)
            return
        }

        requestApi(mRetrofit.uploadMultiImages(description, map), object : BaseMvpObserver<UploadFileResponse2>(view) {
            override fun onSuccess(response: UploadFileResponse2) {

                view.upLoadImageSuccess(response.link, orderId, desc)
            }



        })


    }

    /**
     * 申诉
     */
    fun doComplaint(orderId: Int, desc: String, descImages: String? = null) {
        val apply = ComplaintRequest().apply {
            order_id = orderId
            content = desc
            image = descImages
        }

        requestApi(mRetrofit.complaint( apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.applicationSuccess()
            }

        })
    }

}