package com.jiechengsheng.city.features.store.order

import com.google.gson.Gson
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.request.StoreOrderCommit
import com.jiechengsheng.city.api.request.StoreOrderCommitGood
import com.jiechengsheng.city.api.request.StoreOrderCommitShop
import com.jiechengsheng.city.api.response.store.StoreOrderCommitData
import com.jiechengsheng.city.api.response.store.StoreOrderInfoResponse
import com.jiechengsheng.city.utils.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/21 10:26.
 *
 */
class StoreOrderCommitPresenter(private var view: StoreOrderCommitView) : BaseMvpPresenter(view) {


    /**
     * 计算价格
     */
    fun handlePrice(adapter: StoreOrderCommitAdapter): BigDecimal {
        var totalPrice: BigDecimal = BigDecimal.ZERO

        adapter.data.forEach {
            var goodTotal: BigDecimal = BigDecimal.ZERO

            it.goods.forEach { good ->
                val mul = BigDecimalUtil.mul(good.price, BigDecimal(good.good_num))
                goodTotal = BigDecimalUtil.add(mul, goodTotal)
            }
            val currentItemPrice = BigDecimalUtil.add(goodTotal, BigDecimal(it.delivery_fee.toDouble()))
            totalPrice = BigDecimalUtil.add(currentItemPrice, totalPrice)
        }
        return totalPrice
    }



    fun orderCommit(data: ArrayList<StoreOrderCommitData>, addressId: String?, phone: String) {


        // 商品
        val commitGoodList: ArrayList<StoreOrderCommitShop> = ArrayList()

        data.forEach { parent ->

            val shop = StoreOrderCommitShop().apply {
                shop_id = parent.shop_id
                remark = parent.remark
            }

            parent.goods.forEach {
                shop.goods.add(StoreOrderCommitGood().apply {
                    good_id = it.good_id
                    good_num = it.good_num
                })
            }
            commitGoodList.add(shop)

        }


        // 提交
        val apply = StoreOrderCommit().apply {
            delivery_type = data[0].delivery_type

            if (delivery_type == 1) {
                tel = phone
            }

            if (delivery_type == 2) {
                address_id = addressId
            }
            goods = Gson().toJson(commitGoodList)

        }

        requestApi(mRetrofit.storeOrderCommit(apply), object : BaseMvpObserver<StoreOrderInfoResponse>(view) {
            override fun onSuccess(response: StoreOrderInfoResponse) {
                view.commitSuccess(response)
            }


        })
    }


}