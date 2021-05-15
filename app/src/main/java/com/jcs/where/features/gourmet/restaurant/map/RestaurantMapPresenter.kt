package com.jcs.where.features.gourmet.restaurant.map

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse
import com.jcs.where.utils.CacheUtil
import java.util.*

/**
 * Created by Wangsw  2021/5/14 15:19.
 *
 */
class RestaurantMapPresenter(val view: RestaurantMapView) : BaseMvpPresenter(view) {


    fun getList(category_id: String) {
        val location = CacheUtil.getMyCacheLocation()


        requestApi(mRetrofit.getRestaurantMapList(
                "",
                location.latitude.toString(),
                location.longitude.toString(),
                category_id), object : BaseMvpObserver<ArrayList<RestaurantResponse>>(view) {

            override fun onSuccess(response: ArrayList<RestaurantResponse>) {

                view.bindList(response)
            }

        })
    }
}