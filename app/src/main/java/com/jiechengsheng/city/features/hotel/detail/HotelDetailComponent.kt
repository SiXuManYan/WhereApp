package com.jiechengsheng.city.features.hotel.detail

import android.text.TextUtils
import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.HotelCollectionRequest
import com.jiechengsheng.city.api.response.HotelRoomListResponse
import com.jiechengsheng.city.api.response.hotel.HotelDetail
import com.jiechengsheng.city.features.hotel.detail.media.MediaData
import java.util.*

/**
 * Created by Wangsw  2021/10/8 11:34.
 *
 */

interface HotelDetailView : BaseMvpView {
    fun bindDetail(response: HotelDetail, mediaList: ArrayList<MediaData>)
    fun collectionHandleSuccess(collectionStatus: Boolean)
    fun bindRoom(toMutableList: MutableList<HotelRoomListResponse>)
}


class HotelDetailPresenter(private var view: HotelDetailView) : BaseMvpPresenter(view) {

    fun getData(hotelId: Int) {
        requestApi(mRetrofit.hotelDetail(hotelId), object : BaseMvpObserver<HotelDetail>(view) {
            override fun onSuccess(response: HotelDetail) {
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


    fun collection(infoId: Int) {

        val apply = HotelCollectionRequest().apply {
            hotel_id = infoId
        }

        requestApi(mRetrofit.postCollectHotel(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(true)
            }
        })
    }

    fun unCollection(infoId: Int) {
        val apply = HotelCollectionRequest().apply {
            hotel_id = infoId
        }
        requestApi(mRetrofit.delCollectHotel(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(false)
            }
        })
    }


    fun getRoomList(hotelId: Int, startDate: String, endDate: String, roomNum: Int) {

        requestApi(mRetrofit.getHotelRooms(hotelId,startDate,endDate,roomNum),object :BaseMvpObserver<ArrayList<HotelRoomListResponse>>(view){
            override fun onSuccess(response: ArrayList<HotelRoomListResponse>) {
                view.bindRoom(response.toMutableList())
            }

        })
    }


}