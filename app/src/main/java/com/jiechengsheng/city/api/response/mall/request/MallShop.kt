package com.jiechengsheng.city.api.response.mall.request

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Wangsw  2022/1/24 10:48.
 *
 */
class MallShop(): Parcelable {

    var id = 0

    var title =  ""
    var image =  ""
    var logo =  ""
    var desc =  ""
    /** 店铺状态（0-关闭，1-正常） */
    var status = 1

    /** 0 未收藏 1已收藏 */
    var collect_status = 1

    /** 地址 */
    var address = ""

    /** 资质 */
    var qualification:String? = ""

    /** 客服名称 */
    var nickname = ""

    /** 收藏数量 */
    var collect_count = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString().toString()
        image = parcel.readString().toString()
        logo = parcel.readString().toString()
        desc = parcel.readString().toString()
        status = parcel.readInt()
        collect_status = parcel.readInt()
        address = parcel.readString().toString()
        qualification = parcel.readString()
        nickname = parcel.readString().toString()
        collect_count = parcel.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(image)
        dest.writeString(logo)
        dest.writeString(desc)
        dest.writeInt(status)
        dest.writeInt(collect_status)
        dest.writeString(address)
        dest.writeString(qualification)
        dest.writeString(nickname)
        dest.writeInt(collect_count)
    }

    companion object CREATOR : Parcelable.Creator<MallShop> {
        override fun createFromParcel(parcel: Parcel): MallShop {
            return MallShop(parcel)
        }

        override fun newArray(size: Int): Array<MallShop?> {
            return arrayOfNulls(size)
        }
    }


}