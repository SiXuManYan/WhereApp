package com.jcs.where.api.response.order.bill

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/7/20 14:51.
 *
 */

class BillOrderDetails {


    /** 订单ID */
    var id = 0


    /**
     * 订单状态（1-支付审核中，2-缴费中，3-缴费成功，4-订单关闭，5-退款中，6-退款成功）
     */
    var order_status = 0

    var trade_no = ""

    /** 订单总额 */
    var price = BigDecimal.ZERO

    /** 订单时间 */
    var created_at = ""

    /** 支付类型 */
    var pay_type = ""

    /**
     * 银行卡户头
     */
    var bank_card_account = ""

    var bank_card_number = ""

    var account_name = ""

    var present_address = ""
    var contact_no = ""

    /** 到期金额 */
    var amount_due = ""


    var bills_payment :String? = ""

    /**
     * 开账单的人
     */
    var biller = ""

    /**
     * 账户号
     */
    var account_number = ""

    /**
     * 发票编号
     */
    var invoice_no = ""

    /**
     * 账单日期
     */
    var date = ""

    /**
     * 到期日
     */
    var due_date = ""

    /**
     * 报告期
     */
    var statement_date = ""


}