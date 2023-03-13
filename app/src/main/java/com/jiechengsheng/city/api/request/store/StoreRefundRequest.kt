package com.jiechengsheng.city.api.request.store

/**
 * Created by Wangsw  2021/7/1 10:30.
 *  商城申请售后
 */
class StoreRefundRequest {

    var orderId = 0
    var desc = ""

    /**
     * "["ssssss","xxxxx"]"
     */
    var images: String? = null

}


/**
 * 商城再次申请售后
 */
class StoreRefundModifyRequest {

    var desc = ""

    /**
     * "["ssssss","xxxxx"]"
     */
    var images: String? = null
}


class MallRefundRequest {

    var cancel_reason = ""

    /**
     * "["ssssss","xxxxx"]"
     */
    var cancel_images: String? = null

    /** 打款id */
    var remit_id = 0
}

/**
 * 商城再次申请售后
 */
class MallRefundModifyRequest {

    var desc = ""

    /**
     * "["ssssss","xxxxx"]"
     */
    var images: String? = null
}