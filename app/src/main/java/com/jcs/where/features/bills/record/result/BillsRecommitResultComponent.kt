package com.jcs.where.features.bills.record.result

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillStatus

/**
 * Created by Wangsw  2022/6/11 15:01.
 *
 */

interface BillsRecommitResultView : BaseMvpView {
    fun bindStatus(response: BillStatus)

}

class BillsRecommitResultPresenter(private var view: BillsRecommitResultView) : BaseMvpPresenter(view) {

    fun getStatus(orderId: Int) {
        requestApi(mRetrofit.billsStatus(orderId), object : BaseMvpObserver<BillStatus>(view) {
            override fun onSuccess(response: BillStatus) {
                view.bindStatus(response)
            }

        })
    }
}