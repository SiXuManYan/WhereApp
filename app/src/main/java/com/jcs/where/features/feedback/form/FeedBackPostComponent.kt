package com.jcs.where.features.feedback.form

import com.blankj.utilcode.util.RegexUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.UploadFileResponse2
import com.jcs.where.api.response.feedback.FeedbackPost
import com.jcs.where.api.response.feedback.FeedbackRecord
import com.jcs.where.api.response.integral.IntegralGood
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Wangsw  2022/10/13 16:33.
 *
 */
interface FeedBackPostView :BaseMvpView {
    fun commitSuccess(){}
    fun bindRecord(toMutableList: MutableList<FeedbackRecord>, lastPage: Boolean){}

}


class FeedBackPostPresenter(private var view: FeedBackPostView):BaseMvpPresenter(view){


    fun upLoadImage(contentStr: String, phone:String , imageUrls: ArrayList<String>) {

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

                postFeedback(contentStr  , phone , descImages)
            }
        })
    }

    fun postFeedback(contentStr: String, phone:String,  imageUrls: String?  ){

        val apply = FeedbackPost().apply {
            images = imageUrls
            this.content = contentStr
            this.tel = phone
        }
        requestApi(mRetrofit.postFeedback(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.commitSuccess()
            }

        })



    }

    fun getRecord(page: Int) {
        requestApi(mRetrofit.feedbackRecord(page ) ,object :BaseMvpObserver<PageResponse<FeedbackRecord>>(view){
            override fun onSuccess(response: PageResponse<FeedbackRecord>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindRecord(data.toMutableList(), isLastPage)
            }

        })
    }


}