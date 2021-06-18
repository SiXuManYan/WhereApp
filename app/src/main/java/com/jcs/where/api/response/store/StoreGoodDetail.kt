package com.jcs.where.api.response.store

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/18 17:44.
 *
 */
data class StoreGoodDetail(

/*{
    "delivery_times": "1 day",
    "take_times": "all day",
    "delivery_type": 2,
    "inventory": 52
}*/


        var id: Int,
        var title: String,
        var delivery_times: String,
        var take_times: String,
        var desc: String,
        var price: BigDecimal = BigDecimal.ZERO,
        var original_price: BigDecimal = BigDecimal.ZERO,
        var images: ArrayList<String> = ArrayList(),
        var sale_num: Int,


        /**
         * 配送类型（1:自提，2:商家配送，3:两者都有）
         */
        var delivery_type: Int,
        var inventory: Int
)
