package com.jcs.where.api.request

/**
 * Created by Wangsw  2021/6/22 16:29.
 *
 */
class StoreOrderCommit {

    var delivery_type: Int = 0
    var address_id: Int = 0
    var tel: String? = null
    var goods: ArrayList<StoreOrderCommitShop> = ArrayList()

}

class StoreOrderCommitShop {

    var shop_id: Int = 0
    var remark: String = ""
    var goods: ArrayList<StoreOrderCommitGood> = ArrayList()

}

class StoreOrderCommitGood {
    var good_id: Int = 0
    var good_num: Int = 0
}