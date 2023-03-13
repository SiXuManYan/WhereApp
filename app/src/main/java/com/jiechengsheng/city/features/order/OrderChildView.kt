package com.jiechengsheng.city.features.order

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.order.OrderListResponse

/**
 * Created by Wangsw  2021/5/12 14:08.
 *
 */
interface OrderChildView :BaseMvpView{
    fun bindList(toMutableList: MutableList<OrderListResponse>, lastPage: Boolean)

    /** 确认收货 */
    fun confirmReceipt()
}