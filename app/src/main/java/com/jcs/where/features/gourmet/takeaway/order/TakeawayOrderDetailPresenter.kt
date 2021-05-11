package com.jcs.where.features.gourmet.takeaway.order

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.gourmet.order.TakeawayOrderDetail

/**
 * Created by Wangsw  2021/5/11 14:54.
 *
 */
class TakeawayOrderDetailPresenter (val view: TakeawayOrderDetailView) : BaseMvpPresenter(view) {



    fun getDetail(orderId: String) {
        requestApi(mRetrofit.getTakeawayOrderDetail(orderId), object : BaseMvpObserver<TakeawayOrderDetail>(view) {
            override fun onSuccess(response: TakeawayOrderDetail?) {
                response?.let {
                    view.bindDetail(it)
                }
            }
        })


    }


}