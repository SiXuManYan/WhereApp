package com.jcs.where.features.gourmet.order.detail

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail

/**
 * Created by Wangsw  2021/5/10 9:47.
 *
 */
interface FoodOrderDetailView :BaseMvpView{
    fun bindDetail(it: FoodOrderDetail)
}