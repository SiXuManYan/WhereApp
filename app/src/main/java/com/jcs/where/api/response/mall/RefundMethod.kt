package com.jcs.where.api.response.mall

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Wangsw  2022/4/25 16:35.
 *
 */
class RefundMethod() : Parcelable {

    var id = 0
    var user_name = ""
    var channel_name = ""
    var account = ""
    var bank_name = ""
    var bank_all_name = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        user_name = parcel.readString()!!
        channel_name = parcel.readString()!!
        account = parcel.readString()!!
        bank_name = parcel.readString()!!
        bank_all_name = parcel.readString()!!
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
    var name = ""
}


class RefundBankSelected {
    var abbr = ""
    var all = ""
    var isSelected = false
}

class RefundBindRequest {

    /** 用户名 */
    var user_name = ""

    /** 渠道名 */
    var channel_name = ""

    /** 账号 */
    var account = ""

    /** 银行缩写 */
    var bank_name: String? = null

    /** 银行全名 */
    var bank_all_name: String? = null
}