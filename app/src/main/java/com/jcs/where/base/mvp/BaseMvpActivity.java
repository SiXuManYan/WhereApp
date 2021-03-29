package com.jcs.where.base.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.BaseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Wangsw  2021/1/26 11:06.
 */
public abstract class BaseMvpActivity<T extends BaseMvpPresenter> extends BaseActivity implements BaseMvpView {


    public T presenter;
//    private Unbinder butterKnifeBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
//        butterKnifeBind = ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (presenter != null) {
            presenter.detachView();
        }
//        if (butterKnifeBind != null) {
//            butterKnifeBind.unbind();
//        }

    }


    @Override
    public void onError(ErrorResponse errorResponse) {
        stopLoading();
        int errCode = errorResponse.getErrCode();
        String errMsg = errorResponse.getErrMsg();

        if (errCode <= 0) {
            ToastUtils.showShort(errMsg);
            return;
        }

        if (errMsg.isEmpty()) {
            ToastUtils.showShort(getString(R.string.request_error, errCode));
        } else {
            ToastUtils.showShort(errMsg);
        }
//        if (errCode == 401) {
//            new AlertDialog.Builder(this)
//                    .setTitle(R.string.hint)
//                    .setCancelable(false)
//                    .setMessage(R.string.login_expired_hint)
//                    .setPositiveButton(R.string.login_again, (dialogInterface, i) -> {
//                        // todo 跳转至登录页
//                    })
//                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
//                        dialogInterface.dismiss();
//                    }).create().show();
//
//        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {

    }


}
