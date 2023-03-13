package com.jiechengsheng.city.features.gourmet.restaurant.detail


import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.CollectionRestaurantRequest
import com.jiechengsheng.city.api.response.gourmet.restaurant.RestaurantDetailResponse
import com.jiechengsheng.city.api.response.other.CartNumberResponse
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.CacheUtil


/**
 * Created by Wangsw  2021/4/1 10:28.
 */
interface RestaurantDetailView : BaseMvpView {

    fun bindData(response: RestaurantDetailResponse)

    fun collectionHandleSuccess(collectionStatus: Boolean)
    fun bindCartCount(nums: Int)
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

    fun getCartCount(){
        if (!User.isLogon()) {
            return
        }
        requestApi(mRetrofit.foodCartCount, object : BaseMvpObserver<CartNumberResponse>(view) {
            override fun onSuccess(response: CartNumberResponse) {
                view.bindCartCount(response.nums)
            }
        })
    }

}