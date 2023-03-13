package com.jiechengsheng.city.api.response.travel

import com.jiechengsheng.city.api.response.hotel.HotelComment

/**
 * Created by Wangsw  2021/10/19 13:50.
 *
 */
class TravelDetail  {


    var id  = 0
    var im_status  = 0
    var uuid  = ""
    var name  = ""
    var address  = ""
    var start_time  = ""
    var end_time  = ""
    var content  = ""
    var notice  = ""
    var phone  = ""
    var mer_uuid  = ""
    var mer_name  = ""
    var images: ArrayList<String> = ArrayList()
    var lat = 0.0
    var lng = 0.0
    var grade = 0.0f
    var comments_count = 0



    /**
     * 视频链接
     */
    var video = ""

    /**
     * 视频地址
     */
    var video_image = ""

    var comments:ArrayList<HotelComment> = ArrayList()



    /**
     * 	收藏状态（1：已收藏，2：未收藏）
     */
    val is_collect = 0



}