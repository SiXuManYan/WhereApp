package com.jcs.where.api.response

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.math.BigDecimal

/**
 * Created by Wangsw  2022/3/3 14:17.
 * 用户券包
 */
 class UserCoupon : MultiItemEntity{


    companion object {

        /** 优惠券 */
        var TYPE_COMMON = 0

        /** 标题 */
        var TYPE_TITLE = 1
    }

    var id: Int = 0
    var name: String = ""
    var start_time: String = ""
    var end_time: String = ""

    /** 优惠券面值 */
    var money: String = ""

    /** 使用门槛金额 */
    var doorsill: String = ""

    /** 使用规则 */
    var rule: String = ""

    /** 1平台券  2店铺券*/
    var couponType: Int = 1

    /** 未使用已使用已过期 */
    var nativeType: Int = 0

    /** 是否选中 */
    var nativeSelected: Boolean = false

    /** 店铺券对应的名称 */
    var shopName: String = ""

    /** 区分列表类型，标题或代金券 */
    var nativeListType = Coupon.TYPE_COMMON

    override val itemType: Int
        get() = nativeListType

}

/**
 * 优惠券列表
 */
class Coupon : MultiItemEntity {


    companion object {

        /** 领券中心 */
        var TYPE_COMMON = 0

        /** 店铺页横向优惠券 */
        var TYPE_FOR_SHOP_PAGE = 1
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

    /** 1平台券  2店铺券*/
    var couponType: Int = 1

    /** 1 已抢光 2正常 */
    var coupon_residue_type = 0

    var shopName = ""

    var nativeListType = TYPE_COMMON

    override val itemType: Int
        get() = nativeListType


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

