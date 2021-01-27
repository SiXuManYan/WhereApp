package com.jcs.where.base.mvp;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.base.BaseActivity;

/**
 * Created by Wangsw  2021/1/26 11:06.
 */
public abstract class BaseMvpActivity<T extends BaseMvpPresenter> extends BaseActivity implements BaseMvpView {




    public T presenter;


    @Override
    protected void initData() {
        // todo dagger 替代
        presenter = (T) new BaseMvpPresenter();
        presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }




    @Override
    public void onError(ErrorResponse errorResponse) {
        int errCode = errorResponse.getErrCode();
        String errMsg = errorResponse.getErrMsg();

        if (errCode <= 0) {
            ToastUtils.showShort(errMsg);
            return;
        }
        if (errCode == 401) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.hint)
                    .setCancelable(false)
                    .setMessage(R.string.login_expired_hint)
                    .setPositiveButton(R.string.login_again, (dialogInterface, i) -> {
                        // todo 跳转至登录页
                    })
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).create().show();
            return;
        }

        if (errMsg.isEmpty()) {
            ToastUtils.showShort(getString(R.string.request_error, errCode));
        } else {
            ToastUtils.showShort(errMsg);
        }

    }
}
