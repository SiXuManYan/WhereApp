package com.jcs.where.features.feedback.question

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.feedback.FeedbackCategoryAndQuestion

/**
 * Created by Wangsw  2022/10/12 17:03.
 *
 */
interface QuestionView :BaseMvpView{

    fun bindView(response: java.util.ArrayList<FeedbackCategoryAndQuestion>){

    }
}

class QuestionPresenter(private var view: QuestionView):BaseMvpPresenter(view){



    fun feedbackQuestion(categoryId: Int) {
        requestApi(mRetrofit.feedbackQuestion(categoryId,"") , object : BaseMvpObserver<ArrayList<FeedbackCategoryAndQuestion>>(view){
            override fun onSuccess(response: ArrayList<FeedbackCategoryAndQuestion>) {
                view.bindView(response)
            }

        })
    }

}