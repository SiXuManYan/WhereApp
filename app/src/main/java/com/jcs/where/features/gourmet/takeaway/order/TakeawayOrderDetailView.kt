package com.jcs.where.features.gourmet.takeaway.order

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.order.TakeawayOrderDetail

/**
 * Created by Wangsw  2021/5/11 14:53.
 *
 */
interface TakeawayOrderDetailView :BaseMvpView {
    fun bindDetail(it: TakeawayOrderDetail)
    fun cancelSuccess()
    fun refundSuccess()
}