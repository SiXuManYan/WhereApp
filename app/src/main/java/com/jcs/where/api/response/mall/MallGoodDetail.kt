package com.jcs.where.api.response.mall

/**
 * Created by Wangsw  2021/12/11 14:39.
 * 商品详情
 */
class MallGoodDetail {

    var id = 0
    var title = ""
    var main_image = ""
    var sub_images = ArrayList<String>()
    var min_price = ""
    var max_price = ""
    var stock = 0
    var sold = 0
    var category_id = 0

    /** html 富文本 */
    var desc = ""

    /** 商品属性 */
    var attribute_list = ArrayList<MallAttribute>()

    /** 商品属性组合信息 */
    var specs = ArrayList<MallSpecs>()

    var shop_id = 0
    var shop_name = ""
    var collect_status = ""


}

class MallAttribute {
    var key = ""
    var value = ArrayList<MallAttributeValue>()
}


class MallAttributeValue {
    var key = ""
    var name = ""

    /**
     * 0 未选中
     * 1 选中
     * 2 不可选
     */
    var nativeIsSelected = 0
}

class MallSpecs {

    var id = 0
    var good_id = 0
    var price = ""
    var stock = 0
    var image = ""
    var specs: HashMap<String, String> = HashMap()
}