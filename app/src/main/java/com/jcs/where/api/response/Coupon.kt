package com.jcs.where.api.response

/**
 * Created by Wangsw  2022/3/3 14:17.
 * 用户券包
 */
data class UserCoupon(
    var id: Int = 0,
    var name: String = "",
    var start_time: String = "",
    var end_time: String = "",

    /** 优惠券面值 */
    var money: String = "",

    /** 使用门槛金额 */
    var doorsill: String = "",

    /** 使用规则 */
    var rule: String = "",

    var nativeType :Int= 0

    )