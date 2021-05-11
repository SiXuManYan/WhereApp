package com.jcs.where.api.response.gourmet.order

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/5/10 10:04.
 * 美食订单详情
 */
data class FoodOrderDetail(
        var order_data: OrderData,
        var restaurant_date: RestaurantData,
        var good_data: GoodData

)

data class OrderData(

        /** 订单id */
        var id: Int,

        /** 订单号 */
        var trade_no: String,

        /** 手机号 */
        var phone: String,

        /** 有效期开始时间 */
        var start_date: String,

        /** 有效期结束时间 */
        var end_date: String,

        /** 订单创建时间 */
        var created_at: String,

        /** 订单状态（1：待付款，2：已取消，3：待使用，4 已完成 ， 5支付失败 ， 6退款中，7已退款，8退款失败，9待评价*/
        var status: Int,

        /** 券码号 */
        var coupon_no: String,

        /** 券码到期时间 */
        var coupon_expire: String,

        /** 券码二维码 */
        var coupon_qr_code: String

)


data class RestaurantData(

        /** 订单id */
        var id: Int,

        /** 餐厅名称 */
        var name: String,

        /** 商家uuid */
        var mer_uuid: String,

        /** 商家名称 */
        var mer_name: String,

        /** IM聊天开启状态（1：开启，2：关闭） */
        var im_status: Int = 0

)

data class GoodData(

        /** 订单id */
        var id: Int,

        /** 商品数量 */
        var good_num: Int,

        /** 商品名称 */
        var name: String,

        /** 商品图片 */
        var good_image: String,

        /** 订单总价格 */
        var price: BigDecimal = BigDecimal.ZERO

)

