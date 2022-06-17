package com.jcs.where.features.bills.order

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillRecommit
import com.jcs.where.api.response.order.bill.BillOrderDetails

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


    fun recommit(orderId: Int) {

        val apply = BillRecommit().apply {
            order_id = orderId
        }
        requestApi(mRetrofit.billsRecommit(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.recommitSuccess(orderId)
            }

        })
    }


}

