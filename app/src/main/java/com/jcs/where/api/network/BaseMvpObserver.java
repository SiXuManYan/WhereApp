package com.jcs.where.api.network;

import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;

/**
 * Created by Wangsw  2021/1/26 15:31.
 */
public abstract class BaseMvpObserver<T> extends BaseObserver<T> {

    private final BaseMvpView view;

    public BaseMvpObserver(BaseMvpView view) {
        this.view = view;
    }


    @Override
    protected void onError(ErrorResponse errorResponse) {
        view.onError(errorResponse);
    }





}
