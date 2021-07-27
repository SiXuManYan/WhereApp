package com.jcs.where.features.gourmet.order.detail2

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail

/**
 * Created by Wangsw  2021/7/23 16:06.
 *
 */
interface DelicacyOrderDetailView : BaseMvpView {

    fun bindDetail(it: FoodOrderDetail)

    fun cancelSuccess()


}

class DelicacyOrderDetailPresenter(private var view: DelicacyOrderDetailView) : BaseMvpPresenter(view) {


    fun getDetail(orderId: String) {
        requestApi(mRetrofit.getFoodOrderDetail(orderId), object : BaseMvpObserver<FoodOrderDetail>(view) {
            override fun onSuccess(response: FoodOrderDetail?) {
                response?.let {
                    view.bindDetail(it)
                }
            }
        })
    }

    /**
     * 取消订单
     */
    fun cancelOrder(orderId: String) {
        requestApi(mRetrofit.cancelFoodOrder(orderId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.cancelSuccess()
            }

        })
    }

}