package com.jcs.where.api.response.hotel

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/10/12 14:53.
 * 酒店房间详情
 */
class RoomDetail {

    var id = 0
    var images: ArrayList<String> = ArrayList()
    var name = ""
    var hotel_room_type = ""
    var room_area = ""
    var floor = ""
    var policy = ""
    var shower_room = ""
    var facility = ""
    var media = ""
    var food = ""
    var scene = ""
    var other = ""
    var cancel_time = ""
    var breakfast_type = 0
    var window_type = 0
    var wifi_type = 0
    var people = 0
    var room_num = 0
    var remain_room_num = 0
    var is_cancel = 0
    var price :BigDecimal = BigDecimal.ZERO
    var facilities: ArrayList<Facilities> = ArrayList()


}