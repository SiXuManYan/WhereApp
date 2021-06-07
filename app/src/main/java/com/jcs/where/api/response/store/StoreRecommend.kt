package com.jcs.where.api.response.store

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/1 16:11.
 *
 */
data class StoreRecommend(

        var id: Int,
        var images: ArrayList<String> = ArrayList(),
        var title: String,
        var address: String,
        var lat: Double,
        var lng: Double,
        var distance: String,

        var tags: ArrayList<String> = ArrayList(),
        var grade: Float,

        /**
         * 配送方式（1:自提，2:商家配送，3:两者都有）
         */
        var delivery_type: Int,

        var goods: ArrayList<StoreGood> = ArrayList()

)

data class StoreGood(

        var id: Int,

        /** 销量 */
        var sale_num: Int,

        /** 库存 */
        var inventory: Int,

        var title: String,
        var images: ArrayList<String> = ArrayList(),
        var price: BigDecimal = BigDecimal.ZERO,
        var original_price: BigDecimal = BigDecimal.ZERO,

        /**
         * 配送方式（1:自提，2:商家配送，3:两者都有）
         */
        var delivery_type: Int

)

