package com.jcs.where.api.response.mall

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Wangsw  2022/4/25 16:35.
 *
 */
class RefundMethod() : Parcelable {

    var id = 0
    var user_name: String? = ""
    var channel_name: String? = ""
    var account: String? = ""
    var bank_name: String? = ""
    var bank_all_name: String? = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        user_name = parcel.readString()
        channel_name = parcel.readString()
        account = parcel.readString()
        bank_name = parcel.readString()
        bank_all_name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(user_name)
        parcel.writeString(channel_name)
        parcel.writeString(account)
        parcel.writeString(bank_name)
        parcel.writeString(bank_all_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RefundMethod> {
        override fun createFromParcel(parcel: Parcel): RefundMethod {
            return RefundMethod(parcel)
        }

        override fun newArray(size: Int): Array<RefundMethod?> {
            return arrayOfNulls(size)
        }
    }

}

class RefundChannel {
    var isSelected = false
    var isWidthSplit = false


    var channel_code = ""
    var channel_category = ""
    var currency = ""
    var name = ""

    var amount_limits: AmountLimits? = null


}


class AmountLimits {
    var minimum = ""
    var maximum = ""
    var minimum_increment = ""

}


class RefundBankSelected {
    var abbr = ""
    var all = ""
    var isSelected = false
}

class RefundBindRequest {

    /** 用户名 */
    var user_name = ""


    /** 账号 */
    var account = ""

    /** 渠道码 */
    var channel_code = ""

    /** 渠道分类 */
    var channel_category = ""

    /** 银行全拼 */
    var name: String = ""
}

class RemitId {
    var remit_id = 0
}

class FoodRefundInfo {

    companion object {
        var TYPE_FOOD = 1
        var TYPE_TAKEAWAY = 2
    }

    var price = ""
    var total_price = ""
    var cancel_time = ""

    /**
     * 退款账户详情
     */
    var remit_info: RefundMethod? = null


}