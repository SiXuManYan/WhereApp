package com.jcs.where.api.response.store

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/23 15:00.
 *
 */
class StoreOrderInfoResponse {

    /**
     * 订单信息
     */
    var orders: ArrayList<StoreOrders> = ArrayList()

    /**
     * 总价
     */
    var total_price: BigDecimal = BigDecimal.ZERO

}


class StoreOrders {

    /**
     * 订单id
     */
    var id = 0

    /**
     * 	订单号
     */
    var trade_no = ""

}