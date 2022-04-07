package com.jcs.where.features.gourmet.comment.post

import com.blankj.utilcode.util.RegexUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.hotel.FoodCommitComment
import com.jcs.where.api.response.UploadFileResponse2
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Wangsw  2021/8/17 17:26.
 *
 */

interface FoodCommentPostView : BaseMvpView {
    fun commitSuccess()
}



class FoodCommentPostPresenter (private var view: FoodCommentPostView) : BaseMvpPresenter(view) {


    var restaurantId = 0

    /**
     * 评价类型（1：堂食菜品，2：外卖）
     */
    var commmentType = 0


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

        val apply = FoodCommitComment().apply {
            images = imageUrls
            content = if (contentStr.isEmpty()) {
                null
            } else {
                contentStr
            }
            order_id = orderId
            star = stars
            restaurant_id = restaurantId
            type = commmentType
        }
        requestApi(mRetrofit.commitFoodComment(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.commitSuccess()
            }

        })

    }
}