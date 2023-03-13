package com.jiechengsheng.city.features.store.search

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.store.StoreRecommend

/**
 * Created by Wangsw  2021/6/7 10:16.
 *
 */
interface StoreSearchView :BaseMvpView {
    fun bindData(data: MutableList<StoreRecommend>, lastPage: Boolean)
}