package com.jcs.where.features.hotel.detail.room

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.hotel.RoomDetail
import com.jcs.where.features.hotel.detail.media.MediaData
import java.util.ArrayList

/**
 * Created by Wangsw  2021/10/12 10:41.
 *
 */
interface RoomDetailView : BaseMvpView {
    fun bindDetail(response: RoomDetail, mediaList: ArrayList<MediaData>)

}

class RoomDetailPresenter(private var view: RoomDetailView) : BaseMvpPresenter(view) {

    fun getDetail(roomId: Int, roomNumber: Int, startDate: String, endDate: String) {
        requestApi(mRetrofit.getHotelRoomDetail(roomId,roomNumber,startDate,endDate),object :BaseMvpObserver<RoomDetail>(view){
            override fun onSuccess(response: RoomDetail) {
                val mediaList = ArrayList<MediaData>()

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

}