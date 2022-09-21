package com.jcs.where.features.integral.activitys.child

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.integral.IntegralGood

/**
 * Created by Wangsw  2022/9/21 14:40.
 *
 */

interface IntegralChildView :BaseMvpView {
    fun bindData(toMutableList: MutableList<IntegralGood>, lastPage: Boolean)

}

class IntegralChildPresenter(private var view: IntegralChildView) :BaseMvpPresenter(view){


    fun getData( page: Int , type: Int) {
        requestApi(mRetrofit.getIntegralGood(page , type) ,object :BaseMvpObserver<PageResponse<IntegralGood>>(view){
            override fun onSuccess(response: PageResponse<IntegralGood>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }

        })
    }

}