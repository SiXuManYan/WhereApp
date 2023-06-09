package com.jiechengsheng.city.features.store.refund.detail

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.order.store.RefundDetail
import com.jiechengsheng.city.api.response.order.store.RefundDetailMall

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


interface MallRefundDetailView : BaseMvpView {
    fun bindDetail(data: RefundDetailMall)

    /**
     * 取消申请退货成功
     */
    fun cancelSuccess()

}

class MallRefundDetailPresenter(private var view: MallRefundDetailView) : BaseMvpPresenter(view) {

    fun getDetail(orderId: Int) {

        requestApi(mRetrofit.mallRefundDetail(orderId), object : BaseMvpObserver<RefundDetailMall>(view) {
            override fun onSuccess(response: RefundDetailMall) {
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

        requestApi(mRetrofit.cancelMallRefund(orderId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.cancelSuccess()
            }

        })

    }


}