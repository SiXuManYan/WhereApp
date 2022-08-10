package com.jcs.where.features.order.refund

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.order.OrderListResponse
import com.jcs.where.api.response.order.RefundOrder

/**
 * Created by Wangsw  2022/3/26 10:41.
 *
 */
interface RefundOrderView : BaseMvpView {
    fun bindList(toMutableList: MutableList<RefundOrder>, lastPage: Boolean)

}

class RefundOrderPresenter(private var view: RefundOrderView) : BaseMvpPresenter(view) {
    fun getList(page: Int) {
        requestApi(mRetrofit.refundOrderList(  page), object : BaseMvpObserver<PageResponse<RefundOrder>>(view,page) {
            override fun onSuccess(response: PageResponse<RefundOrder>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                val toMutableList = data.toMutableList()
                view.bindList(toMutableList, isLastPage)
            }
        })
    }

}