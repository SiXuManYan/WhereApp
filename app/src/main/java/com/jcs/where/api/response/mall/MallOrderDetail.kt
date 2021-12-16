package com.jcs.where.api.response.mall

import com.jcs.where.api.response.order.store.StoreOrderAddress
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/16 15:32.
 *
 */
class MallOrderDetail {


    /** 订单ID */
    var id = 0

    /** 订单号 */
    var trade_no = ""



    /** 订单备注 */
    var remark = ""

    /** 订单总额 */
    var price = BigDecimal.ZERO

    var order_status = 0

    /** 地址信息（配送时） */
    var address: StoreOrderAddress? = null

    var shop_title = ""
    var shop_images = ""
    var bank_card_account = ""
    var bank_card_number = ""

    /** 订单时间 */
    var created_at = ""




}