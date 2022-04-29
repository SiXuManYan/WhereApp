package com.jcs.where.api.response.mall

/**
 * Created by Wangsw  2022/3/24 15:58.
 *
 */
class MallRefundInfo {

    /** 订单id */
    var id = 0

    /** 单个商品所属订单id */
    var order_id = 0

    var goods = MallOrderGood()

    var cancel_reason = ""

    var cancel_images = ArrayList<String>()

    var money_info = MallRefundMoneyInfo()

    var address_info  = MallRefundAddressInfo()

    /**
     * 0 可申诉
     * 1 不可申诉
     */
    var complaint = 0

    /**
     * 退款账户详情
     */
    var remit_info :RefundMethod? = null

    /** 退款失败原因 */
    var error_reason = ""
}

class MallRefundMoneyInfo {

    /** 退款金额 */
    var refund_money = ""

    /** 商品总价 */
    var goods_total = ""

    /** 商家优惠 */
    var shop_coupon_money = ""

    /** 平台优惠 */
    var order_coupon_money = ""

    /** 退款时间 */
    var refund_time: String? = ""

    /** 售后编号 */
    var serial_number: String? = ""


}


class MallRefundAddressInfo {
    var name = ""
    var tel = ""
    var address = ""
}