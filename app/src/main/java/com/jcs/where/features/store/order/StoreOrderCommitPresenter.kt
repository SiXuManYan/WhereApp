package com.jcs.where.features.store.order

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.utils.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/21 10:26.
 *
 */
class StoreOrderCommitPresenter(private var view: StoreOrderCommitView):BaseMvpPresenter(view) {


    /**
     * 计算价格
     */
    fun handlePrice(adapter: StoreOrderCommitAdapter): BigDecimal {
        var totalPrice: BigDecimal = BigDecimal.ZERO
        adapter.data.forEach {
            var goodTotal: BigDecimal = BigDecimal.ZERO
            it.goods.forEach { good ->
                goodTotal = BigDecimalUtil.add(good.price, goodTotal)
            }
            val currentItemPrice = BigDecimalUtil.add(goodTotal, BigDecimal(it.delivery_fee.toDouble()))
            totalPrice = BigDecimalUtil.add(currentItemPrice, totalPrice)
        }
        return totalPrice
    }

}