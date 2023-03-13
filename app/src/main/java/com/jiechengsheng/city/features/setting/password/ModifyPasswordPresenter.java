package com.jiechengsheng.city.features.setting.password;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonElement;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.network.BaseMvpObserver;
import com.jiechengsheng.city.api.network.BaseMvpPresenter;
import com.jiechengsheng.city.api.request.modify.ModifyPasswordRequest;
import com.jiechengsheng.city.utils.FeaturesUtil;

/**
 * Created by Wangsw  2021/2/4 14:33.
 */
public class ModifyPasswordPresenter extends BaseMvpPresenter {

    private ModifyPasswordView mView;

    public ModifyPasswordPresenter(ModifyPasswordView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }

    public void modifyPassword(String oldPassword, String newPassword) {
        if (TextUtils.isEmpty(oldPassword)) {
            ToastUtils.showShort(R.string.old_password_empty_hint);
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            ToastUtils.showShort(R.string.new_password_empty_hint);
            return;
        }
        if (oldPassword.equals(newPassword)) {
            ToastUtils.showShort(R.string.password_modify_same_hint);
            return;
        }

        if (FeaturesUtil.isWrongPasswordFormat(newPassword)) {
            return;
        }


        ModifyPasswordRequest request = ModifyPasswordRequest.Builder.aModifyPasswordRequest()
                .old_password(oldPassword)
                .new_password(newPassword)
                .build();

        requestApi(mRetrofit.modifyPassword(request), new BaseMvpObserver<JsonElement>(mView) {
            @Override
            protected void onSuccess(JsonElement response) {
                mView.modifyPasswordSuccess();
            }
        });


    }
}
