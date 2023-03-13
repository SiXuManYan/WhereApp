package com.jiechengsheng.city.features.store.pay

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.store.PayChannel

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

