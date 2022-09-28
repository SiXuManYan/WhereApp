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