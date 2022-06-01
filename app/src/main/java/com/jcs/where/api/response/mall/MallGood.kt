package com.jcs.where.api.response.mall

import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/7 14:52.
 * 商城商品列表
 */
class MallGood {

    var id = 0
    var title = ""
    var main_image = ""
    var sub_images = ArrayList<String>()
    var price = BigDecimal.ZERO
    var stock = 0
    var sold = ""
    var category_id = ""

    var original_cost = BigDecimal.ZERO

    var shop_name = ""
    var shop_id = 0


}