package com.jcs.where.features.store.good

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.store.StoreGoodDetail

/**
 * Created by Wangsw  2021/6/18 14:23.
 *
 */
interface StoreGoodDetailView :BaseMvpView {
    fun bindData(response: StoreGoodDetail)
    fun addSuccess()
}