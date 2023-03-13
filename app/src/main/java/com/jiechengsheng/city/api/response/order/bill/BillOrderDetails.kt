package com.jiechengsheng.city.api.response.order.bill

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/7/20 14:51.
 *
 */

class BillOrderDetails {


    /** 账单类型（1-话费，2-水费，3-电费，4-网费） */
    var order_type = 0

    /** 订单ID */
    var id = 0


    /**
     * 订单状态（0-待支付，1-缴费中，2-缴费成功，3-缴费失败，4-订单关闭，5-退款审核中，6-拒绝退款，7-退款中，8-退款成功，9-退款失败）
     */
    var order_status = 0

    /**
     * 总价
     */
    var total_price = BigDecimal.ZERO

    /**
     * 账单价
     */
    var bills_price = BigDecimal.ZERO

    /**
     * 退款价格
     */
    var refund_price = BigDecimal.ZERO

    /** 服务费 */
    var service_price  = ""

    /** 订单号 */
    var trade_no = ""

    /** 创建时间 */
    var created_at = ""

    /** 支付方式 */
    var payment_method = ""

    /** 失败原因 */
    var refund_refuse_reason = ""

    var bills_params = ArrayList<BillsField>()

    /** 0无1折扣2满减 */
    var type = 0

    /** 优惠金额 */
    var discounts = ""

    /** 优惠券金额 */
    var coupon_price = ""



}




class BillsField {
    var key = ""
    var value = ""
}