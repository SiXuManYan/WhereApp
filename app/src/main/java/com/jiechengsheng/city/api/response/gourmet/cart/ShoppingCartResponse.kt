package com.jiechengsheng.city.api.response.gourmet.cart

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jiechengsheng.city.api.response.hotel.HotelHomeRecommend
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * Created by Wangsw  2021/4/7 11:23.
 */
class ShoppingCartResponse : Serializable, MultiItemEntity {


    companion object {

        /** 0 美食购物车布局 */
        val CONTENT_TYPE_COMMON = 0

        /** 1 美食提交订单 */
        val CONTENT_TYPE_COMMIT = 1
    }


    var restaurant_id = ""
    var restaurant_name = ""
    var products = ArrayList<Products>()
    var nativeIsSelect = false
    var nativeTotalPrice: BigDecimal? = null

    /** 0普通列表 ，1 美食提交订单 */
    var contentType = HotelHomeRecommend.CONTENT_TYPE_COMMON

    override val itemType: Int
        get() = contentType
}