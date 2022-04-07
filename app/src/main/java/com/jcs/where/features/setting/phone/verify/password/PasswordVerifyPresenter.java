package com.jcs.where.features.setting.phone.verify.password;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonElement;
import com.jcs.where.R;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.storage.entity.User;

/**
 * Created by Wangsw  2021/2/4 15:08.
 */
public class PasswordVerifyPresenter extends BaseMvpPresenter {

    private PasswordVerifyView mView;

    public PasswordVerifyPresenter(PasswordVerifyView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;


    }

    public void checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort(R.string.enter_password);
            return;
        }
        String phone = User.getInstance().phone;

        requestApi(mRetrofit.checkPassword(1, password, phone, ""), new BaseMvpObserver<JsonElement>(mView) {
            @Override
            protected void onSuccess(JsonElement response) {
                mView.passwordCheckPass();
            }
        });


    }
}
