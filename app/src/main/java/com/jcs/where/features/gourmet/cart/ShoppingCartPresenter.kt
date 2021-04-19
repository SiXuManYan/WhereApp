package com.jcs.where.features.gourmet.cart

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
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

    /**
     * 是否全部选中
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


}