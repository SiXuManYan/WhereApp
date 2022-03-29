package com.jcs.where.features.comment.batch

import android.annotation.SuppressLint
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.hotel.BatchComment
import com.jcs.where.api.response.UploadFileResponse2
import com.jcs.where.features.store.refund.image.RefundImage
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Wangsw  2022/3/26 14:02.
 *
 */
interface BatchCommentView : BaseMvpView {
    fun commentSuccess()

}

class BatchCommentPresenter(private var view: BatchCommentView) : BaseMvpPresenter(view) {

    fun handleComment(mAdapter: BatchCommentAdapter, orderId: Int) {

        val requestBody = BatchComment().apply {
            order_id = orderId
        }


        // 处理图片
        val imageData = ArrayList<RefundImage>()

        mAdapter.data.forEachIndexed { index, item ->

            val imageList = item.nativeImage

            if (imageList.isNotEmpty()) {

                imageList.forEach {
                    it.tempParentIndex = index
                    if (it.type == RefundImage.TYPE_EDIT) {
                        imageData.add(it)
                    }

                }
            }

            requestBody.goods_comments_temp.add(item)
        }

        upLoadImage(imageData, requestBody)

    }


    /**
     * 多图上传
     */
    @SuppressLint("CheckResult")
    fun upLoadImage(nativeImageData: ArrayList<RefundImage>, requestBody: BatchComment) {

        if (nativeImageData.isEmpty()) {

            postComment(requestBody)
            return
        }

        val description = RequestBody.create(MediaType.parse("multipart/form-data"), "2")

        val partMap = getPartMap(nativeImageData)

        requestApi(mRetrofit.uploadMultiImages(description, partMap), object : BaseMvpObserver<UploadFileResponse2>(view) {
            override fun onSuccess(response: UploadFileResponse2) {

                val link = response.link
                if (link.size == nativeImageData.size) {

                    nativeImageData.forEachIndexed { index, refundImage ->
                        refundImage.tempRealImageUrl = link[index]
                    }

                    nativeImageData.forEachIndexed { index, refundImage ->
                        val tempParentIndex = refundImage.tempParentIndex
                        requestBody.goods_comments_temp[tempParentIndex].image.add(refundImage.tempRealImageUrl)
                    }

                    // 发布
                    postComment(requestBody)
                }
            }


        })


    }

    private fun postComment(requestBody: BatchComment) {

        requestBody.goods_comments = Gson().toJson(requestBody.goods_comments_temp)

        requestApi(mRetrofit.batchComment(requestBody), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.commentSuccess()
            }
        })
    }


    private fun getPartMap(imageData: ArrayList<RefundImage>): Map<String, RequestBody> {
        val map: HashMap<String, RequestBody> = HashMap()
        val imageImageUrls = getImageImageUrls(imageData)

        imageImageUrls.forEach {
            // 只上传相册
            if (!RegexUtils.isURL(it)) {
                val file = File(it)
                val requestFile = RequestBody.create(MediaType.parse("image/jpg"), file)
                // 多图上传的key 由 file改成　file[]
                map["file[]\"; filename=\"" + file.name] = requestFile;
            }
        }

        return map
    }


    /**
     * 获取相册内的图片资源
     */
    private fun getImageImageUrls(imageData: ArrayList<RefundImage>): ArrayList<String> {
        val imageUrl = ArrayList<String>()
        imageData.forEach {
            if (it.type != RefundImage.TYPE_ADD) {
                imageUrl.add(it.imageSource)
            }
        }
        return imageUrl
    }

}

class ImageZipResponse {
    var response2: UploadFileResponse2 = UploadFileResponse2()
}