package com.jcs.where.features.store.order

import com.google.gson.Gson
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.request.StoreOrderCommit
import com.jcs.where.api.request.StoreOrderCommitGood
import com.jcs.where.api.request.StoreOrderCommitShop
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.store.StoreOrderCommitData
import com.jcs.where.api.response.store.StoreOrderInfoResponse
import com.jcs.where.utils.BigDecimalUtil
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
                goodTotal = BigDecimalUtil.add(good.price, goodTotal)
            }
            val currentItemPrice = BigDecimalUtil.add(goodTotal, BigDecimal(it.delivery_fee.toDouble()))
            totalPrice = BigDecimalUtil.add(currentItemPrice, totalPrice)
        }
        return totalPrice
    }

    fun getAddress() {
        requestApi(mRetrofit.addressList(), object : BaseMvpObserver<List<AddressResponse>>(view) {
            override fun onSuccess(response: List<AddressResponse>) {

                view.bindAddress(response.toMutableList())
            }

        })
    }

    fun orderCommit(data: StoreOrderCommitData, addressId: String, phone: String) {


        // 商品
        val commitGoodList: ArrayList<StoreOrderCommitShop> = ArrayList()

        data.goods.forEach {

            val commitGoodChild = StoreOrderCommitShop().apply {
                shop_id = data.shop_id
                remark = data.remark
                goods.add(StoreOrderCommitGood().apply {
                    good_id = it.good_id
                    good_num = it.good_num
                })
            }
            commitGoodList.add(commitGoodChild)
        }


        // 其他
        val apply = StoreOrderCommit().apply {
            delivery_type = data.delivery_type

            if (data.delivery_type == 1) {
                tel = phone
            }

            if (data.delivery_type == 2) {
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