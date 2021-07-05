package com.jcs.where.features.store.refund.detail

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.order.store.RefundDetail

/**
 * Created by Wangsw  2021/7/1 17:54.
 *
 */

interface StoreRefundDetailView : BaseMvpView {
    fun bindDetail(data: RefundDetail)

    /**
     * 取消申请退货成功
     */
    fun cancelSuccess()

}

class StoreRefundDetailPresenter(private var view: StoreRefundDetailView) : BaseMvpPresenter(view) {

    fun getDetail(orderId: Int) {

        requestApi(mRetrofit.storeRefundDetail(orderId), object : BaseMvpObserver<RefundDetail>(view) {
            override fun onSuccess(response: RefundDetail) {
                view.bindDetail(response)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

        })
    }

    /**
     * 取消申请售后
     */
    fun cancelApplication(orderId: Int) {

        requestApi(mRetrofit.cancelStoreRefund(orderId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.cancelSuccess()
            }

        })

    }


}