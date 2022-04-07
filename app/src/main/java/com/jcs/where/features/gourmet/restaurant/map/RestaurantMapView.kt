package com.jcs.where.features.gourmet.restaurant.map

import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse
import com.jcs.where.api.response.search.SearchResultResponse
import java.util.ArrayList

/**
 * Created by Wangsw  2021/5/14 15:18.
 *
 */
interface RestaurantMapView :BaseMvpView{
    fun bindList(response: ArrayList<RestaurantResponse>)
    fun bindSearchResult(data: ArrayList<SearchResultResponse>)
}