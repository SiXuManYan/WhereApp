package com.jcs.where.features.address


import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.address.AddressResponse

interface AddressView : BaseMvpView {
    fun bindList(response: ArrayList<AddressResponse>)
}

/**
 * Created by Wangsw  2021/3/22 10:32.
 */
class AddressPresenter(private val view: AddressView) : BaseMvpPresenter(view) {


    val list: Unit
        get() {
            requestApi(mRetrofit.addressList(), object : BaseMvpObserver<ArrayList<AddressResponse>>(view) {

                override fun onSuccess(response: ArrayList<AddressResponse>) {
                    view.bindList(response)
                }
            })
        }
}