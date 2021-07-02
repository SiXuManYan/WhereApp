package com.jcs.where.api.request.store

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
    var images:String? = null

}

/**
 * 商城再次申请售后
 */
class StoreRefundModifyRequest {

    var desc = ""

    /**
     * "["ssssss","xxxxx"]"
     */
    var images:String? = null

}
