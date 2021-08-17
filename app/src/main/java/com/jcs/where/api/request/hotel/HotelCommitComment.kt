package com.jcs.where.api.request.hotel

/**
 * Created by Wangsw  2021/8/17
 * 提交酒店评价
 */
class HotelCommitComment {


    /**
     * "["ssssss","xxxxx"]"
     */
    var images: String? = null
    var content: String? = null
    var order_id: Int = 0
    var hotel_id: Int = 0

    var star: Int = 5


    var comment_travel_type_id = ""

}