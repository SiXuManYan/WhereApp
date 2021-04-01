package com.jcs.where.features.gourmet.restaurant.detail;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/4/1 10:28.
 */
public class RestaurantDetailPresenter extends BaseMvpPresenter {

    private  RestaurantDetailView view ;

    public RestaurantDetailPresenter(RestaurantDetailView view) {
        super(view);
        this.view = view ;
    }
}
