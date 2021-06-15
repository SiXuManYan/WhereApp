package com.jcs.where.features.store.detail

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.store.StoreDetail

/**
 * Created by Wangsw  2021/6/15 10:32.
 *
 */
interface StoreDetailView :BaseMvpView {
    fun bindDetail(response: StoreDetail)
}