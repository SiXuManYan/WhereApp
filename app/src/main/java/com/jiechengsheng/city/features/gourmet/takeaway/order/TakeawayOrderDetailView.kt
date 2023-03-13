package com.jiechengsheng.city.features.gourmet.takeaway.order

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.gourmet.order.TakeawayOrderDetail

/**
 * Created by Wangsw  2021/5/11 14:53.
 *
 */
interface TakeawayOrderDetailView :BaseMvpView {
    fun bindDetail(it: TakeawayOrderDetail)
    fun cancelSuccess()

}