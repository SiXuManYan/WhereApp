package com.jcs.where.api.response.gourmet.order

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/5/11 17:50.
 *
 */
data class TakeawayOrderDetail(
        var order_data: TakeawayOrderData,
        var restaurant_date: TakeawayRestaurantData,
        var good_data: ArrayList<TakeawayGoodData> = ArrayList()
)


data class TakeawayOrderData(
        /** 订单id */
        var id: Int,
        var delivery_time_type: Int,
        var delivery_time: String,
        var address: TakeawayAddress,
        var trade_no: String,
        var delivery_cost: BigDecimal =BigDecimal.ZERO,
        var packing_charges: BigDecimal =BigDecimal.ZERO,
        var price: BigDecimal = BigDecimal.ZERO,
        var status: Int
)


data class TakeawayAddress(
        var sex: Int,
        var address: String,
        var contact_name: String,
        var contact_number: String
)

data class TakeawayRestaurantData(

        /** 订单id */
        var id: Int,

        /** 餐厅名称 */
        var name: String,

        /** 商家uuid */
        var mer_uuid: String,

        /** 商家名称 */
        var mer_name: String,

        /** IM聊天开启状态（1：开启，2：关闭） */
        var im_status: Int = 0,

        var tel: String
)


data class TakeawayGoodData(


        /** 商品数量 */
        var good_num: Int,

        /** 商品名称 */
        var name: String,

        /** 商品图片 */
        var good_image: String,

        /** 订单总价格 */
        var good_price: BigDecimal = BigDecimal.ZERO

)