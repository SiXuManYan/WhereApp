package com.jcs.where.features.bills.hydropower.pay

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.store.PayChannel

/**
 * Created by Wangsw  2021/6/29 16:42.
 *
 */

interface BillsPayView : BaseMvpView {
    fun bindData(response: ArrayList<PayChannel>)
}


class BillsPayPresenter(private var view: BillsPayView) : BaseMvpPresenter(view) {
    fun getPayChannel() {
        requestApi(mRetrofit.payChannel, object : BaseMvpObserver<ArrayList<PayChannel>>(view) {
            override fun onSuccess(response: ArrayList<PayChannel>) {
                view.bindData(response)
            }
        })
    }

}
