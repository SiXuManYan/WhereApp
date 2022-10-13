package com.jcs.where.features.integral.activitys

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.integral.IntegralTag

/**
 * Created by Wangsw  2022/9/21 10:08.
 *
 */
interface ActivityCenterView : BaseMvpView {
    fun bindTab(response: ArrayList<IntegralTag>)

}

class ActivityCenterPresenter(private var view: ActivityCenterView) : BaseMvpPresenter(view) {


    fun getTab() {
        requestApi(mRetrofit.activityCenterTab(), object : BaseMvpObserver<ArrayList<IntegralTag>>(view) {
            override fun onSuccess(response: ArrayList<IntegralTag>) {
                response.forEachIndexed { index, integralTag ->
                    if (index == 0) {
                        integralTag.nativeIsSelected = true
                    }
                }
                view.bindTab(response)
            }
        })
    }


}