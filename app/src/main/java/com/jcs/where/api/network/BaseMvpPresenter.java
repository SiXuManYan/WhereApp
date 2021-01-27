package com.jcs.where.api.network;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.JcsResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Wangsw  2021/1/26 11:31.
 */
public class BaseMvpPresenter extends BaseModel {


    private BaseMvpView mBaseMvpView;

    public BaseMvpPresenter(BaseMvpView baseMvpView) {
        mBaseMvpView = baseMvpView;
    }



    protected <T> void requestApi(Observable<JcsResponse<T>> observable, BaseMvpObserver<T> observer) {
        dealResponse(observable, observer);
    }

    public void detachView() {
        mBaseMvpView = null;
        CompositeDisposable compositeDisposable = mObserver.getCompositeDisposable();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

}
