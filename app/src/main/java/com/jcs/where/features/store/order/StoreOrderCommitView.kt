package com.jcs.where.features.store.order

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.store.StoreOrderInfoResponse

/**
 * Created by Wangsw  2021/6/21 10:21.
 *
 */
interface StoreOrderCommitView : BaseMvpView {
    fun commitSuccess(response: StoreOrderInfoResponse)
}