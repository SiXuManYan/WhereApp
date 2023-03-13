package com.jiechengsheng.city.api.response.gourmet.cart

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

/**
 * Created by Wangsw  2021/4/7 11:42.
 */
class Products : Serializable, MultiItemEntity {



    /** 0普通列表 ，1 美食提交订单 */
    var contentType = ShoppingCartResponse.CONTENT_TYPE_COMMON

    override val itemType: Int
        get() = contentType

    var cart_id = 0
    var good_num = 0
    var good_data = GoodData()
    var nativeIsSelect = false
}