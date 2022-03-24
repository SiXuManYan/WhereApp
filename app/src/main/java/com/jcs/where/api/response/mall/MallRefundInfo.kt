package com.jcs.where.api.response.mall

/**
 * Created by Wangsw  2022/3/24 15:58.
 *
 */
class MallRefundInfo {

    /** 订单id */
    var id = 0

    /** 订单状态，自提时：（1：待付款，2：支付审核中，4：待使用，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10：退款审核中（商家），11:商家待收货，12：商家拒绝退货），配送时：（1：待付款，2：支付审核中，3：待发货，4：待收货，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10:退款审核中（商家），11：商家待收货，12：商家拒绝退货） */
    var status = 0


    /** 单个商品所属订单id */
    var order_id = 0

    var goods = MallOrderGood()

    var cancel_reason = ""

    var cancel_images = ArrayList<String>()

    var money_info = MallRefundMoneyInfo()

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


}