package com.jcs.where.features.refund.add.channel

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.RefundChannel

/**
 * Created by Wangsw  2022/4/25 19:03.
 *
 */
interface RefundChannelView : BaseMvpView {
    fun bindChanel(data: ArrayList<RefundChannel>)

}

class RefundChannelPresenter(private var view: RefundChannelView) : BaseMvpPresenter(view) {
    fun getChannel() {

        requestApi(mRetrofit.refundChannel, object : BaseMvpObserver<ArrayList<RefundChannel>>(view) {
            override fun onSuccess(response: ArrayList<RefundChannel>) {

                val responseData = ArrayList<RefundChannel>()

                val groupBy = response.groupBy { it.channel_category == "BANK" }

                groupBy.forEach {

                    if (it.key) {
                        val indexOfFirst = it.value.indexOfFirst {
                            it.channel_category == "BANK"
                        }
                        it.value[indexOfFirst].isWidthSplit = true
                    }
                    responseData.addAll(it.value)
                }

                view.bindChanel(responseData)
            }

        })
    }

}