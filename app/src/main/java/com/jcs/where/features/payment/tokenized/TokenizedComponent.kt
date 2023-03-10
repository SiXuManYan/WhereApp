package com.jcs.where.features.payment.tokenized

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.pay.PayCounterChannel

/**
 * Created by Wangsw  2023/3/9 15:25.
 *
 */

interface TokenizedView : BaseMvpView {

    fun bindBoundList(data: MutableList<PayCounterChannel>?)
}

class TokenizedPresenter(private var view: TokenizedView) :BaseMvpPresenter(view){




    fun getBoundList() {

        requestApi(mRetrofit.boundChannel, object : BaseMvpObserver<ArrayList<PayCounterChannel>>(view) {
            override fun onSuccess(response: ArrayList<PayCounterChannel>?) {
                view.bindBoundList(response?.toMutableList())
            }
        })
    }

    fun unbindToken(channelCode: String) {


    }

}