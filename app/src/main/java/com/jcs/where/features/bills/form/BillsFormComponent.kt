package com.jcs.where.features.bills.form

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/6/8 17:33.
 *
 */
interface BillsFormView : BaseMvpView {
    fun bindDiscountList(response: ArrayList<String>)

}

class BillsFormPresenter(private var view: BillsFormView) : BaseMvpPresenter(view) {

    fun getDiscountList(billsType: Int) {
        requestApi(mRetrofit.getBillsDiscountList(billsType), object : BaseMvpObserver<ArrayList<String>>(view) {
            override fun onSuccess(response: ArrayList<String>) {
                view.bindDiscountList(response)
            }

        })
    }
}