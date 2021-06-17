package com.jcs.where.features.store.detail.good

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.store.StoreGoods

/**
 * Created by Wangsw  2021/6/16 15:27.
 *
 */
interface StoreGoodView : BaseMvpView {
    fun bindData(data: MutableList<StoreGoods>, lastPage: Boolean)
}