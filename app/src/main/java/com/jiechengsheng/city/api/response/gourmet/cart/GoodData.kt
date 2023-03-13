package com.jiechengsheng.city.api.response.gourmet.cart

import java.io.Serializable
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/4/7 11:47.
 */
class GoodData : Serializable {
    var id = 0
    var title = ""
    var image = ""
    var price = BigDecimal.ZERO
    var original_price = BigDecimal.ZERO

    /**
     * 库存
     */
    var inventory = ""
}