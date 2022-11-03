package com.jcs.where.api.response.job

import android.os.Parcel
import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by Wangsw  2022/9/27 16:49.
 * 职位列表
 */
class Job {

    var id = 0
    var company = ""
    var job_title = ""
    var salary = ""
    var city = ""
    var created_at = ""
    var job_id = 0
    var logo = ""
}

/**
 * 职位详情
 */
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

    /** 职责和责任 */
    var duty = ""

    /** 要求/资格 */
    var requirement = ""

    var is_collect = false

    var company_info: CompanyInfo? = null

}

class JobSendCv {
    var job_id = 0
}

class ApiVersion {
    var version = 0
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

    /** 出生日期 */
    var birthday = ""

    /** 婚姻状况（0-未婚，1-已婚） */
    var civil_status = 0

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
        birthday = parcel.readString().toString()
        civil_status = parcel.readInt()
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
        parcel.writeString(birthday)
        parcel.writeInt(civil_status)
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
class JobExperience() : Parcelable, MultiItemEntity {


    var id = 0


    var nativeTitleValue = ""

    /**
     * 工作经历 0 ， 教育背景 1 ,标题 1000
     * @see JobExperience.TYPE_JOB_EXPERIENCE
     * @see JobExperience.TYPE_EDU_BACKGROUND
     * @see JobExperience.TYPE_TITLE
     */
    var nativeItemViewType = 0
    var nativeTitleType = 0

    // 工作经历
    var company = ""
    var job_title = ""
    var start_date = ""
    var end_date = ""
    var job_desc = ""
    var city = ""

    // 教育背景


    /** 学校 */
    var educational_attainment = ""

    /** 学历 */
    var educational_level = ""

    /** 专业 */
    var vocational_course = ""


    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        company = parcel.readString().toString()
        job_title = parcel.readString().toString()
        start_date = parcel.readString().toString()
        end_date = parcel.readString().toString()
        job_desc = parcel.readString().toString()
        city = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(company)
        parcel.writeString(job_title)
        parcel.writeString(start_date)
        parcel.writeString(end_date)
        parcel.writeString(job_desc)
        parcel.writeString(city)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JobExperience> {

        /** 工作经历 */
        var TYPE_JOB_EXPERIENCE = 0

        /** 教育背景 */
        var TYPE_EDU_BACKGROUND = 1

        /** 标题 */
        var TYPE_TITLE = 1000


        override fun createFromParcel(parcel: Parcel): JobExperience {
            return JobExperience(parcel)
        }

        override fun newArray(size: Int): Array<JobExperience?> {
            return arrayOfNulls(size)
        }
    }

    override val itemType: Int
        get() = nativeItemViewType
}

/**
 * 创建简历个人信息
 */
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


//    var school = ""
//
//    /** 专业 */
//    var major: String? = null
//
//    /** 学历 */
//    var education = ""


    var birthday = ""

    /** 0未婚 1已婚 */
    var civil_status = 0

}


/**
 * 创建工作经历
 */
class CreateJobExperience {
    var company = ""
    var job_title = ""
    var start_date = ""
    var end_date = ""
    var job_desc = ""
}

/**
 * 职位收藏、取消收藏
 */
class JobCollection {
    var job_id = 0
}


/**
 * 教育背景详情
 */
class EduDet {


    var id = 0

    /** 学校 */
    var educational_attainment = ""

    /** 学历 */
    var educational_level = Degree()

    /** 专业值 */
    var vocational_course: String? = ""

}

/**
 * 学历item
 */
class Degree {

    var id = 0

    /** 学历 */
    var educational_level = ""

    /** 专业标题 */
    var extend_title: String? = ""

    var nativeSelected = false
}


/**
 * 更新教育经历
 */
class EduRequest {

    /** 学校 */
    var educational_attainment = ""

    /** 学历 id */
    var educational_level_id = 0

    /** 专业 */
    var vocational_course: String? = null
}

/**
 * 提交职位雇主申请
 */
class EmployerRequest {
    var first_name = ""
    var middle_name = ""
    var last_name = ""
    var contact_number = ""
    var email = ""
    var init_pwd = ""
    var company_title = ""
}

/**
 * 举报原因列表
 */
class Report {
    var id = 0
    var title = ""
    var nativeIsSelected = false
}

/**
 * 职位举报
 */
class ReportRequest {

    /** 职位id */
    var job_id = 0

    /** 举报原因id */
    var report_title_id = 0
}


/**
 * 公司详情
 */
class CompanyInfo {

    var id = 0
    var company_title = ""
    var logo = ""
    var company_type = ""
    var company_size = ""

    /** 公司简介 */
    var profile = ""
    var address = ""
    var images = ArrayList<String>()
}


class CompanyAlbum {
    var images = ArrayList<String>()
}