package com.jiechengsheng.city.features.travel.map.child

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.travel.TravelChild
import com.jiechengsheng.city.utils.CacheUtil

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
            safeSelectLatLng.longitude,
            ""
        ), object : BaseMvpObserver<PageResponse<TravelChild>>(view,page) {
            override fun onSuccess(response: PageResponse<TravelChild>) {

                val isLastPage = response.lastPage == page
                val data = response.data
                view.bindData(data.toMutableList(), isLastPage)
            }
        })


    }

}