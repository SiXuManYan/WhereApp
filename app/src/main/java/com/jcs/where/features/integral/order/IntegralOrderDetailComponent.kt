package com.jcs.where.features.integral.order

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.integral.IntegralOrderDetail

/**
 * Created by Wangsw  2022/9/26 17:11.
 *
 */
interface IntegralOrderDetailView : BaseMvpView {
    fun bindData(response: IntegralOrderDetail)
}


class IntegralOrderDetailPresenter(private var view: IntegralOrderDetailView) : BaseMvpPresenter(view) {

    fun getData(orderId: Int) {
        requestApi(mRetrofit.integralOrderDetail(orderId), object : BaseMvpObserver<IntegralOrderDetail>(view) {
            override fun onSuccess(response: IntegralOrderDetail) {
                view.bindData(response)
            }
        })
    }

}