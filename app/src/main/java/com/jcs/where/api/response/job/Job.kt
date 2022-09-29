package com.jcs.where.api.response.job

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Wangsw  2022/9/27 16:49.
 *
 */
class Job {

    var id = 0
    var company = ""
    var job_title = ""
    var salary = ""
    var city = ""
    var created_at = ""
}


class JobDetail {

    var id = 0
    var company = ""
    var job_title = ""

    /** 薪资 */
    var salary = ""
    var city = ""
    var created_at = ""


    var job_desc = ""
    var company_desc = ""

    /** 是否发送（true、false） */
    var is_send = false


    /** 是否完善简历（true、false） */
    var is_complete = false

}

class JobSendCv {
    var job_id = 0
}


/**
 * 简历个人信息
 */
class ProfileDetail() : Parcelable {

    var id = 0

    /** 性别（0-未知，1-男，2-女） */
    var gender = 0

    /** 姓 */
    var first_name = ""

    /** 名 */
    var last_name = ""
    var city = ""
    var email = ""
    var contact_number = ""
    var school = ""

    /** 专业 */
    var major = ""

    /** 学历 */
    var education = ""

    var city_id = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        gender = parcel.readInt()
        first_name = parcel.readString().toString()
        last_name = parcel.readString().toString()
        city = parcel.readString().toString()
        email = parcel.readString().toString()
        contact_number = parcel.readString().toString()
        school = parcel.readString().toString()
        major = parcel.readString().toString()
        education = parcel.readString().toString()
        city_id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(gender)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(city)
        parcel.writeString(email)
        parcel.writeString(contact_number)
        parcel.writeString(school)
        parcel.writeString(major)
        parcel.writeString(education)
        parcel.writeInt(city_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileDetail> {
        override fun createFromParcel(parcel: Parcel): ProfileDetail {
            return ProfileDetail(parcel)
        }

        override fun newArray(size: Int): Array<ProfileDetail?> {
            return arrayOfNulls(size)
        }
    }

}

/**
 * 工作经历列表
 */
class JobExperience {

    var id = 0
    var company = ""
    var job_title = ""
    var start_date = ""
    var end_date = ""
    var job_desc = ""
}

class CreateProfileDetail {
    var id = 0

    /** 性别（0-未知，1-男，2-女） */
    var gender = 0

    /** 姓 */
    var first_name = ""

    /** 名 */
    var last_name = ""
    var city_id = 0
    var email = ""
    var contact_number = ""
    var school = ""

    /** 专业 */
    var major: String? = null

    /** 学历 */
    var education = ""
}