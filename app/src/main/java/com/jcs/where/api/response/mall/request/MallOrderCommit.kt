package com.jcs.where.api.response.mall.request

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/15 15:37.
 *
 */


class MallOrderCommit{
    var address_id :String? = null
    var specsIds = ""
    var goods = ""
}



class MallOrderCommitGoodGroup {

    var goods = ArrayList<MallOrderCommitGoodItem>()
    var shop_id = 0
    var remark: String? = null
}

class MallOrderCommitGoodItem {
    var good_id = 0
    var num = 0
    var specs_id = 0
    var cart_id :Int? = null
}


class MallCommitResponse{

    /**
     * 订单 id
     */
    var orders = ArrayList<Int>()
    /**
     * 总价
     */
    var total_price: BigDecimal = BigDecimal.ZERO
}

















