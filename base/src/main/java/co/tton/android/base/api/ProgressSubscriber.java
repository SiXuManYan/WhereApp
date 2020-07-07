package co.tton.android.base.api;

import android.content.Context;

import rx.Observer;
import rx.Subscriber;

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressDialogHandler.ProgressCancelListener {

    private Observer<T> mObserver;
    private ProgressDialogHandler mProgressDialogHandler;

    public ProgressSubscriber(Context context, Observer<T> observer) {
        mObserver = observer;
        mProgressDialogHandler = new ProgressDialogHandler(context, this);
    }

    private void show(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismiss(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        show();
    }

    @Override
    public void onCompleted() {
        dismiss();
        if (mObserver != null) {
            mObserver.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        dismiss();
        if (mObserver != null) {
            mObserver.onError(e);
        }
    }

    @Override
    public void onNext(T t) {
        if (mObserver != null) {
            mObserver.onNext(t);
        }
    }

    @Override
    public void onCancelProgress() {
        if (!isUnsubscribed()) {
            unsubscribe();
        }
    }

}