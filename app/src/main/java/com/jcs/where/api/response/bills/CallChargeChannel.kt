package com.jcs.where.api.response.bills

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Wangsw  2022/7/6 13:43.
 * 话费充值缴费渠道
 */
class CallChargeChannel() : Parcelable {




    var channelName = ""

    var channelItem = ArrayList<CallChargeChannelItem>()

    constructor(parcel: Parcel) : this() {
        channelName = parcel.readString().toString()
        parcel.createTypedArrayList(CallChargeChannelItem.CREATOR)?.let {
            channelItem = it
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(channelName)
        parcel.writeTypedList(channelItem)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CallChargeChannel> {
        override fun createFromParcel(parcel: Parcel): CallChargeChannel {
            return CallChargeChannel(parcel)
        }

        override fun newArray(size: Int): Array<CallChargeChannel?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * 话费充值渠道数据
 */
class CallChargeChannelItem() : Parcelable {

    var isChecked = false


    /** 金额 */
    var Denomination = ""

    /** 电话公司标签 */
    var TelcoTag = ""

    /** 电话公司名称 */
    var TelcoName = ""

    /** 扩展标签 */
    var ExtTag = ""

    var discount = ""

    constructor(parcel: Parcel) : this() {
        Denomination = parcel.readString().toString()
        TelcoTag = parcel.readString().toString()
        TelcoName = parcel.readString().toString()
        ExtTag = parcel.readString().toString()
        discount = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Denomination)
        parcel.writeString(TelcoTag)
        parcel.writeString(TelcoName)
        parcel.writeString(ExtTag)
        parcel.writeString(discount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CallChargeChannelItem> {
        override fun createFromParcel(parcel: Parcel): CallChargeChannelItem {
            return CallChargeChannelItem(parcel)
        }

        override fun newArray(size: Int): Array<CallChargeChannelItem?> {
            return arrayOfNulls(size)
        }
    }
}