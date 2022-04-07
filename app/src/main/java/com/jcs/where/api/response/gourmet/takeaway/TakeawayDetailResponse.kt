package com.jcs.where.api.response.gourmet.takeaway

import com.jcs.where.api.response.gourmet.dish.DishResponse
import java.math.BigDecimal
import java.util.ArrayList


/**
 * Created by Wangsw  2021/4/25 14:56.
 *
 */
class TakeawayDetailResponse {

/*    "data": {
        "id": 1,
        "restaurant_name": "Spencer, Schowalter and Mayer",
        "take_out_image": "https://whereoss.oss-cn-beijing.aliyuncs.com/restaurants/Winter_Garden_127.jpg",
        "delivery_time": 30,
        "delivery_cost": 3,
        "packing_charges": 2,
        "tel": "13130033471",
        "collect_status": 1,
        "mer_uuid": "",
        "mer_name": "",
        "im_status": 1
    }*/

    var id = 0

    /** 餐厅名称 */
    var restaurant_name: String = ""

    /** 外卖餐厅图片 */
    var take_out_image: String = ""

    /** 配送时间 */
    var delivery_time = 0

    /** 配送费用 */
    var delivery_cost: BigDecimal = BigDecimal.ZERO

    /** 包装费 */
    var packing_charges: BigDecimal = BigDecimal.ZERO

    var tel: String = ""

    /** 商家uuid */
    var mer_uuid: String = ""

    /** 商家名称 */
    var mer_name: String = ""

    /** 收藏状态（1：未收藏，2：已收藏） */
    var collect_status = 0

    /** IM聊天开启状态（1：开启，2：关闭） */
    var im_status = 0

    var goods : ArrayList<DishResponse> = ArrayList()

}