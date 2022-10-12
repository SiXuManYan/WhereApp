package com.jcs.where.api.response.bills

import java.math.BigDecimal

/**
 * Created by Wangsw  2022/9/16 14:53.
 * 账单缴费 获取账单折扣，以及最终支付价格
 */
class BillsOrderDiscount {
//    "price": "1",
//    "discounts_price": 0,
//    "discount": ""

    /** 最终支付价格 */
    var price = ""

    /** 折扣价格 */
    var discounts_price  = ""

    /** 折扣描述 */
    var discount = ""

    /** 用户提示 */
    var hint  :String ? = null


    /** 优惠券id */
    var coupon_id = 0

    /** 优惠券金额 */
    var coupon_price = ""


}