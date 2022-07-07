package com.jcs.where.features.bills.channel

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillsChannel
import com.jcs.where.api.response.bills.CallChargeChannel

/**
 * Created by Wangsw  2022/6/8 15:29.
 *
 */
interface BillsChannelView : BaseMvpView {
    fun bindCommonData(response:ArrayList<BillsChannel>)
    fun bindCallData(response: java.util.ArrayList<CallChargeChannel>)
}

class BillsChannelPresenter(private var view: BillsChannelView) : BaseMvpPresenter(view) {


    fun getData(type: Int) {

        if (type == 1) {
            requestApi(mRetrofit.callChargesPayBillsList(),object :BaseMvpObserver<ArrayList<CallChargeChannel>>(view){
                override fun onSuccess(response: ArrayList<CallChargeChannel>) {
                    view.bindCallData(response)
                }
            })
        }else {
            requestApi(mRetrofit.payBillsList(type),object :BaseMvpObserver<ArrayList<BillsChannel>>(view){
                override fun onSuccess(response: ArrayList<BillsChannel>) {
                    view.bindCommonData(response)
                }
            })
        }


    }

}