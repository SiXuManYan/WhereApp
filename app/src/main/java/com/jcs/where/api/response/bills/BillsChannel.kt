package com.jcs.where.api.response.bills

/**
 * Created by Wangsw  2022/6/8 16:08.
 *
 */
class BillsChannel {


/*
    {
        "BillerTag": "BALANGA_WATER",
        "Description": "BALANGA WATER DISTRICT ",
        "Category": "Water Utility\r\n",
        "FieldDetails": [{
                            "Tag": "First Field",
                            "Caption": "10-11 Digit Account Number",
                            "Format": "Numeric",
                            "Width": "11"
                        }, {
                            "Tag": "Second Field",
                            "Caption": "Customer Name",
                            "Format": "AlphaNumeric",
                            "Width": "30"
                        }],
        "Remarks": "Payments can be accepted On or Before Due Date. Due Date will only be accepted until 4:00PM. After Due date and With Penalty shall be refer to Balanga Water office.\r\n",
        "Status": true,
        "ServiceCharge": 10
    }*/

    /** 渠道名称 */
    var BillerTag = ""

    /** 渠道描述 */
    var Description = ""

    /** 渠道分类 */
    var Category = ""


    var FieldDetails = ArrayList<FieldDetail>()

    /** 备注 */
    var Remarks = ""

    /** 是否可用（true or false） */
    var Status = false

    /** 渠道服务费（加上充值费用为支付费用） */
    var ServiceCharge = ""

}

class FieldDetail {

    /** 字段 */
    var Tag = ""

    /** 字段说明 */
    var Caption = ""

    /** 格式化 */
    var Format = ""

    /** 最大长度 */
    var Width = 0

    /** 记录用户输入 */
    var nativeUserInput = ""
}

