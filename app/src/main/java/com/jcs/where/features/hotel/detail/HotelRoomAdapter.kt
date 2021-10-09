package com.jcs.where.features.hotel.detail

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.HotelRoomListResponse

/**
 * Created by Wangsw  2021/10/9 16:17.
 *
 */
class HotelRoomAdapter : BaseQuickAdapter<HotelRoomListResponse, BaseViewHolder>(R.layout.item_hotel_room) {
    

    override fun convert(holder: BaseViewHolder, item: HotelRoomListResponse) {

    }
}