package com.jcs.where.api.request.store

/**
 * Created by Wangsw  2021/7/6 15:40.
 * 添加商品至商城购物车
 */
class StoreAddCart {

    /**
     * 商品ID
     */
    var good_id = 0

    /**
     * 商品数量（默认1件）
     */
    var good_num = 1

    /**
     * 配送类型（1:自提，2:商家配送）
     */
    var delivery_type = 1


}