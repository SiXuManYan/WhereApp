package com.jcs.where.features.merchant

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.merchant.MerchantSettledData
import com.jcs.where.api.request.merchant.MerchantSettledPost

/**
 * Created by Wangsw  2021/11/19 14:17.
 *
 */
interface MerchantSettledView : BaseMvpView {
    fun bindData(response: MerchantSettledData)

}

class MerchantSettledPresenter(var view: MerchantSettledView) : BaseMvpPresenter(view) {

    fun getData() {
        requestApi(mRetrofit.merchantSettled, object : BaseMvpObserver<MerchantSettledData>(view) {
            override fun onSuccess(response: MerchantSettledData) {

                view.bindData(response)
            }
        })
    }

}