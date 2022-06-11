package com.jcs.where.features.gourmet.refund

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillCancelOrder
import com.jcs.where.api.response.mall.RemitId

/**
 * Created by Wangsw  2022/5/12 9:33.
 *
 */
interface ComplexRefundView : BaseMvpView {
    fun refundSuccess()

}


class ComplexRefundPresenter(var view: ComplexRefundView) : BaseMvpPresenter(view) {

    companion object {

        var TYPE_FOOD = 0
        var TYPE_TAKEAWAY = 1
        var TYPE_HOTEL = 2
        var TYPE_BILL = 3
    }


    /**
     * 申请退款
     */
    fun refundOrder(orderId: Int, remitId: Int, type: Int) {

        val remitIdBody = RemitId().apply { remit_id = remitId }

        when (type) {
            TYPE_FOOD -> {

                requestApi(mRetrofit.delicacyOrderRefund(orderId, remitIdBody),
                    object : BaseMvpObserver<JsonElement>(view) {
                        override fun onSuccess(response: JsonElement?) {
                            view.refundSuccess()
                        }
                    })
            }
            TYPE_TAKEAWAY -> {
                requestApi(mRetrofit.takeawayOrderRefund(orderId, remitIdBody), object : BaseMvpObserver<JsonElement>(view) {
                    override fun onSuccess(response: JsonElement?) {
                        view.refundSuccess()
                    }

                })
            }
            TYPE_HOTEL -> {
                requestApi(mRetrofit.refundHotelOrder(orderId, remitIdBody), object : BaseMvpObserver<JsonElement>(view) {
                    override fun onSuccess(response: JsonElement) {
                        view.refundSuccess()
                    }
                })
            }

            TYPE_BILL -> {
                val apply = BillCancelOrder().apply {
                    order_id = orderId
                    remit_id = remitId
                }


                requestApi(mRetrofit.billsCancelOrder(apply), object : BaseMvpObserver<JsonElement>(view) {
                    override fun onSuccess(response: JsonElement) {
                        view.refundSuccess()
                    }
                })
            }

            else -> {}
        }
    }

}
