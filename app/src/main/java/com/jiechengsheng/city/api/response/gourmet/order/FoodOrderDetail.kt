package com.jiechengsheng.city.api.response.gourmet.order

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/5/10 10:04.
 * 美食订单详情
 */
data class FoodOrderDetail(
    var order_data: OrderData,
    var restaurant_data: RestaurantData,
    var good_data: GoodData,
    var payment_data: PaymentData,

    )

data class OrderData(

        /** 订单id */
        var id: Int,

        /** 订单号 */
        var trade_no: String,

        /** 手机号 */
        var phone: String,

        /** 有效期开始时间 */
        var start_date: String = "",

        /** 有效期结束时间 */
        var end_date: String = "",

        /** 订单创建时间 */
        var created_at: String = "",

        /** 订单状态（1-待支付，2-支付审核中，3-交易取消，4-交易关闭，5-待使用，6-交易成功，7-退款中，8-退款成功）*/
        var status: Int,

        /** 券码号 */
        var coupon_no: String,

        /** 券码到期时间 */
        var coupon_expire: String,

        /** 券码二维码 */
        var coupon_qr_code: String,

        /** 评论状态（1：未评论，2：已评论） */
        var comment_status: Int = 0,

        /** 退款失败原因 */
        var error_reason: String = "",

        /** 订单总价格 */
        var price: BigDecimal = BigDecimal.ZERO,

        /** 0 未投诉 */
        var complaint_status :Int = 0

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
        var im_status: Int = 0,

        var tel: String = "",

        /** 订单总价格 */
        var price: BigDecimal = BigDecimal.ZERO,

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

        /** 商品价格 */
        var price: BigDecimal = BigDecimal.ZERO,

        )

data class PaymentData(
    /** 支付渠道 */
    var payment_channel: String = "",

    /** 支付银行户头 */
    var bank_card_account: String = "",

    /** 支付银行卡号 */
    var bank_card_number: String = "",
)

