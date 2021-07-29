package com.jcs.where.features.gourmet.takeaway.submit

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.gourmet.order.OrderResponse
import com.jcs.where.api.response.gourmet.order.TakeawayOrderSubmitData
import java.util.ArrayList

/**
 * Created by Wangsw  2021/4/26 17:54.
 *
 */
interface OrderSubmitTakeawayView :BaseMvpView{
    fun bindAddress(toMutableList: MutableList<AddressResponse>)
    fun bindTime(otherTimes: ArrayList<String>)
    fun submitSuccess(response: TakeawayOrderSubmitData)
}