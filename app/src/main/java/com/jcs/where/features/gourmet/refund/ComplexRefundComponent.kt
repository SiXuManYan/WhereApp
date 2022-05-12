package com.jcs.where.features.gourmet.refund

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.RemitId

/**
 * Created by Wangsw  2022/5/12 9:33.
 *
 */
interface  ComplexRefundView : BaseMvpView {
    fun refundSuccess()

}


class  ComplexRefundPresenter( var view :ComplexRefundView) :BaseMvpPresenter(view){

    /**
     * 申请退款
     */
    fun refundOrder(orderId: Int,remitId:Int) {


        requestApi(mRetrofit.delicacyOrderRefund(orderId, RemitId().apply { remit_id = remitId }), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.refundSuccess()
            }

        })
    }


}
