package com.jcs.where.api.response.hotel

import java.math.BigDecimal
import java.util.*

/**
 * Created by Wangsw  2021/9/26 10:44.
 *
 */
class HotelHomeRecommend {


    var id = 0
    var images = ArrayList<String>()
    var name = ""
    var grade: Float = 0f
    var comment_counts = ""
    var address = ""
    var lat = 0.0
    var lng = 0.0
    var price: BigDecimal = BigDecimal.ZERO
    var distance = ""
    var remain_room_num = ""
    var facebook_link = ""
    var star_level = ""

    var tags = ArrayList<String>()
}