package com.jcs.where.features.store.search

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.store.StoreRecommend

/**
 * Created by Wangsw  2021/6/7 10:16.
 *
 */
interface StoreSearchView :BaseMvpView {
    fun bindData(data: MutableList<StoreRecommend>, lastPage: Boolean)
}