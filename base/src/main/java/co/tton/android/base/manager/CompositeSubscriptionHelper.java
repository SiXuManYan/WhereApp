package co.tton.android.base.manager;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class CompositeSubscriptionHelper {
    private CompositeSubscription mCompositeSubscription;

    public static CompositeSubscriptionHelper newInstance() {
        return new CompositeSubscriptionHelper();
    }

    private CompositeSubscriptionHelper() {

    }

    public void addSubscription(Subscription subscription) {
        if (subscription == null) return;

        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    // 取消注册，以避免内存泄露
    public void unsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
