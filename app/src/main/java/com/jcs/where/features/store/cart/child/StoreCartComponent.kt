package com.jcs.where.features.store.cart.child

import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.CartDeleteRequest
import com.jcs.where.api.response.store.cart.StoreCartGroup
import com.jcs.where.api.response.store.cart.StoreCartResponse
import com.jcs.where.utils.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/7/5 13:40.
 *
 */

interface StoreCartView : BaseMvpView {

    fun deleteSuccess()
    fun bindList(deliveryCarts: ArrayList<StoreCartGroup>)

}


class StoreCartPresenter(private var view: StoreCartView) : BaseMvpPresenter(view) {


    /**
     * 处理价格
     */
    fun handlePrice(adapter: StoreCartAdapter): BigDecimal {

        var totalPrice: BigDecimal = BigDecimal.ZERO
        adapter.data.forEachIndexed { _, data ->
            data.goods.forEach {
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
     * 顶级是否全部选中
     */
    fun checkSelectAll(adapter: StoreCartAdapter): Boolean {
        val result = ArrayList<Boolean>()
        result.clear()
        adapter.data.forEach { data ->
            result.add(data.nativeIsSelect)
            data.goods.forEach {
                result.add(it.nativeIsSelect)
            }
        }
        return !result.contains(false)
    }


    /**
     * 处理全选和非全选
     */
    fun handleSelectAll(adapter: StoreCartAdapter, select: Boolean) {

        adapter.data.forEach {
            it.nativeIsSelect = select
            it.goods.forEach { child ->
                child.nativeIsSelect = select
            }
        }
        adapter.notifyDataSetChanged()

    }

    fun getSelectedCount(adapter: StoreCartAdapter): Int {
        var count = 0
        adapter.data.forEach {

            it.goods.forEach { child ->
                if (child.nativeIsSelect) {
                    count += 1
                }
            }
        }
        return count
    }

    fun getSelectedData(adapter: StoreCartAdapter): ArrayList<StoreCartGroup> {

        var selectData: ArrayList<StoreCartGroup> = ArrayList()

        adapter.data.forEach {

            it.goods.forEach { child ->
                if (child.nativeIsSelect) {

                    val group = StoreCartGroup().apply {
                        shop_id = it.shop_id
                        shop_name = it.shop_name
                        delivery_fee = it.delivery_fee
                        nativeIsSelect = true
                        goods.add(child)
                    }
                    selectData.add(group)

                }
            }
        }
        return selectData
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
        requestApi(mRetrofit.changeStoreCartNumber(cartId, number), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                ToastUtils.showShort(StringUtils.getString(R.string.successful_operation))
            }

        })
    }


    /**
     * 删除购物车
     */
    fun deleteCart(adapter: StoreCartAdapter) {
        if (adapter.data.isEmpty()) {
            return
        }
        val delete = ArrayList<Int>()
        adapter.data.forEach {
            it.goods.forEach { child ->
                if (child.nativeIsSelect) {
                    delete.add(child.cart_id)
                }
            }
        }
        if (delete.isEmpty()) {
            return
        }
        val toJson = Gson().toJson(delete)
        val apply = CartDeleteRequest().apply {
            cart_id = toJson
        }

        requestApi(mRetrofit.deleteStoreCart(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.deleteSuccess()
                ToastUtils.showShort(StringUtils.getString(R.string.successful_operation))
            }
        })
    }


    /**
     * 清空购物车
     * clearStoreCart
     */
    fun clearStoreCart() {
        requestApi(mRetrofit.clearStoreCart(), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                ToastUtils.showShort(StringUtils.getString(R.string.successful_operation))
            }
        })
    }


    /**
     * 购物车列表
     *  @param listType 列表类型（0：自提，1：外卖）
     */
    fun getStoreCart(listType: Int) {
        requestApi(mRetrofit.storeCart, object : BaseMvpObserver<StoreCartResponse>(view) {
            override fun onSuccess(response: StoreCartResponse) {

                val deliveryCarts = response.delivery_carts
                val takeCarts = response.take_carts

                if (listType == 0) {
                    view.bindList(deliveryCarts)
                }

                if (listType == 1) {
                    view.bindList(takeCarts)
                }


            }
        })
    }


}