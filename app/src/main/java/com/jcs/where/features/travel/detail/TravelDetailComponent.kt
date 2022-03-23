package com.jcs.where.features.travel.detail

import android.text.TextUtils
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.TravelCollectionRequest
import com.jcs.where.api.response.travel.TravelDetail
import com.jcs.where.features.hotel.detail.media.MediaData
import java.util.ArrayList

/**
 * Created by Wangsw  2021/10/18 16:16.
 *
 */
interface TravelDetailView :BaseMvpView {
    fun bindDetail(response: TravelDetail, mediaList: ArrayList<MediaData>)
    fun collectionHandleSuccess(collectionStatus: Boolean)
}

class TravelDetailPresenter(private var view: TravelDetailView):BaseMvpPresenter(view){




    fun getData(id: Int) {
        requestApi(mRetrofit.getTravelDetail(id), object : BaseMvpObserver<TravelDetail>(view) {
            override fun onSuccess(response: TravelDetail) {
                val mediaList = ArrayList<MediaData>()

                if (!TextUtils.isEmpty(response.video)) {
                    val media = MediaData().apply {
                        type = MediaData.VIDEO
                        cover = response.video_image
                        src = response.video
                    }
                    mediaList.add(media)
                }
                for (image in response.images) {
                    val media = MediaData().apply {
                        type = MediaData.IMAGE
                        cover = image
                        src = image
                    }
                    mediaList.add(media)
                }
                view.bindDetail(response, mediaList)
            }

        })
    }


    fun collection(id: Int) {
        val apply = TravelCollectionRequest().apply {
            travel_id = id
        }

        requestApi(mRetrofit.travelCollection(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(true)
            }
        })
    }


    fun unCollection(id: Int) {

        requestApi(mRetrofit.travelUnCollection(id), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(false)
            }
        })
    }




}