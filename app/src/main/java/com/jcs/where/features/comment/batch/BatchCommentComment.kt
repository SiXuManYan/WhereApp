package com.jcs.where.features.comment.batch

import android.annotation.SuppressLint
import com.blankj.utilcode.util.RegexUtils
import com.jcs.where.api.JcsResponse
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.hotel.BatchComment
import com.jcs.where.api.request.hotel.BatchCommentItem
import com.jcs.where.api.response.UploadFileResponse2
import com.jcs.where.features.store.refund.image.RefundImage
import io.reactivex.Observable
import io.reactivex.functions.Function
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

    fun handleComment(mAdapter: BatchCommentAdapter, orderId: Int) {


        val body = BatchComment().apply {
            order_id = orderId
        }

        val goods_comments_temp = ArrayList<BatchCommentItem>()


        var imageObservable = ArrayList<Observable<JcsResponse<UploadFileResponse2>>>()

        mAdapter.data.forEachIndexed { index, item ->
            goods_comments_temp.add(item)

            if (item.nativeImageData.isNotEmpty()) {

                // 该组选中了图片
                upLoadImage(item.nativeImageData)
            }

        }


    }


    /**
     * 多图上传
     */
    @SuppressLint("CheckResult")
    fun upLoadImage(nativeImageData: ArrayList<RefundImage>) {


        val description = RequestBody.create(MediaType.parse("multipart/form-data"), "2")

        val partMap = getPartMap(nativeImageData)

        val imageObservable = ArrayList<Observable<JcsResponse<UploadFileResponse2>>>()


        val uploadMultiImages: Observable<JcsResponse<UploadFileResponse2>> = mRetrofit.uploadMultiImages(description, partMap)

/*
        Observable.zip(
            uploadMultiImages,
            uploadMultiImages,
            uploadMultiImages,
           object :Function3<
                   JcsResponse<UploadFileResponse2>,
                   JcsResponse<UploadFileResponse2>,
                   JcsResponse<UploadFileResponse2>,
                   JcsResponse<ImageZipResponse>>{
               override fun apply(
                   t1: JcsResponse<UploadFileResponse2>,
                   t2: JcsResponse<UploadFileResponse2>,
                   t3: JcsResponse<UploadFileResponse2>,
               ): JcsResponse<ImageZipResponse> {
                   TODO("Not yet implemented")
               }

           })
*/


        val listMerger2 = object :Function<Array<Any>, List<JcsResponse<UploadFileResponse2>>> {
            override fun apply(it: Array<Any>): List<JcsResponse<UploadFileResponse2>> {
                val flatMap = it.flatMap {
                    it as List<JcsResponse<UploadFileResponse2>>
                }

                return flatMap
            }

        }


        val listMerger = Function<Array<Any>, List<JcsResponse<UploadFileResponse2>>> {
            it.flatMap {
                it as List<JcsResponse<UploadFileResponse2>>
            }
        }

        val zip = Observable.zip(imageObservable, listMerger)


    }


    private fun getPartMap(nativeImageData: ArrayList<RefundImage>): Map<String, RequestBody> {
        val map: HashMap<String, RequestBody> = HashMap()
        val imageImageUrls = getImageImageUrls(nativeImageData)

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