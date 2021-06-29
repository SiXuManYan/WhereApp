package com.jcs.where.features.bills.hydropower.record

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.hydropower.PaymentRecord
import com.jcs.where.api.response.order.OrderListResponse

/**
 * Created by Wangsw  2021/6/28 16:16.
 *
 */
interface PaymentRecordView : BaseMvpView {
    fun bindList(toMutableList: MutableList<PaymentRecord>, lastPage: Boolean)

}


class PaymentRecordPresenter(private var view: PaymentRecordView) : BaseMvpPresenter(view) {
    fun getData(page: Int) {
        requestApi(mRetrofit.getPaymentRecord(page),object : BaseMvpObserver<PageResponse<PaymentRecord>>(view){
            override fun onSuccess(response: PageResponse<PaymentRecord>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                val toMutableList = data.toMutableList()
                view.bindList(toMutableList, isLastPage)

            }

        })

    }


}