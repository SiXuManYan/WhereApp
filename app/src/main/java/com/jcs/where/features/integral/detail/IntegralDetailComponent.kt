package com.jcs.where.features.integral.detail

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.integral.IntegralGoodDetail

/**
 * Created by Wangsw  2022/9/22 10:24.
 *
 */
interface IntegralDetailView : BaseMvpView {
    fun bindDetail(response: IntegralGoodDetail)

}

class IntegralDetailPresenter(private var view: IntegralDetailView) : BaseMvpPresenter(view) {

    fun getData(goodId: Int) {

        requestApi(mRetrofit.getIntegralGoodDetail(goodId),object :BaseMvpObserver<IntegralGoodDetail>(view){
            override fun onSuccess(response: IntegralGoodDetail) {
                view.bindDetail(response)
            }

        })
    }

}