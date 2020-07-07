package co.tton.android.base.app.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class BaseLazyLoadFragment extends BaseFragment {

    protected boolean mIsViewInitiated;
    protected boolean mIsDataInitiated;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsViewInitiated = true;
        if (getUserVisibleHint() && !mIsDataInitiated) {
            initData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!mIsDataInitiated && mIsViewInitiated) {
            if (isVisibleToUser) {
                initData();
            } else {
                cancelInit();
            }
        }
    }

    public abstract void initData();

    public abstract void cancelInit();

    public void setDataInitiated(boolean init) {
        mIsDataInitiated = init;
    }
}
