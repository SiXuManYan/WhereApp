package com.jcs.where.features.gourmet.restaurant.packages;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonElement;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.request.AddCartRequest;
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

    public void addCart(AddCartRequest request){

        requestApi(mRetrofit.addCartNumber(request), new BaseMvpObserver<JsonElement>(view) {
            @Override
            protected void onSuccess(JsonElement response) {
                ToastUtils.showShort("add success");
            }
        });

    }

}
