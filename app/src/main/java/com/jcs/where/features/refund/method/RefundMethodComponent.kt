package com.jcs.where.features.refund.method

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.IdRequest
import com.jcs.where.api.response.mall.RefundMethod

/**
 * Created by Wangsw  2022/4/25 16:11.
 *
 */

interface RefundMethodView : BaseMvpView {
    fun bindData(toMutableList: MutableList<RefundMethod>)
    fun unBindSuccess()
}

class RefundMethodPresenter(private var view: RefundMethodView) : BaseMvpPresenter(view) {

    fun getRefundMethod(){
        requestApi(mRetrofit.refundMethod,object :BaseMvpObserver<ArrayList<RefundMethod>>(view){
            override fun onSuccess(response: ArrayList<RefundMethod>) {
                view.bindData(response.toMutableList())
            }
        })
    }

    fun unBind(methodId : Int){

        val apply = IdRequest().apply {
            id = methodId
        }

        requestApi(mRetrofit.unbindRefundMethod(apply),object :BaseMvpObserver<JsonElement>(view){
            override fun onSuccess(response: JsonElement) {
                view.unBindSuccess()
            }
        })

    }

}