package com.jcs.where.features.bills.channel

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillsChannel

/**
 * Created by Wangsw  2022/6/8 15:29.
 *
 */
interface BillsChannelView : BaseMvpView {
    fun bindData(response:ArrayList<BillsChannel>)
}

class BillsChannelPresenter(private var view: BillsChannelView) : BaseMvpPresenter(view) {


    fun getData(type: Int) {
         requestApi(mRetrofit.payBillsList(type),object :BaseMvpObserver<ArrayList<BillsChannel>>(view){
             override fun onSuccess(response: ArrayList<BillsChannel>) {
                view.bindData(response)
             }
         })
    }

}