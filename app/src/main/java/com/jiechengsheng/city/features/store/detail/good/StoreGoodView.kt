package com.jiechengsheng.city.features.store.detail.good

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.store.StoreGoods

/**
 * Created by Wangsw  2021/6/16 15:27.
 *
 */
interface StoreGoodView : BaseMvpView {
    fun bindData(data: MutableList<StoreGoods>, lastPage: Boolean)
}