package com.jiechengsheng.city.api.response.hotel

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/8/5 15:42.
 * 酒店准备下单
 */
class HotelOrderCommitResponse {

    /**
     * 	订单信息
     */
    var order: HotelOrderCommitOrder? = null


    /**
     * 	订单总价格
     */
    var total_price: BigDecimal = BigDecimal.ZERO

}

/**
 * 	订单信息
 */
class HotelOrderCommitOrder {

    /**
     * ID
     */
    var id = 0

    /**
     * 订单号
     */
    var trade_no = ""
}