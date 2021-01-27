package com.jcs.where.base.mvp;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.base.BaseFragment;

/**
 * Created by Wangsw  2021/1/26 15:02.
 */
public abstract class BaseMvpFragment<T extends BaseMvpPresenter> extends BaseFragment implements BaseMvpView {

    public T presenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
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
        if (errCode == 401) {
            new AlertDialog.Builder(getContext())
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
