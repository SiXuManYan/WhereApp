package com.jiechengsheng.city.features.payment.result

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.payment.PayStatus
import java.util.ArrayList

/**
 * Created by Wangsw  2022/4/24 17:12.
 *
 */
interface WebPayResultView : BaseMvpView {
    fun bindPayStatus(response: PayStatus)

}

class WebPayResultPresenter(private var view: WebPayResultView) : BaseMvpPresenter(view) {


    fun getPayStatus(moduleType: String, orderIds: ArrayList<Int>) {

       val orderId = if (orderIds.isNotEmpty()) {
           orderIds[0]
        }else {
            0
        }

        requestApi(mRetrofit.getPayStatus(moduleType,orderId),object :BaseMvpObserver<PayStatus>(view){
            override fun onSuccess(response: PayStatus) {
                view.bindPayStatus(response)
            }

        })
    }

}