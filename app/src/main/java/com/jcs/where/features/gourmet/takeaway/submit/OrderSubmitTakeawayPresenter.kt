package com.jcs.where.features.gourmet.takeaway.submit

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.dish.DeliveryTime
import com.jcs.where.api.response.gourmet.dish.DeliveryTimeRetouch
import com.jcs.where.api.response.gourmet.order.TakeawayOrderSubmitData
import com.jcs.where.bean.OrderSubmitTakeawayRequest


interface OrderSubmitTakeawayView : BaseMvpView {
    fun bindTime(otherTimes: MutableList<DeliveryTimeRetouch>)
    fun submitSuccess(response: TakeawayOrderSubmitData)
}

/**
 * Created by Wangsw  2021/4/26 17:54.
 *
 */
class OrderSubmitTakeawayPresenter(val view: OrderSubmitTakeawayView) : BaseMvpPresenter(view) {


    fun getTimeList(id: String) {

        requestApi(mRetrofit.timeList(id), object : BaseMvpObserver<DeliveryTime>(view) {
            override fun onSuccess(response: DeliveryTime) {
                response.other_times.add(0,  response.delivery_time)


                val timesData: MutableList<DeliveryTimeRetouch> = ArrayList()
                response.other_times.forEach {
                    val apply = DeliveryTimeRetouch().apply {
                        time = it
                    }
                    timesData.add(apply)
                }

                view.bindTime(timesData)
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