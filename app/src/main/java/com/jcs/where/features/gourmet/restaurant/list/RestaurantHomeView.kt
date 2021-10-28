package com.jcs.where.features.gourmet.restaurant.list

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse

/**
 * Created by Wangsw  2021/3/24 13:57.
 */
interface RestaurantHomeView : BaseMvpView, OnLoadMoreListener,
    OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {
    fun bindList(data: MutableList<RestaurantResponse>, isLastPage: Boolean)
}