package com.jcs.where.features.gourmet.restaurant.list

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse
import com.jcs.where.bean.RestaurantListRequest

/**
 * Created by Wangsw  2021/3/24 13:57.
 */
class RestaurantHomePresenter(private val view: RestaurantHomeView) : BaseMvpPresenter(view) {

    fun getList(page: Int, request: RestaurantListRequest) {
        requestApi(mRetrofit.getRestaurantList(
            page,
            request.trading_area_id,
            request.per_price,
            request.service,
            request.sort,
            request.search_input,
            request.lat,
            request.lng,
            request.category_id
        ), object : BaseMvpObserver<PageResponse<RestaurantResponse>>(
            view
        ) {
             override fun onSuccess(response: PageResponse<RestaurantResponse>) {
                 val isLastPage = response.lastPage == page
                 val data = response.data
                view.bindList(data.toMutableList(), isLastPage)
            }
        })
    }
}