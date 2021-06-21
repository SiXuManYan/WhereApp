package com.jcs.where.api.response.store

import java.io.Serializable
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/21 14:42.
 *
 */
class StoreOrderCommitList : Serializable {

    var shop_id: Int = 0

    var remark: String = ""

    /**
     * 配送方式（1:自提，2:商家配送，3:两者都有）
     */
    var delivery_type: Int = 0


    var goods: ArrayList<StoreGoodsCommit> = ArrayList()

    var shop_title: String = ""

}


class StoreGoodsCommit : Serializable {

    var good_id: Int = 0

    var good_num: Int = 0


    /**
     * 配送类型（1:自提，2:商家配送，3:两者都有）
     */
    var delivery_type: Int = 0
    var price: BigDecimal = BigDecimal.ZERO
    var goodName: String = ""

    var image: String = ""
}