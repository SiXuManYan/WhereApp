package com.jcs.where.features.gourmet.restaurant.packages;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.gourmet.dish.DishDetailResponse;

/**
 * Created by Wangsw  2021/4/6 14:36.
 */
public interface SetMealView extends BaseMvpView {
    void bindData(DishDetailResponse response);
}
