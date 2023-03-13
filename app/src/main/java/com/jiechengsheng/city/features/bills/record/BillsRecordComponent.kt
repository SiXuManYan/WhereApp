package com.jiechengsheng.city.features.bills.record

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.bills.BillRecommit
import com.jiechengsheng.city.api.response.bills.BillsRecord

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
        requestApi(mRetrofit.billsRecord(page), object : BaseMvpObserver<PageResponse<BillsRecord>>(view, page) {
            override fun onSuccess(response: PageResponse<BillsRecord>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                val toMutableList = data.toMutableList()
                view.bindList(toMutableList, isLastPage)

            }

        })
    }


    fun recommit(orderId: Int, orderType: Int) {


        val apply = BillRecommit().apply {
            order_id = orderId
        }

        when (orderType) {
            BillsRecord.TYPE_PHONE -> {
                requestApi(mRetrofit.billsRecommit4Phone(apply), object : BaseMvpObserver<JsonElement>(view) {
                    override fun onSuccess(response: JsonElement) {
                        view.recommitSuccess(orderId)
                    }
                })
            }
            else -> {
                requestApi(mRetrofit.billsRecommit(apply), object : BaseMvpObserver<JsonElement>(view) {
                    override fun onSuccess(response: JsonElement) {
                        view.recommitSuccess(orderId)
                    }

                })
            }
        }


    }

}