package com.jiechengsheng.city.features.order

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.order.OrderListResponse

/**
 * Created by Wangsw  2021/5/12 14:08.
 *
 */
class OrderChildPresenter(val view: OrderChildView) : BaseMvpPresenter(view) {


    fun getList(orderType: Int, page: Int) {
        requestApi(mRetrofit.getOrderList(orderType, null, page),
            object : BaseMvpObserver<PageResponse<OrderListResponse>>(view, page) {
                override fun onSuccess(response: PageResponse<OrderListResponse>) {
                    val isLastPage = response.lastPage == page
                    val data = response.data
                    val toMutableList = data.toMutableList()
                    view.bindList(toMutableList, isLastPage)
                }
            })
    }


    fun confirmReceipt(orderId: Int) {
        requestApi(mRetrofit.confirmReceipt(orderId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.confirmReceipt()
            }

        })
    }


}