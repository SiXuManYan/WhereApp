package com.jiechengsheng.city.features.payment

import com.google.gson.Gson
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.payment.PayUrl
import com.jiechengsheng.city.api.request.payment.PayUrlGet
import java.util.ArrayList

/**
 * Created by Wangsw  2022/4/24 10:22.
 *
 */
interface WebPayView : BaseMvpView {
    fun bindUrl(redirectUrl: String)

}

class WebParPresenter(private var view: WebPayView) : BaseMvpPresenter(view) {

    fun getPayUrl(moduleType: String, orderIds: ArrayList<Int>) {

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
            module =   moduleType

        }
        requestApi(mRetrofit.getWebPayUrl(apply),object :BaseMvpObserver<PayUrl>(view){
            override fun onSuccess(response: PayUrl) {

                view.bindUrl(response.redirectUrl)
            }


        })
    }

}

