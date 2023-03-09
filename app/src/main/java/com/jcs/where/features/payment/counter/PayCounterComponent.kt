package com.jcs.where.features.payment.counter

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.pay.PayCounter

/**
 * Created by Wangsw  2023/3/7 16:08.
 *
 */
interface PayCounterView : BaseMvpView {
    fun bindPayCounter(response: MutableList<PayCounter>)

}

class PayCounterPresenter(private var view: PayCounterView) : BaseMvpPresenter(view) {
    fun getChannel() {
        requestApi(mRetrofit.paCounter, object : BaseMvpObserver<ArrayList<PayCounter>>(view) {
            override fun onSuccess(response: ArrayList<PayCounter>) {
                view.bindPayCounter(response.toMutableList())
            }

        })
    }

}