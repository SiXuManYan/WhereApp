package com.jcs.where.api.response

import java.math.BigDecimal

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

    /** 1平台券 */
    var couponType: Int = 1,

    /** 未使用已使用已过期 */
    var nativeType: Int = 0,

    /** 是否选中 */
    var nativeSelected: Boolean = false,


    )

/**
 * 优惠券列表
 */
class Coupon {


    companion object {
        var TYPE_FINISH = 1
        var TYPE_COMMON = 2

    }


    var id: Int = 0
    var name: String = ""
    var start_time: String = ""
    var end_time: String = ""

    /** 发行数量 */
    var num = BigDecimal.ZERO

    /** 限制数量(每人领取数量) */
    var limit = BigDecimal.ZERO

    /** 领取数量 */
    var get_num = BigDecimal.ZERO


    /** 优惠券面值 */
    var money: String = ""

    /** 使用门槛金额 */
    var doorsill: String = ""

    /** 使用规则 */
    var rule: String = ""

    /** 1平台券 */
    var couponType: Int = 1

    /** 1 已抢光 2正常 */
    var coupon_residue_type = 0


}

class GetCouponResult {
    var message = ""
}

class GeCouponDefault {
    var coupon_id = ""
    var money = ""
    var data = ArrayList<GeCouponDefaultChild>()


}

class GeCouponDefaultChild {
    var shop_id = 0
    var price = BigDecimal.ZERO
}

