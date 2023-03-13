package com.jiechengsheng.city.features.store.good

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.store.StoreGoodDetail

/**
 * Created by Wangsw  2021/6/18 14:23.
 *
 */
interface StoreGoodDetailView :BaseMvpView {
    fun bindData(response: StoreGoodDetail)
    fun addSuccess()
}