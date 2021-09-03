package com.jcs.where.features.search.yellow

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.api.response.PageResponse
import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2021/9/3 15:02.
 *
 */

interface YellowPageSearchResultView : BaseMvpView {
    fun bindData(response: MutableList<MechanismResponse>, isLastPage: Boolean)

}

class YellowPageSearchResultPresenter(private var view: YellowPageSearchResultView) : BaseMvpPresenter(view) {

    fun getData(page: Int, categoryId: String, search: String) {

        requestApi(mRetrofit.getMechanismListById2(page, categoryId, search, Constant.LAT, Constant.LNG),
            object : BaseMvpObserver<PageResponse<MechanismResponse>>(view) {
                override fun onSuccess(response: PageResponse<MechanismResponse>) {

                    val total = response.total
                    val isLastPage = response.lastPage == page
                    val data = response.data

                    view.bindData(data , isLastPage)
                }
            })
    }


}