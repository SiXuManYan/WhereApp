package com.jcs.where.features.bills.preview

import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillsPlaceOrder
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse

/**
 * Created by Wangsw  2022/6/9 14:56.
 *
 */
interface BillsPlaceOrderView : BaseMvpView {
    fun commitSuccess(response: HotelOrderCommitResponse)

}

class BillsPlaceOrderPresenter(private var view: BillsPlaceOrderView) : BaseMvpPresenter(view) {


    fun placeOrder(billerTag: String, firstField: String, secondField: String, money: Double, billType: Int) {

        val apply = BillsPlaceOrder().apply {
            biller_tag = billerTag
            first_field = firstField
            second_field = secondField
            amount = money.toString()
            bill_type = billType
        }


        requestApi(mRetrofit.billsPlaceOrder(apply), object : BaseMvpObserver<HotelOrderCommitResponse>(view) {
            override fun onSuccess(response: HotelOrderCommitResponse) {
                view.commitSuccess(response)
            }



        })

    }
}