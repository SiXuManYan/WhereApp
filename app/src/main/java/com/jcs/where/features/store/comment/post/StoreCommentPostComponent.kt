package com.jcs.where.features.store.comment.post

import com.blankj.utilcode.util.RegexUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.store.StoreCommitComment
import com.jcs.where.api.response.UploadFileResponse2
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Wangsw  2021/7/14 14:43.
 *
 */
interface StoreCommentPostView : BaseMvpView {
    fun commitSuccess()
}


class StoreCommentPostPresenter(private var view: StoreCommentPostView) : BaseMvpPresenter(view) {


    /**
     * 多图上传
     */
    fun upLoadImage(orderId: Int, star: Int, contentStr: String, imageUrls: ArrayList<String>) {

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
                commitStoreComment(orderId, star, contentStr, descImages)
            }
        })
    }


    fun commitStoreComment(orderId: Int, stars: Int, contentStr: String, imageUrls: String?) {

        val apply = StoreCommitComment().apply {
            images = imageUrls
            content = if (contentStr.isEmpty()) {
                null
            } else {
                contentStr
            }
            order_id = orderId
            star = stars
        }
        requestApi(mRetrofit.commitStoreComment(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.commitSuccess()
            }

        })

    }


}