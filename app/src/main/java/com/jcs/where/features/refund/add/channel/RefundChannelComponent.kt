package com.jcs.where.features.refund.add.channel

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.RefundChannel

/**
 * Created by Wangsw  2022/4/25 19:03.
 *
 */
interface RefundChannelView:BaseMvpView {
    fun bindChanel(data: ArrayList<RefundChannel>)

}

class RefundChannelPresenter(private var view: RefundChannelView):BaseMvpPresenter(view) {
    fun getChannel() {

        requestApi(mRetrofit.refundChannel , object :BaseMvpObserver<ArrayList<String>>(view){
            override fun onSuccess(response: ArrayList<String>) {
                val data = ArrayList<RefundChannel>()
                response.forEach {
                    val apply = RefundChannel().apply {
                        name = it
                    }
                    data.add(apply)
                }
                view.bindChanel(data)
            }

        })
    }

}