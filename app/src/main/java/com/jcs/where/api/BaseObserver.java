package com.jcs.where.api;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {
    private CompositeDisposable mCompositeDisposable;

    public BaseObserver() {
        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.mCompositeDisposable.add(d);
    }

    @Override
    public void onComplete() {
        this.mCompositeDisposable.clear();
    }
}
