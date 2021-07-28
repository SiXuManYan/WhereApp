package com.jcs.where.features.gourmet.order

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.order.FoodOrderSubmitData
import com.jcs.where.api.response.gourmet.order.OrderResponse

/**
 * Created by Wangsw  2021/4/20 16:54.
 *
 */
interface OrderSubmitView :BaseMvpView {
    fun bindData(response: FoodOrderSubmitData)
}