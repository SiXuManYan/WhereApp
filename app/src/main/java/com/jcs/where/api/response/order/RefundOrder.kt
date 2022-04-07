package com.jcs.where.api.response.order

import com.jcs.where.api.response.mall.MallOrderGood

/**
 * Created by Wangsw  2022/3/26 10:28.
 *
 */
class RefundOrder {

    var shop_id = 0
    var shop_title = ""

    var good_info = RefundOrderGood()

    var status = 0

}


class RefundOrderGood {
    var order_id = 0
    var good_id = 0
    var good_title = ""
    var good_image = ""
    var good_num = 0
    var good_price = ""
    var good_specs = ""
    var created_at = ""

    /** 删除状态1已删除 0未删除 */
    var delete_status = 0

    /** 下架 0下架 1上架 */
    var good_status = 0

    // 退款新增
    var specs_id = 0

    /** 订单中的商品状态  1 待售后 2 商家审核中 3商家待收货 4商家拒绝退货 5退款中 6退款成功 7取消售后 */
    var status = 0

    /** 1代表可以进行操作 2不可进行操作 */
    var order_good_status = 0

    /** 售后ID */
    var refund_id = 0

    /** 退款金额 */
    var refund_money = ""
}