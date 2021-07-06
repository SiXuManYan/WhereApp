package com.jcs.where.api.response.store.cart

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/7/5 10:39.
 * 商城购物车
 */
class StoreCartResponse {

    /** 自提商品 */
    var take_carts: ArrayList<StoreCartGroup> = ArrayList()

    /** 配送商品 */
    var delivery_carts: ArrayList<StoreCartGroup> = ArrayList()

}

/**
 * 购物车组
 */
class StoreCartGroup {

    /** 餐厅ID */
    var shop_id = 0
    var shop_name = ""
    var delivery_fee: BigDecimal = BigDecimal.ZERO
    var goods: ArrayList<StoreCartItem> = ArrayList()

    /** 本地记录是否选中 */
    var nativeIsSelect = false

}

/**
 * 购物车子项
 */
class StoreCartItem {

    /** 购物车ID */
    var cart_id = 0
    var good_num = 0
    var good_data: StoreCart = StoreCart()

    /** 本地记录是否选中 */
    var nativeIsSelect = false
}


/**
 * 购物车商品
 */
class StoreCart {

    /** 商品ID */
    var id = 0
    var title = ""
    var images: ArrayList<String> = ArrayList()

    /** 商品价格 */
    var price: BigDecimal = BigDecimal.ZERO

    /** 商品原价 */
    var original_price: BigDecimal = BigDecimal.ZERO

    /** 商品库存（""：不受库存数量限制，"0":库存为空） */
    var inventory = ""


}