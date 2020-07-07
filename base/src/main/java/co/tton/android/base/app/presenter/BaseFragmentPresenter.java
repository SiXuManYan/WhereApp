package co.tton.android.base.app.presenter;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import co.tton.android.base.app.fragment.BaseFragment;
import co.tton.android.base.manager.CompositeSubscriptionHelper;
import rx.Subscription;

public abstract class BaseFragmentPresenter {

    protected BaseFragment mFragment;

    protected CompositeSubscriptionHelper mCompositeSubscriptionHelper;

    public void setFragment(BaseFragment fragment) {
        mFragment = fragment;
    }

    public void onAttach(Context context) {
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public void initContentView(View view) {
        mCompositeSubscriptionHelper = CompositeSubscriptionHelper.newInstance();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onDestroyView() {
        mCompositeSubscriptionHelper.unsubscribe();
    }

    public void onDestroy() {
    }

    public void onDetach() {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void addSubscription(Subscription subscription) {
        mCompositeSubscriptionHelper.addSubscription(subscription);
    }
}
