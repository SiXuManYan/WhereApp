package com.jiechengsheng.city.api.response.mall

import android.os.Parcel
import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by Wangsw  2022/4/25 16:35.
 *
 */
class RefundMethod() : Parcelable {

    var id = 0
    var user_name: String? = ""
    var account: String? = ""
    var channel_category: String? = ""
    var name: String? = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        user_name = parcel.readString()
        account = parcel.readString()
        channel_category = parcel.readString()
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(user_name)
        parcel.writeString(account)
        parcel.writeString(channel_category)
        parcel.writeString(name)
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

class RefundChannel  : MultiItemEntity {

    companion object {

        var TYPE_NORMAL  = 0

        var TYPE_TITLE = 1000

    }

    var isSelected = false


    var channel_code = ""
    var channel_category = ""
    var currency = ""
    var name = ""

    var amount_limits: AmountLimits? = null


    /** 类型 0普通 1 分割线  */
    var type = 0

    override val itemType: Int
        get() = type


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