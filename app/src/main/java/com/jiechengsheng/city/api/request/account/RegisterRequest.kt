package com.jiechengsheng.city.api.request.account

/**
 * Created by Wangsw  2021/1/29 14:34.
 * 注册请求
 */
class RegisterRequest {

    var phone :String? = null
    var password :String? = null

    /**
     * 国家码
     */
    var country_code :String? = null
    var verification_code :String? = null

    var invite_code:String? = null

}