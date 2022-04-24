package com.jcs.where.features.payment

import com.google.gson.Gson
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.payment.PayUrl
import com.jcs.where.api.request.payment.PayUrlGet
import com.jcs.where.utils.Constant
import java.util.ArrayList

/**
 * Created by Wangsw  2022/4/24 10:22.
 *
 */
interface WebPayView : BaseMvpView {
    fun bindUrl(redirectUrl: String)

}

class WebParPresenter(private var view: WebPayView) : BaseMvpPresenter(view) {

    fun getPayUrl(useType: Int, orderIds: ArrayList<Int>) {

        /**
         * 0 商城订单
         * 1 水电订单
         * 2 美食
         * 3 外卖
         * 4 酒店
         * 5 新版商城
         */

        val apply = PayUrlGet().apply {
            order_ids = Gson().toJson(orderIds)
            module =   when (useType) {
                Constant.PAY_INFO_BILLS -> PayUrlGet.BILL_PAY
                Constant.PAY_INFO_FOOD -> PayUrlGet.RESTAURANT
                Constant.PAY_INFO_TAKEAWAY -> PayUrlGet.TAKEAWAY
                Constant.PAY_INFO_HOTEL -> PayUrlGet.HOTEL
                Constant.PAY_INFO_MALL -> PayUrlGet.MALL
                else -> ""
            }

        }
        requestApi(mRetrofit.getWebPayUrl(apply),object :BaseMvpObserver<PayUrl>(view){
            override fun onSuccess(response: PayUrl) {

                view.bindUrl(response.redirectUrl)
            }
        })
    }

}

