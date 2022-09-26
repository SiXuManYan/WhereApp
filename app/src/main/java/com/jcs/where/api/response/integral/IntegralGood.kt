package com.jcs.where.api.response.integral

/**
 * Created by Wangsw  2022/9/21 11:42.
 * 积分商品
 */
class IntegralGood {
/*

    "id": 1404,
    "title": "电费优惠券",
    "image": "https://whereoss.oss-accelerate.aliyuncs.com/images/HZUph5KxfdYIHhiHBZtalXdTpahxTroixg6L1AF0.jpg",
    "price": "200",
    "show_status": 1

*/

    var id = 0
    var title = ""
    var image = ""
    var price = ""

    /** 0正常 1无库存 2即将上线 */
    var show_status = 0


}


class IntegralTag {
    var name = ""

    /**
     * 本地字段，判断是否选中
     */
    var nativeIsSelected = false
}


class IntegralGoodDetail {

    var id = 0
    var title = ""
    var image = ""
    var price = ""
    var stock = 0

    /**  1：商品 其他：优惠券 */
    var type = 0


    var start_time = ""
    var end_time = ""

    /** 0积分不足 1可以兑换 2超出限制  3即将上线 */
    var operation_status = 0

    var user_integral = ""
    var desc = ""

}

class IntegralPlaceOrderResponse {

    var orders = ""
}


class IntegralOrderDetail {

    var id = 0

}


class IntegralRecord {




    var id = 0
    var price = ""
    var title = ""
    var image = ""
    var order_status = 0

    var nativeType = 0

}