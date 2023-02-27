package com.jcs.where.api.response.job

import android.os.Parcel
import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by Wangsw  2022/9/27 16:49.
 * 职位列表
 */
class Job : MultiItemEntity {


    companion object {


        // 列表类型

        /** 简历列表 */
        val TYPE_COMMON_JOB = 0

        /** 收藏列表 */
        val TYPE_COLLETION_JOB = 1

        /** 申请列表 */
        val TYPE_RECORD_APPLIED = 3

        /** 面试列表 */
        val TYPE_RECORD_INTERVIEWS = 4

        /** 标题 */
        val TYPE_TITLE = 5


        // **** 列表请求类型 ***
        /** 投递列表 */
        var REQUEST_APPLIED = 0

        /** 面试列表 */
        var REQUEST_INTERVIEWS = 1

        // *** 列表状态 ***
        /** 职位正常 */
        val STATUS_NORMAL = 1

        /** 职位关闭 */
        val STATUS_CLOSED = 0

        /** 1已申请 */
        val STATUS_APPLIED = 1

        /** 2申请失败 */
        val STATUS_APPLIED_FAILED = 2

        /** 3待面试 */
        val STATUS_TO_INTERVIEWS = 3

        /** 4面试成功 */
        val STATUS_INTERVIEWS_SUCCEED = 4


        /** 5面试失败 */
        val STATUS_INTERVIEWS_FAILED = 5


    }


    var id = 0
    var company = ""
    var job_title = ""
    var salary = ""
    var city = ""
    var created_at = ""
    var job_id = 0
    var logo = ""
    var tag = ArrayList<String>()

    /**
     *  简历列表、收藏列表：
     *  1正常 0 关闭
     *
     *
     *  申请列表，面试列表
     *  1已申请 2申请失败 3待面试 4面试成功 5面试失败
     */
    var status = 0

    /**  0不限 1月薪 2日薪 3时薪 4面议薪资 */
    var salary_type = 0


    /**
     * 列表类型
     */
    var nativeListType = 0

    override val itemType: Int
        get() = nativeListType

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

    /** 是否完善附件简历（true、false） */
    var is_complete_pdf = false

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

    /** 头像 */
    var avatar = ""

    /** pdf 生成时间 */
    var pdf_time:String? = ""

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
        avatar = parcel.readString().toString()
        pdf_time = parcel.readString().toString()
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
        parcel.writeString(avatar)
        parcel.writeString(pdf_time)
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
 * 工作经历、教育背景、资格证书
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

    /** 工作经历、教育背景开始时间 */
    var start_date = ""

    /** 工作经历、教育背景结束时间 */
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



    // 简历证书
    /** 证书名称 */
    var title = ""

    /** 证书图片 数组 */
    var images = ArrayList<String>()


    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()

        nativeTitleValue = parcel.readString().toString()
        nativeItemViewType = parcel.readInt()
        nativeTitleType = parcel.readInt()

        // 工作经历
        company = parcel.readString().toString()
        job_title = parcel.readString().toString()
        start_date = parcel.readString().toString()
        end_date = parcel.readString().toString()
        job_desc = parcel.readString().toString()
        city = parcel.readString().toString()

        // 教育背景
        educational_attainment = parcel.readString().toString()
        educational_level = parcel.readString().toString()
        vocational_course = parcel.readString().toString()

        // 简历证书
        title = parcel.readString().toString()
        parcel.createStringArrayList()?.let { images.addAll(it) }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)

        parcel.writeString(nativeTitleValue)
        parcel.writeInt(nativeItemViewType)
        parcel.writeInt(nativeTitleType)

        // 工作经历
        parcel.writeString(company)
        parcel.writeString(job_title)
        parcel.writeString(start_date)
        parcel.writeString(end_date)
        parcel.writeString(job_desc)
        parcel.writeString(city)

        // 教育背景
        parcel.writeString(educational_attainment)
        parcel.writeString(educational_level)
        parcel.writeString(vocational_course)

        // 简历证书
        parcel.writeString(title)
        parcel.writeStringList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JobExperience> {

        /** 工作经历 */
        var TYPE_JOB_EXPERIENCE = 0

        /** 教育背景 */
        var TYPE_EDU_BACKGROUND = 1

        /** 资格证书 */
        var TYPE_CERTIFICATION = 2

        /** 标题 */
        var TYPE_TITLE = 1000




        /** 工作经历 PDF*/
        var TYPE_JOB_EXPERIENCE_PDF = 4

        /** 教育背景 PDF*/
        var TYPE_EDU_BACKGROUND_PDF = 5

        /** 资格证书 PDF*/
        var TYPE_CERTIFICATION_PDF = 6

        /** 标题 PDF*/
        var TYPE_TITLE_PDF = 2000





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

    var avatar: String? = ""

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

    /** 开始时间 */
    var start_date = ""

    /** 结束时间 */
    var end_date = ""

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

    /** 开始时间 */
    var start_date = ""

    /** 结束时间 */
    var end_date = ""
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

class EmployerEmail {
    var email = ""
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
    var profile: String? = ""
    var address = ""
    var images: ArrayList<String>? = ArrayList()

    var website: String? = ""
}


class CompanyAlbum {
    var images = ArrayList<String>()
}


class JobTag {
    var nativeIsSelected = false
}


class JobFilter {

    var area = ArrayList<FilterItem>()
    var companyType = ArrayList<FilterItem>()
    var jobResumeEducationLevel = ArrayList<FilterItem>()
    var experience = ArrayList<FilterItem>()
}

class FilterItem {
    var id = 0
    var name = ""
    var nativeSelected = false
}


class FilterData {

    /** 薪资类型 0不限 1月薪 2年薪 3时薪*/
    var salaryType = 0

    /** 最低薪水 */
    var minSalary = ""

    /** 最高薪水 */
    var maxSalary = ""

    /** 地区 */
    var areas = ArrayList<Int>()

    /** 公司类型 */
    var companyTypes = ArrayList<Int>()

    /** 学历 */
    var eduLevel = ArrayList<Int>()

    /** 工作经验 */
    var experienceLevel = ArrayList<Int>()


    var salaryData = ArrayList<FilterItem>()
    var areaData = ArrayList<FilterItem>()
    var companyTypeData = ArrayList<FilterItem>()
    var eduData = ArrayList<FilterItem>()
    var experienceData = ArrayList<FilterItem>()


}

class JobNotice {
    var status = false
}


/**
 * 创建简历资格证书
 */
class CreateCertificate {

    var title = ""

    /**
     * "["ssssss","xxxxx"]"
     */
    var images = ""
}


/**
 * 检查简历完整性
 */
class CheckResume {
    var is_complete  = false
}