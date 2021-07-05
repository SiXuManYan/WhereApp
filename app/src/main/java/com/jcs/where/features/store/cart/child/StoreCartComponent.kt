package com.jcs.where.features.store.cart.child

import com.google.gson.Gson
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.CartDeleteRequest
import com.jcs.where.utils.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/7/5 13:40.
 *
 */

interface StoreCartView : BaseMvpView {

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

    /**
     * 修改购物车商品数量
     */
    fun changeCartNumber(cartId: Int, add: Boolean) {
        val number = if (add) {
            1
        } else {
            -1
        }
//        requestApi(mRetrofit.changeCartNumber(cartId, number), object : BaseMvpObserver<JsonElement>(view) {
//            override fun onSuccess(response: JsonElement?) {
//                ToastUtils.showShort(StringUtils.getString(R.string.successful_operation))
//            }
//
//        })
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

//        requestApi(mRetrofit.deleteCart(apply), object : BaseMvpObserver<JsonElement>(view) {
//            override fun onSuccess(response: JsonElement) {
//                view.deleteSuccess()
//                ToastUtils.showShort(StringUtils.getString(R.string.successful_operation))
//            }
//        })
    }



}