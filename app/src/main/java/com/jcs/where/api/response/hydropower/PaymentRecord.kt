package com.jcs.where.api.response.hydropower

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/28 16:40.
 * 水电缴费记录
 */
class PaymentRecord {

    /**
     * 	订单ID
     */
    var id = 0

    /**
     * 	账单类型（1-水费，2-电费）
     */
    var bill_type = 0

    var price :BigDecimal = BigDecimal.ZERO

    /**
     * 订单状态（1-支付审核中，2-缴费中，3-缴费成功，4-订单关闭，5-退款中，6-退款成功）
     */
    var order_status = 0

    /**
     * 账户
     */
    var trade_no = ""


    var created_at = ""



}