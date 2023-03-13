package com.jiechengsheng.city.api.response.gourmet.restaurant

import com.jiechengsheng.city.api.response.gourmet.dish.DishResponse
import com.jiechengsheng.city.api.response.hotel.HotelComment
import java.util.ArrayList

/**
 * Created by Wangsw  2021/4/1 11:15.
 * 餐厅详情
 */
class RestaurantDetailResponse {
    var id = ""
    var uuid = ""
    var title = ""
    var images = ArrayList<String>()
    var video = ""
    var video_image = ""
    var grade = 0f
    var comment_num: String? = null
    var per_price = ""
    var address = ""
    var start_time = ""
    var end_time = ""
    var lat = 0.0
    var lng = 0.0
    var distance = ""
    var tel = ""

    /**
     * 收藏状态（1：未收藏，2：已收藏）
     */
    var collect_status = 0
    var take_out_status = 0
    var mer_uuid = ""
    var mer_name = ""

    /**
     * IM聊天开启状态（1：开启，2：关闭）
     */
    var im_status = 0
    var introduction = ""
    var website = ""
    var facebook = ""
    var email = ""

    var comments:ArrayList<HotelComment> = ArrayList()
    var goods : ArrayList<DishResponse> = ArrayList()

}