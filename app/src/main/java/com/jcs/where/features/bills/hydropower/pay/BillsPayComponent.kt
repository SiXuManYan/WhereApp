package com.jcs.where.features.bills.hydropower.pay

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.bills.BillsOrderCommit
import com.jcs.where.api.response.bills.BillsOrderInfo
import com.jcs.where.api.response.store.PayChannel

/**
 * Created by Wangsw  2021/6/29 16:42.
 *
 */

interface BillsPayView : BaseMvpView {

    /**
     * 支付渠道列表
     */
    fun bindChannelData(response: ArrayList<PayChannel>)

    /**
     * 订单提交成功
     */
    fun commitOrderSuccess(response: BillsOrderInfo)
}


class BillsPayPresenter(private var view: BillsPayView) : BaseMvpPresenter(view) {
    fun getPayChannel() {
        requestApi(mRetrofit.payChannel, object : BaseMvpObserver<ArrayList<PayChannel>>(view) {
            override fun onSuccess(response: ArrayList<PayChannel>) {
                view.bindChannelData(response)
            }
        })
    }




    fun commitOrder( billsOrderCommit: BillsOrderCommit) {

        requestApi(mRetrofit.billsCommitOrder(billsOrderCommit),object :BaseMvpObserver<BillsOrderInfo>(view){
            override fun onSuccess(response: BillsOrderInfo) {
                view.commitOrderSuccess(response)
            }

        })


    }

}
