package com.jcs.where.features.payment.tokenized

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.pay.PayChannelUnbind
import com.jcs.where.api.response.pay.PayCounterChannel

/**
 * Created by Wangsw  2023/3/9 15:25.
 *
 */

interface TokenizedView : BaseMvpView {

    fun bindBoundList(data: MutableList<PayCounterChannel>?)
    fun unBindSuccess()
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

        val apply = PayChannelUnbind().apply {
            channel_code = channelCode
        }

        requestApi(mRetrofit.unBindPayChannel(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.unBindSuccess()
            }

        })

    }

}