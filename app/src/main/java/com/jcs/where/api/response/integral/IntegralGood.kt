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


class IntegralTag{
    var name = ""

    /**
     * 本地字段，判断是否选中
     */
    var nativeIsSelected = false
}