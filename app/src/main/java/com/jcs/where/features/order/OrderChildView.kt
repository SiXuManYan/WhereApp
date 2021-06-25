package com.jcs.where.features.order

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.order.OrderListResponse

/**
 * Created by Wangsw  2021/5/12 14:08.
 *
 */
interface OrderChildView :BaseMvpView{
    fun bindList(toMutableList: MutableList<OrderListResponse>, lastPage: Boolean)
}