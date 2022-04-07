package com.jcs.where.api.response.mall

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/14 18:26.
 *
 */
class MallCartGroup : Serializable, MultiItemEntity {


    /** 店铺id */
    var shop_id = 0

    /** 店铺名称  */
    var title: String? = ""

    var gwc = ArrayList<MallCartItem>()

    /** 本地记录是否选中 */
    var nativeIsSelect = false

    /** 编辑是否选中 */
    var nativeIsSelectEdit = false

    /** 按钮是否可用 */
    var nativeEnable = true


    /** 本地记录，通过城市id额外获取的 当前店铺配送费 */
    var nativeShopDelivery: BigDecimal? = null
    var nativeRemark = ""


    var delivery_fee: BigDecimal? = BigDecimal.ZERO

    /** 0 普通商品 1 失效商品 */
    var nativeIsNormalType = 0


    override val itemType: Int
        get() = nativeIsNormalType

    /** 失效商品 */
    var nativeExpiredData = ArrayList<MallExpired>()

    /** 优惠的平台券金额 */
    @Deprecated("")
    var nativeCouponPrice = BigDecimal.ZERO

    /** 优惠的店铺券金额 */
    var nativeShopCouponPrice = BigDecimal.ZERO

    /** 优惠的店铺券id */
    var nativeShopCouponId: Int? = null


}

class MallCartItem : Serializable {

    /** 本地记录是否选中 */
    var nativeIsSelect = false

    /** 按钮是否可用 */
    var nativeEnable = true

    /** 编辑是否选中 */
    var nativeIsSelectEdit = false

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


    /** 所有的商品属性组合信息 */
    var specs = ArrayList<MallSpecs>()


}

/** 商品详情 */
class MallGoodInfo : Serializable {
    var title: String? = ""
    var photo = ""

    /** 库存数 ： 0库存不足 1 正常 */
//   var good_stock = 1
    /** 商品属性 */
    var attribute_list = ArrayList<MallAttribute>()
    var min_price = ""
    var max_price = ""
    var main_image = ""
    var good_stock = 0

}

class MallSpecsInfo : Serializable {
    var specs: HashMap<String, String> = HashMap()
    var price: BigDecimal = BigDecimal.ZERO
    var stock = 0


    /** SKU是否被删除  0未删除 1已删除 */
    var delete_status = 0
}