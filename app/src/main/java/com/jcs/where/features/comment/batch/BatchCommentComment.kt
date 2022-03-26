package com.jcs.where.features.comment.batch

import com.blankj.utilcode.util.RegexUtils
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.UploadFileResponse2
import com.jcs.where.features.store.refund.image.RefundImage
import com.jcs.where.features.store.refund.image.StoreRefundAdapter2
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Wangsw  2022/3/26 14:02.
 *
 */
interface BatchCommentView : BaseMvpView {

}

class BatchCommentPresenter(private var view: BatchCommentView) : BaseMvpPresenter(view) {

    fun handleComment(mAdapter: BatchCommentAdapter) {




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
//            view.upLoadImageSuccess(ArrayList<String>(), orderId, desc)
            return
        }

        requestApi(mRetrofit.uploadMultiImages(description, map), object : BaseMvpObserver<UploadFileResponse2>(view) {
            override fun onSuccess(response: UploadFileResponse2) {

//                view.upLoadImageSuccess(response.link, orderId, desc)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
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

}