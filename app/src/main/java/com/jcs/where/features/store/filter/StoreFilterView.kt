package com.jcs.where.features.store.filter

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.store.StoreRecommend

/**
 * Created by Wangsw  2021/6/9 10:25.
 *
 */
interface StoreFilterView :BaseMvpView {
    fun bindData(data: MutableList<StoreRecommend>, lastPage: Boolean)
}