package com.jcs.where.features.account.login;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonElement;
import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.request.account.LoginRequest;
import com.jcs.where.api.request.SendCodeRequest;
import com.jcs.where.api.request.account.ThreePartyLoginRequest;
import com.jcs.where.api.response.LoginResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.MobUtil;
import com.jcs.where.utils.SPKey;

import java.util.concurrent.TimeUnit;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.google.GooglePlus;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Wangsw  2021/1/28 16:43.
 */
public class LoginPresenter extends BaseMvpPresenter {

    private final LoginView mView;

    public LoginPresenter(LoginView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }


    /**
     * 处理登录
     *
     * @param isVerifyMode 是否是验证码模式
     * @param prefix       账号号前缀
     * @param account      账号
     * @param verifyCode   验证码
     * @param password     密码
     */
    public void handleLogin(boolean isVerifyMode, String prefix, String account, String verifyCode, String password) {

        if (FeaturesUtil.isWrongPhoneNumber(prefix, account)) {
            return;
        }

        if (isVerifyMode) {
            // 验证码
            if (TextUtils.isEmpty(verifyCode)) {
                ToastUtils.showShort(StringUtils.getString(R.string.verify_code_hint));
                return;
            }
            login(Constant.LOGIN_TYPE_VERIFY_CODE, account, verifyCode, password);
        } else {
            // 密码
            if (FeaturesUtil.isWrongPasswordFormat(password)) {
                return;
            }
            login(Constant.LOGIN_TYPE_PASSWORD, account, verifyCode, password);
        }


    }


    /**
     * 验证码登录
     * 当登录接口返回404时，注册接口
     *
     * @param loginType  登录类型 （1：手机验证码，2：手机密码）
     * @param account    账号
     * @param verifyCode 验证码
     * @param password   密码
     */
    public void login(int loginType, String account, String verifyCode, String password) {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setPhone(account);
        loginRequest.setType(loginType);


        switch (loginType) {
            case Constant.LOGIN_TYPE_VERIFY_CODE:
                loginRequest.setVerificationCode(verifyCode);
                break;
            case Constant.LOGIN_TYPE_PASSWORD:
                loginRequest.setPassword(password);
                break;
            default:
                break;
        }

        requestApi(mRetrofit.login(loginRequest), new BaseMvpObserver<LoginResponse>(mView) {
            @Override
            protected void onSuccess(LoginResponse response) {
                String token = response.getToken();
                saveToken(token);
                mView.LoginSuccess();
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                super.onError(errorResponse);
                int errCode = errorResponse.getErrCode();

                // 验证码登录，返回404时是新用户，需要走注册接口
                if (loginType == Constant.LOGIN_TYPE_VERIFY_CODE && errCode == 404) {
                    mView.guideRegister(account, verifyCode);
                }

            }
        });

    }


    /**
     * 获取验证码
     *
     * @param account
     * @param getVerifyTv
     */
    public void getVerifyCode(String prefix, String account, TextView getVerifyTv) {
        if (FeaturesUtil.isWrongPhoneNumber(prefix, account)) {
            return;
        }

        SendCodeRequest request = new SendCodeRequest();
        request.setPhone(account);
        request.setCountryCode(prefix);
        request.setType(Constant.VERIFY_CODE_TYPE_1_LOGIN);

        requestApi(mRetrofit.getVerifyCode(request), new BaseMvpObserver<JsonElement>(mView) {
            @Override
            protected void onSuccess(JsonElement response) {

                getVerifyTv.setClickable(false);
                countdown(getVerifyTv, StringUtils.getString(R.string.get_again));
                ToastUtils.showShort(R.string.verify_code_send_success);
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                super.onError(errorResponse);
                getVerifyTv.setClickable(true);

            }
        });
    }

    /**
     * 倒计时
     *
     * @param countdownView 倒计时显示的view
     * @param defaultStr    默认显示的文字
     */
    private void countdown(TextView countdownView, String defaultStr) {
        Flowable.intervalRange(0, Constant.WAIT_DELAYS, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    String string = StringUtils.getString(R.string.count_down_format, Constant.WAIT_DELAYS - aLong);
                    countdownView.setText(string);
                })
                .doOnComplete(() -> {
                    countdownView.setText(defaultStr);
                    countdownView.setEnabled(true);
                }).subscribe();
    }

    /**
     * 保存token
     *
     * @param token
     */
    private void saveToken(String token) {
        CacheUtil.cacheWithCurrentTime(SPKey.K_TOKEN, token);
    }


    /**
     * 第三方授权
     *
     * @param platformName 平台名称
     * @see Facebook
     * @see cn.sharesdk.google.GooglePlus
     */
    public void threePartyAuthorize(String platformName) {

        MobUtil.authorize(ShareSDK.getPlatform(platformName), db -> {
            // 授权成功
            String userName = db.getUserName();
            String userId = db.getUserId();
            String userIcon = db.getUserIcon();
            Log.d("第三方登录", "userName == " + userName);
            Log.d("第三方登录", "userId == " + userId);
            Log.d("第三方登录", "userIcon == " + userIcon);
            threePartyLogin(platformName ,db);

        });
    }

    /**
     * 第三方登录
     */
    private void threePartyLogin(String platformName, PlatformDb db) {

        int loginType = 0;
        if (platformName.equals(Facebook.NAME)) {
            loginType = ThreePartyLoginRequest.TYPE_FACEBOOK;
        }

        if (platformName.equals(GooglePlus.NAME)) {
            loginType = ThreePartyLoginRequest.TYPE_GOOGLE;
        }


        ThreePartyLoginRequest request = ThreePartyLoginRequest.Builder.aThreePartyLoginRequest()
                .type(loginType)
                .nickname(db.getUserName())
                .open_id(db.getUserId())
                .avatar(db.getUserIcon()).build();

        int finalLoginType = loginType;
        requestApi(mRetrofit.threePartyLogin(request), new BaseMvpObserver<LoginResponse>(mView) {
            @Override
            protected void onSuccess(LoginResponse response) {
                String token = response.getToken();
                saveToken(token);
                mView.LoginSuccess();
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                super.onError(errorResponse);
                int errCode = errorResponse.getErrCode();

                // 404：账号不存在，需要跳转绑定手机号界面
                if (errCode == 404) {
                    mView.guideToAccountBind(db, finalLoginType);
                }
            }
        });
    }


}
