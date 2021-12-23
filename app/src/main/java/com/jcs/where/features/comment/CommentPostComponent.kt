package com.jcs.where.features.comment

import com.blankj.utilcode.util.RegexUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.hotel.HotelCommitComment
import com.jcs.where.api.request.hotel.TravelCommitComment
import com.jcs.where.api.request.store.StoreCommitComment
import com.jcs.where.api.response.UploadFileResponse2
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Wangsw  2021/10/19 15:04.
 *
 */

interface CommentView : BaseMvpView {
    fun commitSuccess()
}

class CommentPostPresenter(private var view: CommentView) : BaseMvpPresenter(view) {


    /**
     * 业务id 如：
     * 酒店id
     * 旅游id
     * 订单id（新版商城）
     */
    var targetId = 0

    /**
     * 多图上传
     * @param commentType       0 酒店
     *                          1 旅游
     */
    fun upLoadImage(commentType: Int, orderId: Int, star: Int, contentStr: String, imageUrls: ArrayList<String>) {

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

                when (commentType) {
                    0 -> {
                        postHotelComment(orderId, star, contentStr, descImages)
                    }
                    1 -> {
                        postTravelComment(star, contentStr, descImages)
                    }
                    2 -> {
                        commitMallComment(orderId, star, contentStr, descImages)
                    }

                    else -> {
                    }
                }

            }
        })
    }


    /**
     * 发表酒店评价
     */
    fun postHotelComment(orderId: Int, stars: Int, contentStr: String, imageUrls: String? = null) {

        val apply = HotelCommitComment().apply {
            images = imageUrls
            content = if (contentStr.isEmpty()) {
                null
            } else {
                contentStr
            }
            order_id = orderId
            star = stars
            hotel_id = targetId
        }
        requestApi(mRetrofit.commitHotelComment(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.commitSuccess()
            }

        })

    }

    /**
     * 发表旅游评价
     */
    fun postTravelComment(stars: Int, contentStr: String, imageUrls: String? = null) {

        val apply = TravelCommitComment().apply {
            images = imageUrls
            content = if (contentStr.isEmpty()) {
                ""
            } else {
                contentStr
            }
            star_level = stars
            travel_id = targetId
        }
        requestApi(mRetrofit.commitTravelComment(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.commitSuccess()
            }

        })
    }

    /**
     * mall 商城评价
     */
    fun commitMallComment(orderId: Int, stars: Int, contentStr: String, imageUrls: String?) {

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
        requestApi(mRetrofit.commitMallComment(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.commitSuccess()
            }

        })

    }


}