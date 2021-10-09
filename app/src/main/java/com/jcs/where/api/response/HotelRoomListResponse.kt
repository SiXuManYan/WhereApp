package com.jcs.where.api.response

import java.math.BigDecimal
import java.util.*

/**
 * create by zyf on 2020/12/27 3:30 PM
 */
class HotelRoomListResponse {

    var id = 0
    var images: ArrayList<String> = ArrayList()
    var name = ""
    var hotel_room_type= ""

    var breakfast_type = 0

    var room_area= ""
    var room_num= 0
    var remain_room_num = 0
    var price :BigDecimal = BigDecimal.ZERO

    /** 是否可取消（1：可取消，2：不可取消） */
    var is_cancel = 0


    var tags: ArrayList<TagsBean> = ArrayList()

    class TagsBean {

        var zh_cn_name = ""
    }
}