package com.jiechengsheng.city.api.response.bills

/**
 * Created by Wangsw  2022/10/12 10:24.
 *
 */
class BillsCoupon {

    var id = 0
    var name = ""
    var start_time = ""
    var end_time = ""

    /** 优惠券金额 */
    var money = ""

    /** 使用门槛金额 */
    var doorsill = ""

    /** 规则 */
    var rule = ""

    /** 店铺id 0代表平台 大于0店铺id */
    var shop_id = ""

    /** 1 平台优惠券 2 店铺优惠券 3 支付账单 */
    var couponType = ""

    /** 支付账单优惠券类型( 2水 3电 4网 5手机充值) */
    var bill_type = ""

    var nativeSelected = false

}