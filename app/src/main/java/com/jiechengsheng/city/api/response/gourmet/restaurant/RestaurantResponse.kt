package com.jiechengsheng.city.api.response.gourmet.restaurant

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.math.BigDecimal
import java.util.ArrayList

/**
 * Created by Wangsw  2021/3/25 11:18.
 * 餐厅列表
 */
class RestaurantResponse  : MultiItemEntity {

    companion object {
        /** 0普通列表 */
        val CONTENT_TYPE_COMMON = 0
        /** 1外层增加 CardView */
        val CONTENT_TYPE_CARD = 1
    }

    var id = 0
    var images : ArrayList<String> = ArrayList()
    var title = ""
    var grade = 0f
    var comment_num = 0
    var per_price: BigDecimal = BigDecimal.ZERO
    var distance = 0f
    var type: String = ""
    var trading_area: String = ""
    var tags = ArrayList<String>()
    var take_out_status = 0
    var lat = 0.0
    var lng = 0.0

    /** 0普通列表 ，1 外层增加 CardView */
    var contentType = CONTENT_TYPE_COMMON

    override val itemType: Int
        get() = contentType

}