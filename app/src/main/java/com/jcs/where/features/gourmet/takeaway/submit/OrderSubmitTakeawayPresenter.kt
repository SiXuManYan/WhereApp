package com.jcs.where.features.gourmet.takeaway.submit

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.address.AddressResponse

/**
 * Created by Wangsw  2021/4/26 17:54.
 *
 */
class OrderSubmitTakeawayPresenter(val view: OrderSubmitTakeawayView) : BaseMvpPresenter(view) {
    fun getAddress() {

        requestApi(mRetrofit.addressList(), object : BaseMvpObserver<List<AddressResponse>>(view) {
            override fun onSuccess(response: List<AddressResponse>) {

                view.bindAddress(response.toMutableList())
            }

        })
    }


}