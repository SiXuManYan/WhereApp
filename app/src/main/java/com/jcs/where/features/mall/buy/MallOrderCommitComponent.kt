package com.jcs.where.features.mall.buy

import com.google.gson.Gson
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.api.response.mall.request.*
import com.jcs.where.utils.BigDecimalUtil
import java.math.BigDecimal
import java.util.*

/**
 * Created by Wangsw  2021/12/15 13:57.
 *
 */
interface MallOrderCommitView : BaseMvpView {
    fun commitSuccess(response: MallCommitResponse)

    fun bindTotalDelivery(totalServiceDeliveryFee: BigDecimal?)

}


class MallOrderCommitPresenter(private var view: MallOrderCommitView) : BaseMvpPresenter(view) {


    /**
     * 计算价格
     */
    fun handlePrice(adapter: MallOrderCommitAdapter, mTotalServiceDeliveryFee: BigDecimal?): BigDecimal {

        var totalPrice: BigDecimal = BigDecimal.ZERO

        adapter.data.forEach {

            it.gwc.forEach { good ->

                // 商品价格
                val goodPrice = BigDecimalUtil.mul(good.specs_info!!.price, BigDecimal(good.good_num))

                // 配送费(以额外通过城市id获取到的为准)
                val deliveryFee = if (mTotalServiceDeliveryFee != null) {

                    BigDecimal.ZERO
                } else {
                    good.delivery_fee
                }
                val itemPrice = BigDecimalUtil.add(deliveryFee, goodPrice)

                totalPrice = BigDecimalUtil.add(itemPrice, totalPrice)
            }
        }

        // 接口返回的配送费不为空，加上
        if (mTotalServiceDeliveryFee != null) {
            totalPrice = BigDecimalUtil.add(mTotalServiceDeliveryFee, totalPrice)
        }

        return totalPrice
    }

    fun orderCommit(data: ArrayList<MallCartGroup>, addressId: String?) {

        val specsIdsArray = ArrayList<String>()
        val goodsGroup = ArrayList<MallOrderCommitGoodGroup>()

        data.forEach { group ->

            val itemGroup = MallOrderCommitGoodGroup().apply {
                remark = group.nativeRemark
                shop_id = group.shop_id
            }

            group.gwc.forEach { gwc ->


                specsIdsArray.add(gwc.specs_id.toString() + "," + gwc.good_num.toShort())

                val item = MallOrderCommitGoodItem().apply {
                    good_id = gwc.good_id
                    num = gwc.good_num
                    specs_id = gwc.specs_id
                    cart_id = gwc.cart_id
                }

                itemGroup.goods.add(item)
            }
            goodsGroup.add(itemGroup)

        }

        val gson = Gson()
        val bean = MallOrderCommit().apply {
            address_id = addressId
            specsIds = gson.toJson(specsIdsArray)
            goods = gson.toJson(goodsGroup)

        }

        requestApi(mRetrofit.mallOrderCommit(bean), object : BaseMvpObserver<MallCommitResponse>(view) {
            override fun onSuccess(response: MallCommitResponse) {
                view.commitSuccess(response)
            }

            override fun onError(errorResponse: ErrorResponse?) {
                super.onError(errorResponse)
            }

        })
    }


    /**
     * 通过城市id获取店铺配送费
     */
    fun getDelivery(adapter: MallOrderCommitAdapter, cityId: Int) {

        val data = adapter.data
        if (data.isEmpty()) {
            return
        }


        val apply = MallDeliveryRequest().apply {
            city_id = cityId
        }

        data.forEach { shop ->
            val allGoods = ArrayList<MallDeliverItem>()
            shop.gwc.forEach { gwc ->

                val item = MallDeliverItem().apply {
                    shop_id = shop.shop_id
                    goods_id = gwc.good_id
                    count = gwc.good_num
                }
                allGoods.add(item)
            }
            apply.goods.add(allGoods)
        }

        requestApi(mRetrofit.shopDelivery(apply), object : BaseMvpObserver<MallDeliveryResponse>(view) {
            override fun onSuccess(response: MallDeliveryResponse) {
                val deliveryFee = response.delivery_fee

                // 刷新配送费
                adapter.data.forEach {
                    val shopIdKey = it.shop_id.toString()
                    if (deliveryFee.containsKey(shopIdKey)) {
                        it.nativeShopDelivery = deliveryFee[shopIdKey]
                    }
                }
                adapter.notifyDataSetChanged()

                // 获取总配送费
                var totalServiceDeliveryFee = BigDecimal.ZERO
                if (deliveryFee.containsKey("total")) {
                    totalServiceDeliveryFee = deliveryFee["total"] as BigDecimal
                }

                view.bindTotalDelivery(totalServiceDeliveryFee)


            }
        })


    }


}