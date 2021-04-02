package com.jcs.where.features.gourmet.restaurant.detail;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantDetailResponse;

/**
 * Created by Wangsw  2021/4/1 10:28.
 */
public interface RestaurantDetailView extends BaseMvpView {
    void bindData(RestaurantDetailResponse response);
}
