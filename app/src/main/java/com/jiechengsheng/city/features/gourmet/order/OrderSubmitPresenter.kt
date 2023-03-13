package com.jiechengsheng.city.features.gourmet.order

import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.gourmet.cart.Products
import com.jiechengsheng.city.api.response.gourmet.cart.ShoppingCartResponse
import com.jiechengsheng.city.api.response.gourmet.order.FoodOrderSubmitData
import com.jiechengsheng.city.bean.OrderSubmitChildRequest
import com.jiechengsheng.city.bean.OrderSubmitRequest
import com.jiechengsheng.city.utils.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/4/20 16:54.
 *
 */
class OrderSubmitPresenter(val view: OrderSubmitView) : BaseMvpPresenter(view) {


    fun submitOrder(allCart: MutableList<ShoppingCartResponse>, phoneStr: String) {

        val allProduct = getAllProduct(allCart)



        if (phoneStr.length < 9 || phoneStr.length>15) {
            ToastUtils.showShort(R.string.food_wrong_number)
            return
        }


        val goodIds = ArrayList<OrderSubmitChildRequest>()

        allProduct.forEach {
            val apply = OrderSubmitChildRequest().apply {
                good_id = it.good_data.id
                good_num = it.good_num
                cart_id = it.cart_id
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