package com.jcs.where.features.gourmet.takeaway.submit

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.gourmet.dish.DeliveryTime
import com.jcs.where.api.response.gourmet.order.OrderResponse
import com.jcs.where.api.response.gourmet.order.TakeawayOrderSubmitData
import com.jcs.where.bean.OrderSubmitTakeawayRequest

/**
 * Created by Wangsw  2021/4/26 17:54.
 *
 */
class OrderSubmitTakeawayPresenter(val view: OrderSubmitTakeawayView) : BaseMvpPresenter(view) {



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
        requestApi(mRetrofit.takeawayOrderSubmit(apply), object : BaseMvpObserver<TakeawayOrderSubmitData>(view) {
            override fun onSuccess(response: TakeawayOrderSubmitData) {
                view.submitSuccess(response)
            }

        })

    }


}