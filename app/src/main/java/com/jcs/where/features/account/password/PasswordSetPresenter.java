package com.jcs.where.features.account.password;

import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.request.RegisterRequest;
import com.jcs.where.api.response.LoginResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.SPKey;

/**
 * Created by Wangsw  2021/1/29 16:51.
 */
public class PasswordSetPresenter extends BaseMvpPresenter {

    private final PasswordSetView mView;

    public PasswordSetPresenter(PasswordSetView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }

    /**
     * 注册
     *
     * @param account    账号
     * @param verifyCode 验证码
     * @param countryCode   国家码
     */
    public void register(String account, String verifyCode, String password ,String countryCode) {

        if (FeaturesUtil.isWrongPhoneNumber(countryCode, account)) {
            return;
        }

        RegisterRequest build = RegisterRequest.Builder.aRegisterRequest()
                .phone(account)
                .verification_code(verifyCode)
                .password(password)
                .build();
        requestApi(mRetrofit.register(build), new BaseMvpObserver<LoginResponse>(mView) {
            @Override
            protected void onSuccess(LoginResponse response) {
                String token = response.getToken();
                CacheUtil.cacheWithCurrentTime(SPKey.K_TOKEN, token);
                mView.registerSuccess();
            }
        });


    }

}
