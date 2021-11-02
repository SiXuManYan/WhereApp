package com.jcs.where.features.gourmet.restaurant.detail


import com.google.gson.JsonElement
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.CollectionRestaurantRequest
import com.jcs.where.api.response.gourmet.restaurant.RestaurantDetailResponse
import com.jcs.where.utils.CacheUtil


/**
 * Created by Wangsw  2021/4/1 10:28.
 */
interface RestaurantDetailView : BaseMvpView {

    fun bindData(response: RestaurantDetailResponse)

    fun collectionHandleSuccess(collectionStatus: Boolean)
}

/**
 * Created by Wangsw  2021/4/1 10:28.
 */
class RestaurantDetailPresenter(private val view: RestaurantDetailView) : BaseMvpPresenter(view) {

    fun getDetail(restaurantId: Int) {
        val latLng = CacheUtil.getSafeSelectLatLng()
        requestApi(mRetrofit.getRestaurantDetail(restaurantId, latLng.latitude, latLng.longitude),
            object : BaseMvpObserver<RestaurantDetailResponse>(view) {
                 override fun onSuccess(response: RestaurantDetailResponse) {

                    view.bindData(response)
                }
            })
    }




    fun collection(restaurantId: Int) {
        val request = CollectionRestaurantRequest()
        request.restaurant_id = restaurantId.toString()
        requestApi(mRetrofit.collectsRestaurant(request), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(true)
            }
        })
    }

    fun unCollection(restaurantId: Int) {
        val request = CollectionRestaurantRequest()
        request.restaurant_id = restaurantId.toString()
        requestApi(mRetrofit.unCollectsRestaurant(request), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement) {
                view.collectionHandleSuccess(false)
            }
        })
    }
}