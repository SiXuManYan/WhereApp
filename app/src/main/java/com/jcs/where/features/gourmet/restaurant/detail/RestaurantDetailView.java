package com.jcs.where.features.gourmet.restaurant.detail;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.gourmet.comment.CommentResponse;
import com.jcs.where.api.response.gourmet.dish.DishResponse;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantDetailResponse;

import java.util.List;

/**
 * Created by Wangsw  2021/4/1 10:28.
 */
public interface RestaurantDetailView extends BaseMvpView {
    void bindData(RestaurantDetailResponse response);

    void bindDishData(List<DishResponse> data);

    void bindCommentData(List<CommentResponse> data);
}
