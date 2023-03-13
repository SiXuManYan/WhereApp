package com.jiechengsheng.city.api.request.account

/**
 * Created by Wangsw  2021/2/1 16:37.
 * 绑定手机号
 */
class BindPhoneRequest {
    /**
     * 登录类型（1：Facebook，2：Google，3：Twitter）
     */
    var type = 0

    /**
     * 第三方平台用户ID
     */
    var open_id: String? = null

    /**
     * 昵称
     */
    var nickname: String? = null

    /**
     * 头像
     */
    var avatar: String? = null
    var phone: String? = null
    var country_code: String? = null
    var verification_code: String? = null
    var password: String? = null

    /**
     * 邀请码
     */
    var invite_code: String? = null
}