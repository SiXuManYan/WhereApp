package com.jiechengsheng.city.features.bills.order

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.bills.BillRecommit
import com.jiechengsheng.city.api.response.bills.BillsRecord
import com.jiechengsheng.city.api.response.order.bill.BillOrderDetails

/**
 * Created by Wangsw  2021/7/20 10:49.
 *
 */
interface BillsDetailView : BaseMvpView {
    fun bindDetail(response: BillOrderDetails)
    fun recommitSuccess(orderId: Int)

}

class BillsDetailPresenter(private var view: BillsDetailView) : BaseMvpPresenter(view) {

    fun getDetail(orderId: Int) {

        requestApi(mRetrofit.billOrderDetail(orderId), object : BaseMvpObserver<BillOrderDetails>(view) {
            override fun onSuccess(response: BillOrderDetails) {
                view.bindDetail(response)
            }

        })

    }


    fun recommit(orderId: Int, orderType: Int) {

        val apply = BillRecommit().apply {
            order_id = orderId
        }

        when (orderType) {
            BillsRecord.TYPE_PHONE -> {

                requestApi(mRetrofit.billsRecommit4Phone(apply), object : BaseMvpObserver<JsonElement>(view) {
                    override fun onSuccess(response: JsonElement) {
                        view.recommitSuccess(orderId)
                    }

                })
            }
            else -> {
                requestApi(mRetrofit.billsRecommit(apply), object : BaseMvpObserver<JsonElement>(view) {
                    override fun onSuccess(response: JsonElement) {
                        view.recommitSuccess(orderId)
                    }

                })

            }
        }


    }


}

