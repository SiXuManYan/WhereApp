package com.jcs.where.features.gourmet.cart

import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.request.CartDeleteRequest
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.utils.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/4/7 14:47.
 */
class ShoppingCartPresenter(val view: ShoppingCartView) : BaseMvpPresenter(view) {


    fun getData(page: Int) {
        requestApi(mRetrofit.getShoppingCartList(page), object : BaseMvpObserver<PageResponse<ShoppingCartResponse>>(view) {
            override fun onSuccess(response: PageResponse<ShoppingCartResponse>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                val toMutableList = data.toMutableList()

                view.bindList(toMutableList, isLastPage)
            }
        })
    }

    /**
     * 处理价格
     */
    fun handlePrice(adapter: ShoppingCartAdapter): BigDecimal {

        var totalPrice: BigDecimal = BigDecimal.ZERO
        adapter.data.forEachIndexed { _, data ->
            data.products.forEach {
                if (it.nativeIsSelect) {
                    val price = it.good_data.price
                    val goodNum = it.good_num
                    val currentItemPrice = BigDecimalUtil.mul(price, BigDecimal(goodNum))
                    totalPrice = BigDecimalUtil.add(currentItemPrice, totalPrice)
                }
            }
        }

        return totalPrice
    }

    fun getSelectedList(adapter: ShoppingCartAdapter): ArrayList<ShoppingCartResponse> {

        val list = ArrayList<ShoppingCartResponse>()

        adapter.data.forEach {

            if (it.nativeIsSelect) {
                list.add(it)

            }else{
                val parent = ShoppingCartResponse().apply {
                    restaurant_id = it.restaurant_id
                    restaurant_name = it.restaurant_name
                }

                it.products.forEach { child ->
                    if (child.nativeIsSelect) {
                        parent.products.add(child)
                    }
                }
                if (parent.products.isNotEmpty()) {
                    list.add(parent)
                }
            }



        }
        return list
    }


    /**
     * 顶级是否全部选中
     */
    fun checkSelectAll(adapter: ShoppingCartAdapter): Boolean {
        val result = ArrayList<Boolean>()
        result.clear()
        adapter.data.forEach { data ->
            result.add(data.nativeIsSelect)
            data.products.forEach {
                result.add(it.nativeIsSelect)
            }
        }
        return !result.contains(false)
    }

    /**
     * 处理全选和非全选
     */
    fun handleSelectAll(adapter: ShoppingCartAdapter, select: Boolean) {

        adapter.data.forEach {
            it.nativeIsSelect = select
            it.products.forEach { child ->
                child.nativeIsSelect = select
            }
        }
        adapter.notifyDataSetChanged()

    }

    /**
     * 修改购物车商品数量
     */
    fun changeCartNumber(cartId: Int, add: Boolean) {
        val number = if (add) {
            1
        } else {
            -1
        }
        requestApi(mRetrofit.changeCartNumber(cartId, number), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                ToastUtils.showShort(StringUtils.getString(R.string.successful_operation))
            }

        })
    }


    /**
     * 删除购物车
     */
    fun deleteCart(delete: ArrayList<Int>) {
        if (delete.isEmpty()) {
            return
        }
        val toJson = Gson().toJson(delete)
        val apply = CartDeleteRequest().apply {
            cart_id = toJson
        }

        requestApi(mRetrofit.deleteCart(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.deleteSuccess()
                ToastUtils.showShort(StringUtils.getString(R.string.successful_operation))
            }
        })
    }


}