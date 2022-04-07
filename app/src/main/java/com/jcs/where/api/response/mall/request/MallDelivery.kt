package com.jcs.where.api.response.mall.request

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/29 18:37.
 *
 */


class MallDeliveryRequest {
    var city_id = 0
//    var goods = ArrayList<ArrayList<MallDeliverItem>>()
    var goods = ""
}

class MallDeliverItem {
    var shop_id = 0
    var goods_id = 0
    var count = 0
}

class MallDeliveryResponse {
    var delivery_fee = HashMap<String, BigDecimal>()
}

class MallDeliveryResponseItem {
//    var "?" : 0
//    var "?" : 0
//    var "total" : 00
}