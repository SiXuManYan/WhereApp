package com.jcs.where.api.response.hotel

/**
 * Created by Wangsw  2021/8/17 13:51.
 *
 */
class HotelComment {

    var id = 0
    var user_id = 0
    var images:ArrayList<String> = ArrayList()
    var star = 0.0f
    var created_at = ""


    /**
     * 评论类型
     */
    var comment_travel_type_id = 0

    var username = ""
    var avatar = ""
    var content = ""
    var star_level = 0.0f


    /** 酒店评论含有 */
    var hotel_id = 0

    /** 美食评论 */
    var merchant_review = ""

}