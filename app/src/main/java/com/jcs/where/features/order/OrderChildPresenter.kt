package com.jcs.where.features.order

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.OrderListResponse
import com.jcs.where.api.response.PageResponse

/**
 * Created by Wangsw  2021/5/12 14:08.
 *
 */
class OrderChildPresenter (val view: OrderChildView) : BaseMvpPresenter(view) {


    fun getList(orderType: Int, page: Int) {
        requestApi(mRetrofit.getOrderList(orderType,null,page),object :BaseMvpObserver<PageResponse<OrderListResponse>>(view){
            override fun onSuccess(response: PageResponse<OrderListResponse>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                val toMutableList = data.toMutableList()
                view.bindList(toMutableList, isLastPage)

            }

        })
    }


}