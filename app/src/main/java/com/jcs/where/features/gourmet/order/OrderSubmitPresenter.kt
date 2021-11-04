package com.jcs.where.features.gourmet.order

import com.google.gson.Gson
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.gourmet.cart.Products
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.api.response.gourmet.order.FoodOrderSubmitData
import com.jcs.where.bean.OrderSubmitChildRequest
import com.jcs.where.bean.OrderSubmitRequest
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.FeaturesUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/4/20 16:54.
 *
 */
class OrderSubmitPresenter(val view: OrderSubmitView) : BaseMvpPresenter(view) {


    fun submitOrder(allCart: MutableList<ShoppingCartResponse>, phoneStr: String) {

        val allProduct = getAllProduct(allCart)


        if (FeaturesUtil.isWrongPhoneNumber("63", phoneStr)) {
            return
        }

        val goodIds = ArrayList<OrderSubmitChildRequest>()

        allProduct.forEach {
            val apply = OrderSubmitChildRequest().apply {
                good_id = it.good_data.id
                good_num = it.good_num
            }
            goodIds.add(apply)
        }

        val request = OrderSubmitRequest().apply {
            goods = Gson().toJson(goodIds)
            phone = phoneStr
        }

        requestApi(mRetrofit.orderSubmit(request), object : BaseMvpObserver<FoodOrderSubmitData>(view) {


            override fun onSuccess(response: FoodOrderSubmitData) {
                view.summitSuccess(response)
            }
        })

    }


    fun convertList(oldList: ArrayList<ShoppingCartResponse>): ArrayList<ShoppingCartResponse> {

        val data: ArrayList<ShoppingCartResponse> = ArrayList()

        oldList.forEach {

            it.contentType = ShoppingCartResponse.CONTENT_TYPE_COMMIT
            it.products.forEach { product ->
                product.contentType = ShoppingCartResponse.CONTENT_TYPE_COMMIT
            }

            it.nativeTotalPrice = getChildTotalPrice(it.products)
        }
        data.addAll(oldList)
        return data
    }

    private fun getChildTotalPrice(productList: ArrayList<Products>): BigDecimal {
        var totalPrice: BigDecimal = BigDecimal.ZERO
        productList.forEach {
            val price = it.good_data.price
            val goodNum = it.good_num
            val currentItemPrice = BigDecimalUtil.mul(price, BigDecimal(goodNum))
            totalPrice = BigDecimalUtil.add(currentItemPrice, totalPrice)
        }
        return totalPrice
    }


    fun getAllProduct(oldList: MutableList<ShoppingCartResponse>): ArrayList<Products> {
        val data: ArrayList<Products> = ArrayList()
        oldList.forEach {
            data.addAll(it.products)
        }
        return data


    }


}