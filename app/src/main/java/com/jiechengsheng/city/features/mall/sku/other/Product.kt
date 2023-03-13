package com.jiechengsheng.city.features.mall.sku.other

import com.jiechengsheng.city.features.mall.sku.bean.Sku
import java.math.BigDecimal

/**
 * Created by liufei on 2017/11/30.
 */
class Product {

    var id = 0
    var title = ""
    var main_image = ""
    var price = BigDecimal.ZERO
    var original_cost = BigDecimal.ZERO
    var measurementUnit = ""
    var sold = 0
    var stock = 0
    var skus = ArrayList<Sku>()

}