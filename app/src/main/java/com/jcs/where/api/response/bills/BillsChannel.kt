package com.jcs.where.api.response.bills

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.math.BigDecimal

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
    var ServiceCharge = BigDecimal.ZERO

}

class FieldDetail() :Parcelable {

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

    constructor(parcel: Parcel) : this() {
        Tag = parcel.readString().toString()
        Caption = parcel.readString().toString()
        Format = parcel.readString().toString()
        Width = parcel.readInt()
        nativeUserInput = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Tag)
        parcel.writeString(Caption)
        parcel.writeString(Format)
        parcel.writeInt(Width)
        parcel.writeString(nativeUserInput)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FieldDetail> {
        override fun createFromParcel(parcel: Parcel): FieldDetail {
            return FieldDetail(parcel)
        }

        override fun newArray(size: Int): Array<FieldDetail?> {
            return arrayOfNulls(size)
        }
    }
}


class BillsPlaceOrder{


    /** 账单类型（1-话费，2-水费，3-电费，4-网费） */
    var bill_type = 0
    var amount = ""

    // 水电网费
    var biller_tag :String ?= null
    var first_field :String ?= null
    var second_field :String ?= null

    // 话费
    var telco :String ?= null
    var cellphone_no :String ?= null
    var ext_tag :String ?= null


}

class BillsRecord {


    companion object {

        // 账单类型
        val TYPE_PHONE = 1
        val TYPE_WATER = 2
        val TYPE_ELECTRICITY = 3
        val TYPE_NET = 4

    }

    var id = 0

    /** 账单类型（1-话费，2-水费，3-电费，4-网费） */
    var order_type = 0


    var total_price = BigDecimal.ZERO

    /** 订单状态（0-待支付，1-缴费中，2-缴费成功，3-缴费失败，4-订单关闭，5-退款审核中，6-拒绝退款，7-退款中，8-退款成功，9-退款失败） */
    var order_status = 0

    var created_at = ""

    /** 退款价格 */
    var refund_price = BigDecimal.ZERO

}

class BillCancelOrder {
    var order_id = 0

    var remit_id = 0
}

class BillRecommit {
    var order_id = 0
}

class BillStatus {

    /** 缴费状态 */
    var transaction_status = false
}

