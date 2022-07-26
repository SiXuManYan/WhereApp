package com.jcs.where.features.mall.sku.other

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jcs.where.R
import com.jcs.where.features.mall.sku.bean.Sku
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

    companion object {
        operator fun get(context: Context): Product {
            val json = context.getString(R.string.product)
            return Gson().fromJson(json, object : TypeToken<Product?>() {}.type)
        }
    }
}