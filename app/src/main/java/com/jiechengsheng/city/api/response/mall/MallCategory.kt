package com.jiechengsheng.city.api.response.mall

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Wangsw  2021/12/7 14:35.
 * 新版商城分类
 */
class MallCategory() : Parcelable {
    var id = 0
    var name = ""
    var icon = ""
    var second_level: ArrayList<MallCategory> = ArrayList()
    var nativeIsSelected = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString().toString()
        icon = parcel.readString().toString()
        nativeIsSelected = parcel.readByte() != 0.toByte()
        second_level = parcel.createTypedArrayList(CREATOR)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeByte(if (nativeIsSelected) 1 else 0)
        parcel.writeTypedList(second_level)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MallCategory> {
        override fun createFromParcel(parcel: Parcel): MallCategory {
            return MallCategory(parcel)
        }

        override fun newArray(size: Int): Array<MallCategory?> {
            return arrayOfNulls(size)
        }
    }
}

class MallBannerCategory {
    var childItem = ArrayList<MallCategory>()
}