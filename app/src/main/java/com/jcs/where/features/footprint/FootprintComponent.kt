package com.jcs.where.features.footprint

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.footprint.Footprint

/**
 * Created by Wangsw  2021/11/18 16:41.
 *
 */
interface FootprintView : BaseMvpView {
    fun bindData(data: MutableList<Footprint>, lastPage: Boolean)

}

class FootprintPresenter(private var view: FootprintView) : BaseMvpPresenter(view) {
    fun getData(page: Int) {

        requestApi(mRetrofit.getFootprint(page), object : BaseMvpObserver<PageResponse<Footprint>>(view) {
            override fun onSuccess(response: PageResponse<Footprint>) {

                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }
        })
    }

}