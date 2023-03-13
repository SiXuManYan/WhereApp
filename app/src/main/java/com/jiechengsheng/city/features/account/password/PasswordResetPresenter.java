package com.jiechengsheng.city.features.account.password;

import static com.jiechengsheng.city.api.request.account.ResetPasswordRequest.TYPE_PHONE;

import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonElement;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.ErrorResponse;
import com.jiechengsheng.city.api.network.BaseMvpObserver;
import com.jiechengsheng.city.api.network.BaseMvpPresenter;
import com.jiechengsheng.city.api.request.SendCodeRequest;
import com.jiechengsheng.city.api.request.account.ResetPasswordRequest;
import com.jiechengsheng.city.utils.Constant;
import com.jiechengsheng.city.utils.FeaturesUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Wangsw  2021/1/30 10:33.
 */
public class PasswordResetPresenter extends BaseMvpPresenter {


    private final PasswordResetView mView;

    public PasswordResetPresenter(PasswordResetView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }


    /**
     * 重置密码
     *
     * @param countryCode 国家码
     * @param account     账号
     * @param verifyCode  验证码
     * @param newPassword 新密码
     */
    public void resetPassword(String countryCode, String account, String verifyCode, String newPassword) {
        if (FeaturesUtil.isWrongPhoneNumber(account, countryCode)) {
            return;
        }
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showShort(StringUtils.getString(R.string.verify_code_hint));
            return;
        }
        if (FeaturesUtil.isWrongPasswordFormat(newPassword)) {
            return;
        }
        ResetPasswordRequest request = ResetPasswordRequest.Builder.aResetPasswordRequest()
                .phone(account)
                .type(TYPE_PHONE)
                .password(newPassword)
                .verification_code(verifyCode)
                .build();
        requestApi(mRetrofit.resetPassword(request), new BaseMvpObserver<JsonElement>(mView) {

            @Override
            protected void onSuccess(JsonElement response) {
                mView.passwordResetSuccess();
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
        request.setType(Constant.VERIFY_CODE_TYPE_3_FORGET_PASSWORD);

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
                    countdownView.setClickable(true);
                }).subscribe();

    }

}
