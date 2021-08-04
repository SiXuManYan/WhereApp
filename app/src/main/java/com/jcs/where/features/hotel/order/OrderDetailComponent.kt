package com.jcs.where.features.hotel.order

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.hotel.HotelOrderDetail

/**
 * Created by Wangsw  2021/8/3 14:46.
 *
 */
interface OrderDetailView : BaseMvpView {
    fun bindDetail(response: HotelOrderDetail)

}

class OrderDetailPresenter(private var view: OrderDetailView) : BaseMvpPresenter(view) {
    fun getDetail(orderId: Int) {
        requestApi(mRetrofit.hotelOrderDetail(orderId), object : BaseMvpObserver<HotelOrderDetail>(view) {
            override fun onSuccess(response: HotelOrderDetail) = view.bindDetail(response)
        })
    }

}