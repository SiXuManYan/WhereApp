package com.jcs.where.api.response.mall

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/14 18:26.
 *
 */
class MallCartGroup {


    /** 店铺id */
    var shop_id = 0

    /** 店铺名称  */
    var title = ""

    var gwc = ArrayList<MallCartItem>()

    /** 本地记录是否选中 */
    var nativeIsSelect = false

}

class MallCartItem {

    /** 本地记录是否选中 */
    var nativeIsSelect = false

    var id = 0
    var cart_id = 0
    var good_id = 0
    var specs_id = 0
    var good_num = 1
    var created_at = ""
    var updated_at = ""
    var good_specs = ""

    /** 商品详情 */
    var goods_info: MallGoodInfo? = null
    var specs_info: MallSpecsInfo? = null

}

/** 商品详情 */
class MallGoodInfo {
    var title = ""
    var photo = ""
}

class MallSpecsInfo {
    var specs: HashMap<String, String> = HashMap()
    var price : BigDecimal = BigDecimal.ZERO
    var stock = 0
}