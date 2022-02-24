package com.jcs.where.api.response.mall

import com.chad.library.adapter.base.entity.MultiItemEntity

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


class MallShopRecommend {

    var img = ""
    var goods_id = 0

    /**
     * 跳转类型
     */
    var type = 0

}


class ShopRecommend : MultiItemEntity {

    companion object {
        var TYPE_CARD = 0
        var TYPE_BANNER = 1
    }

    var recommend = ArrayList<MallShopRecommend>()


    override val itemType: Int
        get() = if (recommend.size > 1) {
            TYPE_BANNER
        } else {
            TYPE_CARD
        }


}


