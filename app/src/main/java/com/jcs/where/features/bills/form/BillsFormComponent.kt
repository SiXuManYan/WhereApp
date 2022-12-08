package com.jcs.where.features.bills.form

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillAccount

/**
 * Created by Wangsw  2022/6/8 17:33.
 *
 */
interface BillsFormView : BaseMvpView {
    fun bindDiscountList(response: ArrayList<String>)
    fun bindDefaultAccount(response: BillAccount)

}

class BillsFormPresenter(private var view: BillsFormView) : BaseMvpPresenter(view) {

    fun getDiscountList(billsType: Int) {
        requestApi(mRetrofit.getBillsDiscountList(billsType), object : BaseMvpObserver<ArrayList<String>>(view) {
            override fun onSuccess(response: ArrayList<String>) {
                view.bindDiscountList(response)
            }

        })
    }

    /**
     * @param module  账单类型（1-话费，2-水费，3-电费，4-网费）
     */
    fun getDefaultAccount(module: Int) {
        requestApi(mRetrofit.getBillsDefaultAccount(module - 1), object : BaseMvpObserver<BillAccount>(view) {
            override fun onSuccess(response: BillAccount) {
                view.bindDefaultAccount(response)
            }

        })
    }

}