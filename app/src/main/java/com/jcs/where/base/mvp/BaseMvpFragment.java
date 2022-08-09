package com.jcs.where.base.mvp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.view.empty.EmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Wangsw  2021/1/26 15:02.
 */
public abstract class BaseMvpFragment<T extends BaseMvpPresenter> extends BaseFragment implements BaseMvpView {

    public T presenter;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    public void onError(ErrorResponse errorResponse) {

        int errCode = errorResponse.getErrCode();
        String errMsg = errorResponse.getErrMsg();

        if (!emptyViewList.isEmpty()) {
            for (EmptyView emptyView : emptyViewList) {
                emptyView.showNetworkError(null);
            }
        }


        if (errCode <= 0) {
            ToastUtils.showLong(errMsg);
            return;
        }
        if (!errMsg.isEmpty()) {
            ToastUtils.showShort(errMsg);
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {

    }


    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }
}
