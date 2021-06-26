package com.jcs.where.features.store.order.detail

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.order.store.StoreOrderDetail

/**
 * Created by Wangsw  2021/6/26 11:16.
 * 商城订单详情
 */
interface StoreOrderDetailView : BaseMvpView {
    fun bindData(response: StoreOrderDetail)
}


/**
 * 商城订单详情
 */
class StoreOrderDetailPresenter(private var view: StoreOrderDetailView) : BaseMvpPresenter(view) {


    fun getOrderDetail(orderId: Int) {

        requestApi(mRetrofit.getStoreOrderDetail(orderId), object : BaseMvpObserver<StoreOrderDetail>(view) {
            override fun onSuccess(response: StoreOrderDetail) {
                view.bindData(response)
            }
        })
    }
}