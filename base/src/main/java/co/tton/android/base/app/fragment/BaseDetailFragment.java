package co.tton.android.base.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import co.tton.android.base.R;

import co.tton.android.base.view.CommonLayout;
import rx.Subscription;

public abstract class BaseDetailFragment extends BaseLazyLoadFragment {

    protected CommonLayout mCommonLayout;

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mCommonLayout = view.findViewById(R.id.common_layout);
        mCommonLayout.setContentLayoutId(getContentLayoutId());
        mCommonLayout.showContent();
        mCommonLayout.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDetail();
            }
        });
        return view;
    }

    protected int getLayoutId() {
        return R.layout.fragment_common_layout;
    }

    protected abstract int getContentLayoutId();

    @Override
    public void initData() {
        setDataInitiated(true);
        requestDetail();
    }

    @Override
    public void cancelInit() {

    }

    private void requestDetail() {
        mCommonLayout.showLoading();
        Subscription subscription = getDetailRequest();
        addSubscription(subscription);
    }

    protected abstract Subscription getDetailRequest();
}
