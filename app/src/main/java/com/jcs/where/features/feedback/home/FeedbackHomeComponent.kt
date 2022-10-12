package com.jcs.where.features.feedback.home

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.feedback.FeedbackCategory

/**
 * Created by Wangsw  2022/10/12 16:09.
 *
 */

interface FeedbackHomeView :BaseMvpView{
    fun bindView(response: ArrayList<FeedbackCategory>)

}

class FeedbackHomePresenter(private var view:FeedbackHomeView ) :BaseMvpPresenter(view){
    fun feedBackCatgegory() {

        requestApi(mRetrofit.feedBackCatgegory() , object :BaseMvpObserver<ArrayList<FeedbackCategory>>(view){
            override fun onSuccess(response: ArrayList<FeedbackCategory>) {
                view.bindView(response)
            }

        })


    }

}
