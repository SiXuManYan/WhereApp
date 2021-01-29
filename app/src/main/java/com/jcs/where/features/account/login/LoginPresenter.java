package com.jcs.where.features.account.login;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonElement;
import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.network.BaseMvpObserver;
import com.jcs.where.api.network.BaseMvpPresenter;
import com.jcs.where.api.request.LoginRequest;
import com.jcs.where.api.request.RegisterRequest;
import com.jcs.where.api.request.SendCodeRequest;
import com.jcs.where.api.response.LoginResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.SPKey;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Wangsw  2021/1/28 16:43.
 */
public class LoginPresenter extends BaseMvpPresenter {

    private final LoginView mView;

    public LoginPresenter(LoginView baseMvpView) {
        super(baseMvpView);
        mView = baseMvpView;
    }


    public String getCountryPrefix(LoginActivity activity) {

        String[] stringArray = StringUtils.getStringArray(R.array.country_prefix);
        // 默认菲律宾
        final String[] prefix = {stringArray[0]};

        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_country_prefix, null);
        dialog.setContentView(view);
        try {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.setBackgroundResource(android.R.color.transparent);
        } catch (Exception ignored) {

        }
        view.findViewById(R.id.philippines_tv).setOnClickListener(v -> prefix[0] = stringArray[0]);
        view.findViewById(R.id.china_tv).setOnClickListener(v13 -> prefix[0] = stringArray[1]);
        view.findViewById(R.id.confirm_tv).setOnClickListener(v1 ->
                dialog.dismiss()
        );
        dialog.show();

        return prefix[0];

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
            if (TextUtils.isEmpty(password)) {
                ToastUtils.showShort(StringUtils.getString(R.string.input_password_hint));
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
        loginRequest.setVerificationCode(verifyCode);

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

                getVerifyTv.setEnabled(false);
                countdown(getVerifyTv, StringUtils.getString(R.string.get_again));
                ToastUtils.showShort(R.string.verify_code_send_success);
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                super.onError(errorResponse);
                getVerifyTv.setEnabled(true);

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
        CompositeDisposable compositeDisposable = null;

        if (mObserver != null) {
            compositeDisposable = mObserver.getCompositeDisposable();
        }
        if (compositeDisposable == null) {
            // todo 走重新实例化的，可能需要释放
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(
                Flowable.intervalRange(0, Constant.WAIT_DELAYS, 0, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(aLong -> {
                            String string = StringUtils.getString(R.string.count_down_format, Constant.WAIT_DELAYS - aLong);
                            countdownView.setText(string);
                        })
                        .doOnComplete(() -> {
                            countdownView.setText(defaultStr);
                            countdownView.setEnabled(true);
                        }).subscribe()

        );

    }

    /**
     * 保存token
     *
     * @param token
     */
    private void saveToken(String token) {
        CacheUtil.cacheWithCurrentTime(SPKey.K_TOKEN, token);
    }


}
