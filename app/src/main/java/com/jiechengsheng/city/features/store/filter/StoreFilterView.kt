package com.jiechengsheng.city.features.store.filter

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.store.StoreRecommend

/**
 * Created by Wangsw  2021/6/9 10:25.
 *
 */
interface StoreFilterView :BaseMvpView {
    fun bindData(data: MutableList<StoreRecommend>, lastPage: Boolean)
}