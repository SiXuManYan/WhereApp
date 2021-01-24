package com.jcs.where.login.model;

import com.jcs.where.api.BaseModel;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.request.LoginRequest;
import com.jcs.where.api.request.SendCodeRequest;
import com.jcs.where.api.response.LoginResponse;
import com.jcs.where.api.response.SuccessResponse;

import okhttp3.ResponseBody;
import retrofit2.Response;

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
