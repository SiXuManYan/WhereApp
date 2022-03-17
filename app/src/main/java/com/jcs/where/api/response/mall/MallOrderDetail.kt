package com.jcs.where.api.response.mall

import com.jcs.where.api.response.order.store.StoreOrderAddress
import java.io.Serializable
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/16 15:32.
 *
 */
class MallOrderDetail {


    val im_status = 0
    val aftersale_status = 0
    val mer_uuid = ""
    val mer_name = ""


    /** 订单ID */
    var id = 0

    /** 订单号 */
    var trade_no = ""


    /** 订单备注 */
    var remark = ""

    var delivery_fee = ""

    /** 订单总额 */
    var price = BigDecimal.ZERO

    /** 订单状态（1-待付款，2-支付审核中，3-待发货，4-待收货/待使用，5-交易成功，6-订单取消（未支付时取消），7-交易关闭，8-退款中，9-退款成功，10-退款审核中（商家），11-商家待收货，12-商家拒绝退货） */
    var order_status = 0

    /** 地址信息（配送时） */
    var address: StoreOrderAddress? = null

    var shop_title = ""
    var shop_images = ""
    var bank_card_account = ""
    var bank_card_number = ""

    /** 订单时间 */
    var created_at = ""

    var goods = ArrayList<MallOrderGood>()

    /** 支付渠道 */
    var pay_channel = ""

    /** 电话（自提时） */
    var tel = ""

    /**1支持退货 2不支持退货*/
    var is_cancel = 0

    /** 评论状态（1-未评论，2-已评论，3-已超时） */
    var comment_status = 0

    var comments_id = 0

    /** 物流公司 */
    var company_name = ""

    /** 物流单号 */
    var logistics = ""

    /** 减去优惠金额前的总价 */
    var total_price = ""

    /** 平台优惠金额 */
    var money = ""

    /** 商家优惠金额 */
    var shop_coupon_money = ""




}

class MallOrderGood :Serializable {
    var id = 0
    var order_id = 0
    var good_id = 0
    var good_title = ""
    var good_image = ""
    var good_num = 0
    var good_price = ""
    var good_specs: HashMap<String, String> = HashMap()
    var created_at = ""

    /** 删除状态1已删除 0未删除 */
    var delete_status = 0

    /** 下架 0下架 1上架 */
    var good_status = 0


}