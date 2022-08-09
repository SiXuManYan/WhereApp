package com.jcs.where.base.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.view.empty.EmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Wangsw  2021/1/26 11:06.
 */
public abstract class BaseMvpActivity<T extends BaseMvpPresenter> extends BaseActivity implements BaseMvpView {

    public T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }

    }

    @Override
    protected void onDestroy() {
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
            ToastUtils.showShort(errMsg);
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
