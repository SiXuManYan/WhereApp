package com.jcs.where.features.store.pay

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.store.UpLoadPayAccountInfo
import com.jcs.where.api.response.store.PayChannel

/**
 * Created by Wangsw  2021/6/23 15:10.
 *
 */

interface StorePayView : BaseMvpView {
    fun bindData(response: ArrayList<PayChannel>)

}

class StorePayPresenter(private var view: StorePayView) : BaseMvpPresenter(view) {

    fun getPayChannel() {
        requestApi(mRetrofit.payChannel, object : BaseMvpObserver<ArrayList<PayChannel>>(view) {
            override fun onSuccess(response: ArrayList<PayChannel>) {
                view.bindData(response)
            }
        })
    }


}

