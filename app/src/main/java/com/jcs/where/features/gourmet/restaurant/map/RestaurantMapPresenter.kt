package com.jcs.where.features.gourmet.restaurant.map

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse
import com.jcs.where.api.response.search.SearchResultResponse
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import kotlin.collections.ArrayList

/**
 * Created by Wangsw  2021/5/14 15:19.
 *
 */
class RestaurantMapPresenter(val view: RestaurantMapView) : BaseMvpPresenter(view) {


    fun getList(category_id: String, search: String?) {

        val location = CacheUtil.getMyCacheLocation()
        val testLat = Constant.LAT.toString()
        val testLng = Constant.LNG.toString()

        requestApi(mRetrofit.getRestaurantMapList(
                search,
                location.latitude.toString(),
                location.longitude.toString(),
                category_id), object : BaseMvpObserver<ArrayList<RestaurantResponse>>(view) {

            override fun onSuccess(response: ArrayList<RestaurantResponse>) {

                view.bindList(response)
            }

        })
    }

    fun search(finalInput: String) {
        requestApi(mRetrofit.getSearchResult(finalInput), object : BaseMvpObserver<List<SearchResultResponse>>(view) {
            override fun onSuccess(response: List<SearchResultResponse>) {


                val data = ArrayList<SearchResultResponse>()

                response.forEach {
                    if (it.type == 4 ){
                        data.add(it)
                    }
                }


                view.bindSearchResult(data)
            }

        })
    }
}