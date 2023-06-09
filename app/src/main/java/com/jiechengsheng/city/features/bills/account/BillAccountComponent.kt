package com.jiechengsheng.city.features.bills.account

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.bills.BillAccount
import com.jiechengsheng.city.api.response.bills.BillAccountEdit

/**
 * Created by Wangsw  2022/12/9 13:48.
 *
 */

interface BillAccountView : BaseMvpView {
    fun bindAccount(response: ArrayList<BillAccount>) {}
    fun handleSuccess(response: JsonElement) {}


}

class BillAccountPresenter(private var view: BillAccountView) : BaseMvpPresenter(view) {

    fun getBillAccount(module: Int) {
        requestApi(mRetrofit.getBillsAccountHistory(module), object : BaseMvpObserver<ArrayList<BillAccount>>(view) {
            override fun onSuccess(response: ArrayList<BillAccount>) {
                view.bindAccount(response)
            }
        })
    }


    fun addAccount(billType: Int, first: String, second: String, isDefault: Boolean) {

        val apply = BillAccountEdit().apply {
            first_field = first
            second_field = second
            status = if (isDefault) {
                1
            } else {
                0
            }
            module = billType
        }

        requestApi(mRetrofit.addBillsAccount(apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.handleSuccess(response)
            }
        })

    }


    fun editAccount(billType: Int, first: String, second: String, isDefault: Boolean, id: Int) {

        val apply = BillAccountEdit().apply {
            first_field = first
            second_field = second
            status = if (isDefault) {
                1
            } else {
                0
            }
            module = billType
        }

        requestApi(mRetrofit.editBillsAccount(id, apply), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.handleSuccess(response)
            }
        })

    }


    fun deleteAccount(id: Int) {
        requestApi(mRetrofit.deleteBillsAccount(id), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.handleSuccess(response)
            }
        })

    }
}
