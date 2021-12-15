package com.jcs.where.api.response.mall.request

/**
 * Created by Wangsw  2021/12/15 15:37.
 *
 */
class MallOrderCommit {
    var address_id :String? = null
    var specsIds = ArrayList<String>()
    var goods = ArrayList<MallOrderCommitGoodGroup>()
}

class MallOrderCommitGoodGroup {

    var goods = ArrayList<MallOrderCommitGoodItem>()
    var remark: String? = null
    var shop_id = 0
}

class MallOrderCommitGoodItem {
    var good_id = 0
    var num = 0
    var specs_id = 0
    var cart_id :Int? = null
}


class MallCommitResponse{
    var orders = ArrayList<Int>()
}
















