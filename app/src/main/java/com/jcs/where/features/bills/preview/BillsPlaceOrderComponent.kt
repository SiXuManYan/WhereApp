package com.jcs.where.features.bills.preview

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillsPlaceOrder

/**
 * Created by Wangsw  2022/6/9 14:56.
 *
 */
interface BillsPlaceOrderView : BaseMvpView {

}

class BillsPlaceOrderPresenter(private var view: BillsPlaceOrderView) : BaseMvpPresenter(view) {


    fun placeOrder(billerTag: String, firstField: String, secondField: String, money: Double) {

        val apply = BillsPlaceOrder().apply {
            biller_tag = billerTag
            first_field = firstField
            second_field =   secondField
            amount = money.toString()
        }


        requestApi(mRetrofit.billsPlaceOrder(apply),object :BaseMvpObserver<JsonElement>(view){
            override fun onSuccess(response: JsonElement?) {

            }

        })

    }
}