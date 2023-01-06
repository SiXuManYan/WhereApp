package com.jcs.where.features.main

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.MtjDuration
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.BusinessUtils
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/8/12 15:25.
 *
 */
interface MainView : BaseMvpView {
}


class MainPresenter(private var view: MainView) : BaseMvpPresenter(view){


    fun mtjDuration( mtj : MtjDuration){

        val div = BigDecimalUtil.div(BigDecimal(mtj.time), BigDecimal(1000)).toLong()
        mtj.time = div

        requestApi(mRetrofit.mtjDuration(mtj),object :BaseMvpObserver<JsonElement>(view){
            override fun onSuccess(response: JsonElement?) {

            }

        })
    }

}

