package com.jcs.where.features.account.bind;

import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonElement;
import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.request.SendCodeRequest;
import com.jcs.where.api.request.account.BindPhoneRequest;
import com.jcs.where.api.response.LoginResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.SPKey;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Wangsw  2021/2/1 16:14.
 */
public class BindPhonePresenter extends BaseMvpPresenter {

    private BindPhoneView mView;

    public BindPhonePresenter(BindPhoneView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
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
     * 绑定手机号
     *
     * @param countryCode 国家码
     * @param account     账号
     * @param verifyCode  验证码
     * @param password    密码
     * @param userName    第三方返回用户昵称
     * @param userId      第三方返回用户id
     * @param userIcon    第三方返回用户头像
     * @param loginType
     */
    public void bindPhone(String countryCode, String account, String verifyCode, String password, String userName, String userId, String userIcon, int loginType) {

        if (FeaturesUtil.isWrongPhoneNumber(countryCode, account)) {
            return;
        }
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showShort(StringUtils.getString(R.string.verify_code_hint));
            return;
        }
        if (FeaturesUtil.isWrongPasswordFormat(password)) {
            return;
        }

        BindPhoneRequest request = BindPhoneRequest.Builder.aBindPhoneRequest()
                .type(loginType)
                .nickname(userName)
                .open_id(userId)
                .avatar(userIcon)
                .phone(account)
                .password(password)
                .verification_code(verifyCode)
                .country_code(countryCode)
                .build();


        requestApi(mRetrofit.bindPhone(request), new BaseMvpObserver<LoginResponse>(mView) {
            @Override
            protected void onSuccess(LoginResponse response) {
                String token = response.getToken();
                CacheUtil.cacheWithCurrentTime(SPKey.K_TOKEN, token);
                mView.bindSuccess();
            }
        });


    }


}
