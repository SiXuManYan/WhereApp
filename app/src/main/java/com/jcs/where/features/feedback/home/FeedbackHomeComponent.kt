package com.jcs.where.features.feedback.home

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.feedback.FeedbackCategoryAndQuestion

/**
 * Created by Wangsw  2022/10/12 16:09.
 *
 */

interface FeedbackHomeView :BaseMvpView{
    fun bindView(response: ArrayList<FeedbackCategoryAndQuestion>)

}

class FeedbackHomePresenter(private var view:FeedbackHomeView ) :BaseMvpPresenter(view){
    fun feedbackCategory() {
        requestApi(mRetrofit.feedbackCategory() , object :BaseMvpObserver<ArrayList<FeedbackCategoryAndQuestion>>(view){
            override fun onSuccess(response: ArrayList<FeedbackCategoryAndQuestion>) {
                view.bindView(response)
            }

        })
    }

}
