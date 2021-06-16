package com.jcs.where.api.response.store

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/16 16:45.
 *
 */
data class StoreGoods (


        var id: Int,
        var sale_num: Int,
        var inventory: Int,
        var delivery_type: Int,
        var price:BigDecimal = BigDecimal.ZERO,
        var original_price:BigDecimal = BigDecimal.ZERO,
        var title: String = "",
        var images: ArrayList<String> = ArrayList(),
)
