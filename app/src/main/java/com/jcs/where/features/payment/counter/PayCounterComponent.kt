package com.jcs.where.features.payment.counter

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.pay.PayCounterChannel
import com.jcs.where.api.response.pay.PayCounterChannelDetail

/**
 * Created by Wangsw  2023/3/7 16:08.
 *
 */
interface PayCounterView : BaseMvpView {
    fun bindPayCounter(response: MutableList<PayCounterChannel>)
    fun bindChannelDetail(response: PayCounterChannelDetail){}

}

class PayCounterPresenter(private var view: PayCounterView) : BaseMvpPresenter(view) {


    fun getChannel() {
        requestApi(mRetrofit.payCounterChannel, object : BaseMvpObserver<ArrayList<PayCounterChannel>>(view) {
            override fun onSuccess(response: ArrayList<PayCounterChannel>) {
                view.bindPayCounter(response.toMutableList())
            }

        })
    }

    fun getChannelDetail(channelCode:String) {
        requestApi(mRetrofit.getChannelBalance(channelCode), object : BaseMvpObserver<PayCounterChannelDetail>(view) {
            override fun onSuccess(response: PayCounterChannelDetail) {
                view.bindChannelDetail(response)
            }
        })
    }

}