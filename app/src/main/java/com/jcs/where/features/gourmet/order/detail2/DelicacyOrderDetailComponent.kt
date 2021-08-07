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

    /**
     * 取消成功
     */
    fun cancelSuccess()

    /**
     * 退款成功
     */
    fun refundSuccess()


}

class DelicacyOrderDetailPresenter(private var view: DelicacyOrderDetailView) : BaseMvpPresenter(view) {


    fun getDetail(orderId: Int) {
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
    fun cancelOrder(orderId: Int) {
        requestApi(mRetrofit.cancelFoodOrder(orderId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.cancelSuccess()
            }

        })
    }

    /**
     * 申请退款
     */
    fun refundOrder(orderId: Int) {
        requestApi(mRetrofit.delicacyOrderRefund(orderId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.refundSuccess()
            }

        })
    }

}