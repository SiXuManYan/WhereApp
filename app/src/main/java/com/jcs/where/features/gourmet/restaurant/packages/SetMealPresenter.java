package com.jcs.where.features.gourmet.restaurant.packages;

import com.jcs.where.api.network.BaseMvpPresenter;

/**
 * Created by Wangsw  2021/4/6 14:37.
 */
public class SetMealPresenter extends BaseMvpPresenter {

    private SetMealView view;

    public SetMealPresenter(SetMealView view) {
        super(view);
        this.view = view;
    }
}
