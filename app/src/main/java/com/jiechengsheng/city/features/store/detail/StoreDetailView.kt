package com.jiechengsheng.city.features.store.detail

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.store.StoreDetail

/**
 * Created by Wangsw  2021/6/15 10:32.
 *
 */
interface StoreDetailView :BaseMvpView {
    fun bindDetail(data: StoreDetail)
    fun changeCollection(b: Boolean)
}