package com.jcs.where.features.gourmet.restaurant.packages;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.response.gourmet.dish.DishDetailResponse;

/**
 * Created by Wangsw  2021/4/6 14:37.
 */
public class SetMealPresenter extends BaseMvpPresenter {

    private SetMealView view;

    public SetMealPresenter(SetMealView view) {
        super(view);
        this.view = view;
    }

    public void getDetail(String eatInFoodId) {
        requestApi(mRetrofit.getDishDetail(eatInFoodId), new BaseMvpObserver<DishDetailResponse>(view) {
            @Override
            protected void onSuccess(DishDetailResponse response) {
                view.bindData(response);
            }
        });
    }
}
