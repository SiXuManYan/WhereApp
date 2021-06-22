package com.jcs.where.features.store.order

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.address.AddressResponse

/**
 * Created by Wangsw  2021/6/21 10:21.
 *
 */
interface StoreOrderCommitView :BaseMvpView {
     fun bindAddress(toMutableList: MutableList<AddressResponse>)
}