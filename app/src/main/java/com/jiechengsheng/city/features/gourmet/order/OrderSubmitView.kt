package com.jiechengsheng.city.features.gourmet.order

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.gourmet.order.FoodOrderSubmitData

/**
 * Created by Wangsw  2021/4/20 16:54.
 *
 */
interface OrderSubmitView :BaseMvpView {
    fun summitSuccess(response: FoodOrderSubmitData)
}