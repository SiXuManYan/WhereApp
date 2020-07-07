package co.tton.android.base.manager;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

public class RxBus {

    private static volatile RxBus sInstance;
    private final SerializedSubject<Object, Object> mSubject;

    private RxBus() {
        mSubject = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus get() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    public void post(Object object) {
        mSubject.onNext(object);
    }

    public  <T> Observable<T> toObservable(final Class<T> type) {
        return mSubject.ofType(type);
    }

    public boolean hasObservers() {
        return mSubject.hasObservers();
    }
}