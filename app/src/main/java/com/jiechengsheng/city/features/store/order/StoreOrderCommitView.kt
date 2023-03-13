package com.jiechengsheng.city.features.store.order

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.store.StoreOrderInfoResponse

/**
 * Created by Wangsw  2021/6/21 10:21.
 *
 */
interface StoreOrderCommitView : BaseMvpView {
    fun commitSuccess(response: StoreOrderInfoResponse)
}