package com.jcs.where.features.travel.map.child

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.travel.TravelChild
import com.jcs.where.utils.CacheUtil

/**
 * Created by Wangsw  2021/10/18 9:53.
 *
 */

interface TravelChildView : BaseMvpView {
    fun bindData(toMutableList: MutableList<TravelChild>, lastPage: Boolean)

}

class TravelChildPresenter(private var view: TravelChildView) : BaseMvpPresenter(view) {


    fun getData(page: Int, categoryId: Int, searchInput: String?) {

        val safeSelectLatLng = CacheUtil.getSafeSelectLatLng()

        requestApi(mRetrofit.getTravelChildList(
            page,
            categoryId,
            searchInput,
            safeSelectLatLng.latitude,
            safeSelectLatLng.longitude
        ), object : BaseMvpObserver<PageResponse<TravelChild>>(view) {
            override fun onSuccess(response: PageResponse<TravelChild>) {

                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindData(data.toMutableList(), isLastPage)
            }
        })


    }

}