package com.jiechengsheng.city.features.feedback.question

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.feedback.FeedbackCategoryAndQuestion

/**
 * Created by Wangsw  2022/10/12 17:03.
 *
 */
interface QuestionView :BaseMvpView{

    fun bindView(response: java.util.ArrayList<FeedbackCategoryAndQuestion>){

    }
}

class QuestionPresenter(private var view: QuestionView):BaseMvpPresenter(view){



    fun feedbackQuestion(categoryId: Int, searchName: String) {
        requestApi(mRetrofit.feedbackQuestion(categoryId,searchName) , object : BaseMvpObserver<ArrayList<FeedbackCategoryAndQuestion>>(view){
            override fun onSuccess(response: ArrayList<FeedbackCategoryAndQuestion>) {
                view.bindView(response)
            }

        })
    }

}