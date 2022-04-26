package com.jcs.where.api.request

/**
 * Created by Wangsw  2022/4/26 15:35.
 *
 */
class CodeVerify {

    /** 1手机号 2 邮箱 */
    var type = 0

    /** 验证码 */
    var verification_code = ""

    /** 手机号 */
    var phone: String? = null

    /** 邮箱 */
    var email: String? = null


}