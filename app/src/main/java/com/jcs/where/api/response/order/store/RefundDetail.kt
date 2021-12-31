package com.jcs.where.api.response.order.store

import com.jcs.where.api.response.mall.MallOrderGood
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/7/2 14:07.
 * 售后详情
 */
class RefundDetail {

    var id = 0

    /**
     * 订单状态，自提时：（1：待付款，2：支付审核中，            4：待使用，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10：退款审核中（商家），11:商家待收货，12：商家拒绝退货），
     *           配送时：（1：待付款，2：支付审核中，3：待发货，4：待收货，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10:退款审核中（商家），11：商家待收货，12：商家拒绝退货）
     */
    var status = 0

    var shop: RefundDetailShop? = null

    var goods: ArrayList<StoreOrderShopGoods> = ArrayList()

    /**
     * 退款金额
     */
    var refund_price: BigDecimal = BigDecimal.ZERO

    /**
     * 退款时间
     */
    var cancel_time = ""

    /**
     * 	退款描述
     */
    var cancel_reason = ""

    /**
     * 	退款单号
     */
    var trade_no :Double = 0.0

    var cancel_images: ArrayList<String> = ArrayList()


}


class RefundDetailMall {

    var id = 0

    /**
     * 订单状态，自提时：（1：待付款，2：支付审核中，            4：待使用，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10：退款审核中（商家），11:商家待收货，12：商家拒绝退货），
     *           配送时：（1：待付款，2：支付审核中，3：待发货，4：待收货，5：交易成功，6：订单取消（未支付时取消），7：交易关闭，8：退款中，9：退款成功，10:退款审核中（商家），11：商家待收货，12：商家拒绝退货）
     */
    var status = 0

    var shop: RefundDetailShop? = null

    var goods: ArrayList<MallOrderGood> = ArrayList()

    /**
     * 退款金额
     */
    var refund_price: BigDecimal = BigDecimal.ZERO

    /**
     * 退款时间
     */
    var cancel_time = ""

    /**
     * 	退款描述
     */
    var cancel_reason = ""

    /**
     * 	退款单号
     */
    var trade_no  = ""

    var cancel_images: ArrayList<String> = ArrayList()


}

class RefundDetailShop {

    /**
     * 联系电话
     */
    var contact_cell_phone = ""

    /**
     * 联系人姓名
     */
    var contact_name = ""
    var address = ""

}