package com.jcs.where.features.gourmet.restaurant.list;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/3/24 13:57.
 */
public interface RestaurantListView extends BaseMvpView {
    void bindList(List<RestaurantResponse> data, boolean isLastPage);
}
