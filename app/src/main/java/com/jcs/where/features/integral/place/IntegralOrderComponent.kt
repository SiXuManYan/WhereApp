package com.jcs.where.features.integral.place

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.IntegralPlaceOrder
import com.jcs.where.api.response.integral.IntegralPlaceOrderResponse

/**
 * Created by Wangsw  2022/9/23 14:21.
 *
 */
interface IntegralOrderView : BaseMvpView {
    fun submitSuccess(response: IntegralPlaceOrderResponse)

}


class IntegralOrderPresenter(private var view: IntegralOrderView) : BaseMvpPresenter(view) {

    fun makeOrder(goodsId: Int, addressId: String) {
        val apply = IntegralPlaceOrder().apply {
            goods_id = goodsId.toString()
            address_id = addressId
        }

        requestApi(mRetrofit.integralPlaceOrder(apply), object : BaseMvpObserver<IntegralPlaceOrderResponse>(view) {
            override fun onSuccess(response: IntegralPlaceOrderResponse) {
                view.submitSuccess(response)
            }

        })
    }
}