package com.jiechengsheng.city.features.job.employer

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.job.EmployerEmail
import com.jiechengsheng.city.api.response.job.EmployerRequest

/**
 * Created by Wangsw  2022/10/25 13:55.
 *
 */
interface  EmployerView :BaseMvpView {
    fun applySuccess()
    fun showEmployerEmail(email: String)
}


class  EmployerPresenter (private var view: EmployerView):BaseMvpPresenter(view){

    fun applyEmployer(request: EmployerRequest) {

        requestApi(mRetrofit.employerApply(request),object : BaseMvpObserver <JsonElement>(view){
            override fun onSuccess(response: JsonElement) {
                view.applySuccess()
            }

        })
    }
   fun getEmployerEmail() {

        requestApi(mRetrofit.employersEmail,object : BaseMvpObserver <EmployerEmail>(view){
            override fun onSuccess(response: EmployerEmail) {
                view.showEmployerEmail(response.email)
            }

        })
    }

}