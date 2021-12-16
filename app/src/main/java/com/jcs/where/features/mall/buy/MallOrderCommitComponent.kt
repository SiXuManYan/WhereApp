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

}


class MallOrderCommitPresenter(private var view: MallOrderCommitView) : BaseMvpPresenter(view) {


    /**
     * 计算价格
     */
    fun handlePrice(adapter: MallOrderCommitAdapter): BigDecimal {
        var totalPrice: BigDecimal = BigDecimal.ZERO

        adapter.data.forEach {
            var goodTotal: BigDecimal = BigDecimal.ZERO

            var goodDeliveryPrice: BigDecimal = BigDecimal.ZERO

            it.gwc.forEach { good ->
                val mul = BigDecimalUtil.mul(good.specs_info!!.price, BigDecimal(good.good_num))
                goodTotal = BigDecimalUtil.add(mul, goodTotal)
                goodDeliveryPrice = BigDecimalUtil.add(good.delivery_fee, goodTotal)
            }
            val currentItemPrice = BigDecimalUtil.add(goodTotal, goodDeliveryPrice)
            totalPrice = BigDecimalUtil.add(currentItemPrice, totalPrice)
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


}