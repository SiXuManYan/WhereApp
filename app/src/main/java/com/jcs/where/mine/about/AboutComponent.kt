package com.jcs.where.mine.about

import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.feedback.About

/**
 * Created by Wangsw  2022/10/14 17:04.
 *
 */

interface AboutView : BaseMvpView {
    fun bindMessage(response: About)

}

class AboutPresenter(private var view: AboutView) : BaseMvpPresenter(view) {
    fun getMessage() {
        requestApi(mRetrofit.about() , object :BaseMvpObserver<About>(view,false){
            override fun onSuccess(response: About) {
                view.bindMessage(response)
            }

            override fun onError(errorResponse: ErrorResponse?) = Unit

        })

    }

}