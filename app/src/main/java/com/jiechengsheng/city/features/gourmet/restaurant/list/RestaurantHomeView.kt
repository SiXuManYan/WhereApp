package com.jiechengsheng.city.features.gourmet.restaurant.list

import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.gourmet.restaurant.RestaurantResponse

/**
 * Created by Wangsw  2021/3/24 13:57.
 */
interface RestaurantHomeView : BaseMvpView,
    OnLoadMoreListener,
    OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener
{


    fun bindList(data: MutableList<RestaurantResponse>, isLastPage: Boolean)
    fun bindMakerList(response: MutableList<RestaurantResponse>)
}