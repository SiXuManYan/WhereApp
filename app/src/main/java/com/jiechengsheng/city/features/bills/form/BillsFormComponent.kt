package com.jiechengsheng.city.features.bills.form

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.bills.BillAccount

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
        requestApi(mRetrofit.getBillsDefaultAccount(module), object : BaseMvpObserver<BillAccount?>(view) {
            override fun onSuccess(response: BillAccount?) {
                if (response!=null) {
                    view.bindDefaultAccount(response)
                }
            }

        })
    }

}