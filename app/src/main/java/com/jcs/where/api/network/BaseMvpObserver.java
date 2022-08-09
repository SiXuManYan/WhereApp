package com.jcs.where.api.network;

import androidx.annotation.NonNull;

import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.JcsResponse;

import io.reactivex.disposables.Disposable;

/**
 * Created by Wangsw  2021/1/26 15:31.
 */
public abstract class BaseMvpObserver<T> extends BaseObserver<T> {

    private final BaseMvpView view;

    public boolean showLoading = true;

    public BaseMvpObserver(BaseMvpView view) {
        this.view = view;
    }



    public BaseMvpObserver(BaseMvpView view ,Boolean showLoading) {
        this.view = view;
        this.showLoading = showLoading;
    }


    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        if (showLoading){
            view.showLoading();
        }
    }




    @Override
    public void onNext(@NonNull JcsResponse<T> tJcsResponse) {
        super.onNext(tJcsResponse);
        view.hideLoading();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        view.hideLoading();
    }

    @Override
    protected void onError(ErrorResponse errorResponse) {
        view.onError(errorResponse);
        if (showLoading) {
            view.showLoading();
        }
    }

}
