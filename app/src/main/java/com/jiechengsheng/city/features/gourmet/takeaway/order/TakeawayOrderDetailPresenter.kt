package com.jiechengsheng.city.features.gourmet.takeaway.order

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.gourmet.order.TakeawayOrderDetail

/**
 * Created by Wangsw  2021/5/11 14:54.
 *
 */
class TakeawayOrderDetailPresenter (val view: TakeawayOrderDetailView) : BaseMvpPresenter(view) {


    /**
     * 外卖订单详情
     */
    fun getDetail(orderId: Int) {
        requestApi(mRetrofit.getTakeawayOrderDetail(orderId), object : BaseMvpObserver<TakeawayOrderDetail>(view) {
            override fun onSuccess(response: TakeawayOrderDetail?) {
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
        requestApi(mRetrofit.takeawayOrderCancel(orderId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.cancelSuccess()
            }
        })
    }





}