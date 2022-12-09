package com.jcs.where.api.response.bills

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Wangsw  2022/12/8 16:49.
 *
 */
class BillAccount() : Parcelable {

    var id = 0

    /** 第一行 */
    var first_field = ""

    /** 第二行 */
    var second_field = ""

    /** 1为默认账号 */
    var status = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        first_field = parcel.readString().toString()
        second_field = parcel.readString().toString()
        status = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(first_field)
        parcel.writeString(second_field)
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BillAccount> {
        override fun createFromParcel(parcel: Parcel): BillAccount {
            return BillAccount(parcel)
        }

        override fun newArray(size: Int): Array<BillAccount?> {
            return arrayOfNulls(size)
        }
    }

}


class BillAccountEdit {


    /** 第一行 */
    var first_field = ""

    /** 第二行 */
    var second_field = ""

    /** 1为默认账号 */
    var status = 0

    /** 1 自来水 2电力公司 3互联网 */
    var module = 0
}

