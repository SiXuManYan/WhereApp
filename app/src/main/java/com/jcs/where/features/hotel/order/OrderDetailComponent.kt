package com.jcs.where.features.hotel.order

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.hotel.HotelOrderDetail

/**
 * Created by Wangsw  2021/8/3 14:46.
 *
 */
interface HotelOrderDetailView : BaseMvpView {

    /**
     * 酒店订单详情数据
     */
    fun bindDetail(response: HotelOrderDetail)

    /**
     * 订单取消成功
     */
    fun cancelSuccess()



}

class HotelOrderDetailPresenter(private var viewHotel: HotelOrderDetailView) : BaseMvpPresenter(viewHotel) {

    /**
     * 酒店订单详情数据
     */
    fun getDetail(orderId: Int) {
        requestApi(mRetrofit.hotelOrderDetail(orderId), object : BaseMvpObserver<HotelOrderDetail>(viewHotel) {
            override fun onSuccess(response: HotelOrderDetail) = viewHotel.bindDetail(response)
        })
    }


    /**
     * 取消订单
     */
    fun cancelOrder(orderId: Int) {
        requestApi(mRetrofit.cancelHotelOrder(orderId), object : BaseMvpObserver<JsonElement>(viewHotel) {
            override fun onSuccess(response: JsonElement) = viewHotel.cancelSuccess()
        })
    }







}