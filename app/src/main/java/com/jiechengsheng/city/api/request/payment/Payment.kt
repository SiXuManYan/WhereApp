package com.jiechengsheng.city.api.request.payment

/**
 * Created by Wangsw  2022/4/24 14:55.
 *
 */
class PayUrlGet {

    companion object {

        // 模块（hotel: 酒店、eat_in_food: 堂食菜品、take_out_food: 餐厅外卖、bill_pay: 支付账单、estore: Estore）
        var HOTEL = "hotel"
        var RESTAURANT = "eat_in_food"
        var TAKEAWAY = "take_out_food"
        var BILL_PAY = "bill_pay"
        var MALL = "estore"

        /**
         * 一次性支付
         */
        var ONE_TIME_PAYMENT = "ONE_TIME_PAYMENT"

        /**
         * 令牌支付
         */
        var TOKENIZED_PAYMENT = "TOKENIZED_PAYMENT"

    }


    var module: String = ""

    var order_ids = ""
    var payment_channel = "Xendit"
    var version = 2

    /**
     * 支付方式（一次性支付: ONE_TIME_PAYMENT，令牌支付: TOKENIZED_PAYMENT)
     */
    var payment_method = ""

    /**
     * 支付渠道编码
     */
    var channel_code = ""

}


class PayUrl {
    /**
     * 支付重定向Url
     * 当为H5 一次性支付时会返回
     */
    var redirectUrl = ""
}


class PayStatus {
    var pay_status = false
}