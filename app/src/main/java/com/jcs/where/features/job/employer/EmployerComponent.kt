package com.jcs.where.features.job.employer

import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.job.EmployerEmail
import com.jcs.where.api.response.job.EmployerRequest

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