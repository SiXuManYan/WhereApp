package com.jcs.where.features.gourmet.takeaway.submit

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.gourmet.dish.DeliveryTime
import com.jcs.where.api.response.gourmet.order.OrderResponse
import com.jcs.where.bean.OrderSubmitTakeawayRequest

/**
 * Created by Wangsw  2021/4/26 17:54.
 *
 */
class OrderSubmitTakeawayPresenter(val view: OrderSubmitTakeawayView) : BaseMvpPresenter(view) {

    fun getAddress() {
        requestApi(mRetrofit.addressList(), object : BaseMvpObserver<List<AddressResponse>>(view) {
            override fun onSuccess(response: List<AddressResponse>) {

                view.bindAddress(response.toMutableList())
            }

        })
    }


    fun getTimeList(id: String) {

        requestApi(mRetrofit.timeList(id), object : BaseMvpObserver<DeliveryTime>(view) {
            override fun onSuccess(response: DeliveryTime) {
                val deliveryTime = response.delivery_time
                response.other_times.add(0, deliveryTime)
                view.bindTime(response.other_times)
            }

        })
    }

    fun submitOrder(apply: OrderSubmitTakeawayRequest) {
        requestApi(mRetrofit.takeawayOrderSubmit(apply), object : BaseMvpObserver<OrderResponse>(view) {
            override fun onSuccess(response: OrderResponse?) {
                view.submitSuccess(response)
            }

        })

    }


}