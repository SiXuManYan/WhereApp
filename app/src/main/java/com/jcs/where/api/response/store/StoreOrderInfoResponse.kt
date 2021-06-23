package com.jcs.where.api.response.store

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/23 15:00.
 *
 */
class StoreOrderInfoResponse {

    var orders:ArrayList<StoreOrders> = ArrayList()

    var total_price : BigDecimal = BigDecimal.ZERO

}


class StoreOrders {
    var id = 0
    var trade_no = ""

}