package com.jiechengsheng.city.features.integral.detail

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.integral.IntegralGoodDetail

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