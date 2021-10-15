package com.jcs.where.features.hotel.detail

import android.text.TextUtils
import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.HotelRoomListResponse
import com.jcs.where.api.response.hotel.HotelDetail
import com.jcs.where.hotel.activity.detail.MediaData
import java.util.*

/**
 * Created by Wangsw  2021/10/8 11:34.
 *
 */

interface HotelDetailView : BaseMvpView {
    fun bindDetail(response: HotelDetail, mediaList: ArrayList<MediaData>)

    /**
     * 	收藏状态（1：已收藏，2：未收藏）
     */
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
        requestApi(mRetrofit.postCollectHotel(infoId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(true)
            }
        })
    }

    fun unCollection(infoId: Int) {

        requestApi(mRetrofit.delCollectHotel(infoId), object : BaseMvpObserver<JsonElement>(view) {
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