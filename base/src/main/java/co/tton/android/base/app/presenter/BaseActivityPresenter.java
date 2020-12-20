package co.tton.android.base.app.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.jcs.where.base.BaseActivity;
import co.tton.android.base.manager.CompositeSubscriptionHelper;
import rx.Subscription;

public class BaseActivityPresenter<T> {

    protected BaseActivity mActivity;

    private CompositeSubscriptionHelper mCompositeSubscriptionHelper;

    public void setActivity(BaseActivity activity) {
        mActivity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        mCompositeSubscriptionHelper = CompositeSubscriptionHelper.newInstance();
    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }

    public void onDestroy() {
        mCompositeSubscriptionHelper.unsubscribe();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void addSubscription(Subscription subscription) {
        mCompositeSubscriptionHelper.addSubscription(subscription);
    }
}