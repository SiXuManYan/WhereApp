package com.jiechengsheng.city.features.gourmet.restaurant.list

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.gourmet.restaurant.RestaurantResponse
import com.jiechengsheng.city.bean.RestaurantListRequest
import java.util.*

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
            view,page
        ) {
            override fun onSuccess(response: PageResponse<RestaurantResponse>) {
                val isLastPage = response.lastPage == page
                val data = response.data
                data.forEach {
                    it.contentType = RestaurantResponse.CONTENT_TYPE_COMMON
                }
                view.bindList(data.toMutableList(), isLastPage)
            }
        })
    }

    fun getMakerData(request: RestaurantListRequest) {
        requestApi(mRetrofit.getRestaurantMapMarker(
            request.trading_area_id,
            request.per_price,
            request.service,
            request.sort,
            request.search_input,
            request.lat,
            request.lng,
            request.category_id
        ), object : BaseMvpObserver<ArrayList<RestaurantResponse>>(view) {
            override fun onSuccess(response: ArrayList<RestaurantResponse>) {
                response.forEach {
                    it.contentType = RestaurantResponse.CONTENT_TYPE_CARD
                }
                view.bindMakerList(response.toMutableList())
            }

        })
    }
}