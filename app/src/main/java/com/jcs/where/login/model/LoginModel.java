package com.jcs.where.login.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.request.account.LoginRequest;
import com.jcs.where.api.request.SendCodeRequest;
import com.jcs.where.api.response.LoginResponse;

/**
 * create by zyf on 2021/1/9 2:17 下午
 */
public class LoginModel extends BaseModel {

    public void login(LoginRequest loginRequest, BaseObserver<LoginResponse> observer) {
        dealResponse(mRetrofit.patchLogin(loginRequest), observer);
    }

    public void sendCode(SendCodeRequest sendCodeRequest, BaseObserver<Object> observer) {
        dealResponse(mRetrofit.postSendCode(sendCodeRequest), observer);
    }

}
