package com.jiechengsheng.city.api.response.hotel

/**
 * Created by Wangsw  2021/10/11 14:47.
 *
 */
class HotelDetail {


    var id = 0
    var name = ""
    var start_business_time = ""
    var address = ""
    var lat = 0.0
    var lng = 0.0
    var tel = ""
    var grade = 0.0f
    var comment_counts = 0
    var policy: Policy? = null

    /**
     * 	收藏状态（1：已收藏，2：未收藏）
     */
    val collect_status = 0

    /**
     * IM聊天开启状态（1：开启，2：关闭
     */
    var im_status = 0
    var facebook_link = ""


    var desc = ""
    var images: ArrayList<String> = ArrayList()
    var facilities: ArrayList<Facilities> = ArrayList()

    /**
     * 视频链接
     */
    var video = ""

    /**
     * 视频地址
     */
    var video_image = ""

    /**
     * 酒店星级
     */
    var star_level =""


    var uuid = ""
    var mer_name = ""

    var comments:ArrayList<HotelComment> = ArrayList()

}

class Facilities {
    var name = ""
    var icon = ""
}

class Policy {
    var check_in_time = ""
    var check_out_time = ""
    var children = ""
    var pet = ""
    var hint = ""
    var payment = ""
    var breadfast = ""
    var service_desc = ""
}