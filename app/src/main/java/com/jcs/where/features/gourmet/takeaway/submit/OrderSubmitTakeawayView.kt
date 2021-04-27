package com.jcs.where.features.gourmet.takeaway.submit

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.address.AddressResponse

/**
 * Created by Wangsw  2021/4/26 17:54.
 *
 */
interface OrderSubmitTakeawayView :BaseMvpView{
    fun bindAddress(toMutableList: MutableList<AddressResponse>)
}