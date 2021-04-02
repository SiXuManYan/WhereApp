package com.jcs.where.features.gourmet.restaurant.detail;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantDetailResponse;
import com.jcs.where.utils.Constant;

/**
 * Created by Wangsw  2021/4/1 10:28.
 */
public class RestaurantDetailPresenter extends BaseMvpPresenter {

    private RestaurantDetailView view;

    public RestaurantDetailPresenter(RestaurantDetailView view) {
        super(view);
        this.view = view;
    }

    public void getDetail(String restaurantId) {
        requestApi(mRetrofit.getRestaurantDetail(restaurantId, Constant.LAT + "", Constant.LNG + ""), new BaseMvpObserver<RestaurantDetailResponse>(view) {
            @Override
            protected void onSuccess(RestaurantDetailResponse response) {
                if (response == null) {
                    return;
                }
                view.bindData(response);
            }
        });


    }
}
