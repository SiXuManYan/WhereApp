package com.jcs.where.api.response.job

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
class ProfileDetail {

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