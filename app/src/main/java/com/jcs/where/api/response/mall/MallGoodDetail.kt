package com.jcs.where.api.response.mall

import com.jcs.where.api.response.hotel.HotelComment
import java.io.Serializable
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/11 14:39.
 * 商品详情
 */
class MallGoodDetail {

    var id = 0
    var title = ""
    var main_image = ""
    var sub_images = ArrayList<String>()
    var min_price:String? = ""
    var max_price = ""
    var stock = 0
    var sold = 0
    var category_id = 0

    /** html 富文本 */
    var desc: String? = ""

    /** 商品属性 */
    var attribute_list = ArrayList<MallAttribute>()

    /** 商品属性组合信息 */
    var specs = ArrayList<MallSpecs>()

    var shop_id = 0

    var shop_name: String? = ""

    /** 0未收藏 1收藏 */
    var collect_status = 0
    var delivery_fee: BigDecimal = BigDecimal.ZERO
    var website = ""
    var mer_name = ""
    var mer_uuid = ""
    var im_status = 0

    /** 评论数 */
    var count = 0
    var comments = ArrayList<HotelComment>()


}

class MallAttribute: Serializable {
    var key = ""
    var value = ArrayList<MallAttributeValue>()
}


class MallAttributeValue : Serializable{
    var key = ""
    var name = ""

    /**
     * 0 未选中
     * 1 选中
     * 2 不可选
     */
    var nativeIsSelected = 0
}

class MallSpecs : Serializable{

    var id = 0
    var goods_id = 0
    var specs_id = 0
    var price: BigDecimal = BigDecimal.ZERO
    var stock = 0
    var image = ""
    var specs: HashMap<String, String> = HashMap()
}

class SkuDataSource {
    var main_image = ""
    var min_price :String?= ""
    var stock = 0
    /** 商品属性 */
    var attribute_list = ArrayList<MallAttribute>()

    /** 商品属性组合信息 */
    var specs = ArrayList<MallSpecs>()


}