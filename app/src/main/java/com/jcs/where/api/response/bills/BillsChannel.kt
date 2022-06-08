package com.jcs.where.api.response.bills

/**
 * Created by Wangsw  2022/6/8 16:08.
 *
 */
class BillsChannel {

    /** 渠道名称 */
    var BillerTag = ""

    /** 渠道描述 */
    var Description = ""

    /** 渠道分类 */
    var Category = ""


    var FieldDetails = FieldDetail()

    /** 备注 */
    var Remarks = ""

    /** 是否可用（true or false） */
    var Status = false

    /** 渠道服务费（加上充值费用为支付费用） */
    var ServiceCharge = ""

}

class FieldDetail {

    var Tag = ""
    var Caption = ""


    var Format = ""

    /** 最大长度 */
    var Width = ""
}