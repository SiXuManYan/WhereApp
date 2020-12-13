package co.tton.android.base.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.tton.android.base.app.presenter.linker.FragmentLinker;
import co.tton.android.base.dialog.CustomProgressDialog;
import co.tton.android.base.manager.CompositeSubscriptionHelper;
import rx.Subscription;

public abstract class BaseFragment extends Fragment {

//    private FragmentLinker mLinker = new FragmentLinker();

//    private CompositeSubscriptionHelper mCompositeSubscriptionHelper;

    public static CustomProgressDialog dialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mLinker.register(this);
//        mLinker.onAttach(context);
//        mCompositeSubscriptionHelper = CompositeSubscriptionHelper.newInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mLinker.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initContentView(inflater, container, savedInstanceState);
//        mLinker.initContentView(view);
        return view;
    }

    protected abstract View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mLinker.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
//        mLinker.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mLinker.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mLinker.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mLinker.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mLinker.onDestroyView();
//        mCompositeSubscriptionHelper.unsubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mLinker.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mLinker.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mLinker.onOptionsItemSelected(item)) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        mLinker.onSaveInstanceState(outState);
    }

    public void addSubscription(Subscription subscription) {
//        mCompositeSubscriptionHelper.addSubscription(subscription);
    }

    public void showLoading(String msg) {
        if (dialog != null && dialog.isShowing()) {
        } else {
            dialog = new CustomProgressDialog(getContext(), msg);
//            dialog.setCancelable(isCancelable);
            dialog.show();
        }
    }

    public void showLoading() {
        if (dialog != null && dialog.isShowing()) {
        } else {
            dialog = new CustomProgressDialog(getContext(), "");
//            dialog.setCancelable(isCancelable);
            dialog.show();
        }
    }

    public void stopLoading() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

        } catch (Exception e) {

        }
    }



}
