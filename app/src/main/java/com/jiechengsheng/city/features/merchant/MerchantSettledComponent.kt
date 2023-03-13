package com.jiechengsheng.city.features.merchant

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.merchant.MerchantSettledData

/**
 * Created by Wangsw  2021/11/19 14:17.
 *
 */
interface MerchantSettledView : BaseMvpView {
    fun bindData(response: MerchantSettledData?)

}

class MerchantSettledPresenter(var view: MerchantSettledView) : BaseMvpPresenter(view) {

    fun getData() {
        requestApi(mRetrofit.merchantSettled, object : BaseMvpObserver<MerchantSettledData?>(view) {
            override fun onSuccess(response: MerchantSettledData?) {
                view.bindData(response)
            }
        })
    }

}