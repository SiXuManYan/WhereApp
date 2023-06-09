package com.jiechengsheng.city.api.response.mall

import com.jiechengsheng.city.api.response.hotel.HotelComment
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


    var price = BigDecimal.ZERO
    var original_cost = BigDecimal.ZERO

    /** 预估配送费 */
    var estimated_delivery_fee = ""


    var video: String? = null
    var video_image: String? = null

}

class MallAttribute : Serializable {
    var key = ""
    var value = ArrayList<MallAttributeValue>()
    var nativeSelectedValue = ""
}


class MallAttributeValue : Serializable {
    var key = ""
    var name = ""

    /**
     * 0 未选中
     * 1 选中
     * 2 不可选
     * （0代表者正常，1代表选中，2代表不可选（库存为0||无规格））
     */
    var nativeIsSelected = 2

    /**
     * 选中
     * 未选中
     */
    var nativeSelected = false

    /**
     * 是否可用
     */
    var nativeEnable = false

}

class MallSpecs : Serializable {

    var id = 0
    var goods_id = 0
    var specs_id = 0
    var price: BigDecimal = BigDecimal.ZERO
    var stock = 0
    var image = ""
    var specs: HashMap<String, String> = HashMap()
    var original_cost = BigDecimal.ZERO

    /**
     * sku 属性集合
     */
    var nativeSpecsValues = ArrayList<String>()
}

class SkuDataSource {
    var main_image = ""
    var min_price = BigDecimal.ZERO
    var stock = 0

    /** 商品属性 */
    var attribute_list = ArrayList<MallAttribute>()

    /** 商品属性组合信息 */
    var specs = ArrayList<MallSpecs>()


    var original_cost = BigDecimal.ZERO


}