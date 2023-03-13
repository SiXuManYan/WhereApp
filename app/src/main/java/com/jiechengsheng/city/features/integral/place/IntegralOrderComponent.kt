package com.jiechengsheng.city.features.integral.place

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.IntegralPlaceOrder
import com.jiechengsheng.city.api.response.integral.IntegralGoodDetail
import com.jiechengsheng.city.api.response.integral.IntegralPlaceOrderResponse

/**
 * Created by Wangsw  2022/9/23 14:21.
 *
 */
interface IntegralOrderView : BaseMvpView {
    fun bindDetail(response: IntegralGoodDetail){}
    fun submitSuccess(response: IntegralPlaceOrderResponse){}

}


class IntegralOrderPresenter(private var view: IntegralOrderView) : BaseMvpPresenter(view) {

    fun getData(goodId: Int) {

        requestApi(mRetrofit.getIntegralGoodDetail(goodId),object :BaseMvpObserver<IntegralGoodDetail>(view){
            override fun onSuccess(response: IntegralGoodDetail) {
                view.bindDetail(response)
            }

        })
    }


    fun makeOrder(goodsId: Int, addressId: String? = null) {
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