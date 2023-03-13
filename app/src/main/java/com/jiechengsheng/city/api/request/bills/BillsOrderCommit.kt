package com.jiechengsheng.city.api.request.bills

import java.io.Serializable

/**
 * Created by Wangsw  2021/6/29 14:50.
 *  水电提交订单
 */
class BillsOrderCommit : Serializable {

    var bill_type = 0

    var account_name = ""
    var account_number = ""
    var present_address = ""

    /**
     * 联系电话
     */
    var contact_no = ""

    /**
     * 付款方式
     */
    val payment_method = "Bills Payment"

    /**
     * 开账单的人
     */
    var biller = ""

    /**
     * 到期金额
     */
    var amount_due = ""


    /**
     * 发票编号
     */
    var invoice_no = ""


    /**
     * 总金额
     */
    var total_amount = ""

    /**
     * 备注
     */
    val remark = ""


    /**
     * 账单日期
     */
    var date = ""


    /**
     * 终止日期
     */
    var due_date = ""


    /**
     * 报告日期
     */
    var statement_date = ""


    /**
     * 电力公司（PENELCO 、 AFAB GREENCORE POWER SOLUTION (Transco)）
     */
    var electricity_company: String? = null


}