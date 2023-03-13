package com.jiechengsheng.city.features.integral.activitys.child

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.integral.IntegralGood

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