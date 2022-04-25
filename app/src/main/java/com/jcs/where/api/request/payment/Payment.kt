package com.jcs.where.api.request.payment

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

    }


    var module: String = ""

    var order_ids = ""

}


class PayUrl {
    var redirectUrl = ""
}


class PayStatus {
    var pay_status = false
}