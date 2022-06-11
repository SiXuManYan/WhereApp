package com.jcs.where.features.bills.record

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.bills.BillRecommit
import com.jcs.where.api.response.bills.BillsRecord

/**
 * Created by Wangsw  2022/6/11 13:37.
 *
 */
interface BillsRecordView : BaseMvpView {
    fun bindList(toMutableList: MutableList<BillsRecord>, lastPage: Boolean)
    fun recommitSuccess(orderId: Int)

}

class BillsRecordPresenter(private var view: BillsRecordView) : BaseMvpPresenter(view) {
    fun getData(page: Int) {
        requestApi(mRetrofit.billsRecord(page),object : BaseMvpObserver<PageResponse<BillsRecord>>(view){
            override fun onSuccess(response: PageResponse<BillsRecord>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                val toMutableList = data.toMutableList()
                view.bindList(toMutableList, isLastPage)

            }

        })
    }


    fun recommit(orderId:Int ){
        val apply = BillRecommit().apply {
            order_id = orderId
        }

        requestApi(mRetrofit.billsRecommit(apply),object :BaseMvpObserver<JsonElement>(view){
            override fun onSuccess(response: JsonElement) {
                view.recommitSuccess(orderId)
            }

        })
    }

}