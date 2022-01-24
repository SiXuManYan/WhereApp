package com.jcs.where.api.response.mall

/**
 * Created by Wangsw  2022/1/24 14:30.
 *
 */
class MallShopCategory {
    var id = 0
    var name = ""
    var shop_id = 0

    /** 商品数量 */
    var num = 0

    /** 是否有次级分类 */
    var level = ArrayList<MallShopCategory>()
}