package com.jcs.where.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.RetrofitManager;
import com.jcs.where.api.request.LoginRequest;
import com.jcs.where.api.request.SendCodeRequest;
import com.jcs.where.api.response.LoginResponse;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.login.model.LoginModel;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.PhoneCheckUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.WorksSizeCheckUtil;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class LoginActivity extends BaseActivity {

    private TextView accountTv, phoneTv;
    private EditText accountEt, passwordEt, phoneEt, codeEt;
    private LinearLayout accountLl, phoneLl;
    private TextView getCodeTv, accountLoginTv, phoneLoginTv;
    private MyCountDownTimer myCountDownTimer;
    private TextView accountErrorTv, phoneErrorTv;
    private LoginModel mModel;

    public static void goTo(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 验证手机格式
     */
    public boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        fullScreen(this);
        initView();
    }

    @Override
    protected void initView() {
        myCountDownTimer = new MyCountDownTimer(60000, 1000);

        accountTv = findViewById(R.id.tv_account);
        phoneTv = findViewById(R.id.tv_phone);
        accountEt = findViewById(R.id.et_account);
        passwordEt = findViewById(R.id.et_password);
        accountLl = findViewById(R.id.ll_account);
        phoneLl = findViewById(R.id.ll_phone);
        getCodeTv = findViewById(R.id.tv_getcode);
        accountLoginTv = findViewById(R.id.tv_accountlogin);
        accountLoginTv.setClickable(false);
        phoneEt = findViewById(R.id.et_phone);
        codeEt = findViewById(R.id.et_code);
        phoneLoginTv = findViewById(R.id.tv_phonelogin);
        phoneLoginTv.setClickable(false);
        accountErrorTv = findViewById(R.id.tv_accounterror);
        phoneErrorTv = findViewById(R.id.tv_phoneerror);
    }

    @Override
    protected void initData() {
        mModel = new LoginModel();
    }

    private void setTextChangeListener() {
        //1.创建工具类对象 把要改变颜色的btn先传过去
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(accountLoginTv);

        //2.把所有要监听的edittext都添加进去
        textChangeListener.addAllEditText(accountEt, passwordEt);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 btn 应该设置什么颜色
        WorksSizeCheckUtil.setChangeListener(this::onEditTextChange);

        //1.创建工具类对象 把要改变颜色的btn先传过去
        PhoneCheckUtil.textChangeListener phoneCheckUtil = new PhoneCheckUtil.textChangeListener(phoneLoginTv);

        //2.把所有要监听的edittext都添加进去
        phoneCheckUtil.addAllEditText(phoneEt, codeEt);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 btn 应该设置什么颜色
        PhoneCheckUtil.setChangeListener(this::onPhoneChanged);
    }

    @Override
    protected void bindListener() {
        setTextChangeListener();
        accountTv.setOnClickListener(this::onAccountTvClicked);
        phoneTv.setOnClickListener(this::onPhoneTvClicked);
        getCodeTv.setOnClickListener(this::onGetCodeTvClicked);
        accountLoginTv.setOnClickListener(this::onAccountLoginTvClicked);
        phoneLoginTv.setOnClickListener(this::onPhoneLoginTvClicked);

        findViewById(R.id.tv_forgetpas).setOnClickListener(v -> ForgetPasswordActivity.goTo(LoginActivity.this));
        findViewById(R.id.tv_register).setOnClickListener(v -> RegisterActivity.goTo(LoginActivity.this));
    }

    /**
     * 手机号登录（手机号加验证码）
     */
    private void onPhoneLoginTvClicked(View view) {
        if (!isMobileNO(phoneEt.getText().toString())) {
            showToast(getString(R.string.prompt_input_correct_phone));
            return;
        }
        if (codeEt.getText().toString().equals("")) {
            phoneErrorTv.setVisibility(View.VISIBLE);
            phoneErrorTv.setText(getText(R.string.hint_input_code));
            return;
        } else {
            phoneErrorTv.setVisibility(View.INVISIBLE);
        }
        showLoading();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setType(loginRequest.TYPE_LOGIN_VERIFICATION_CODE);
        loginRequest.setPhone(phoneEt.getText().toString());
        loginRequest.setVerificationCode(codeEt.getText().toString());
        mModel.login(loginRequest, new BaseObserver<LoginResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                accountErrorTv.setVisibility(View.VISIBLE);
                accountErrorTv.setText(errorResponse.getErrMsg());
            }

            @Override
            public void onNext(@NotNull LoginResponse loginResponse) {
                stopLoading();
                CacheUtil.cacheWithCurrentTime(SPKey.K_TOKEN, loginResponse.getToken());
                EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_LOGIN_SUCCESS));
                showToast(getString(R.string.login_success));
                finish();
            }
        });
    }

    /**
     * 账号登录
     */
    private void onAccountLoginTvClicked(View view) {
        String password = passwordEt.getText().toString();
        if (password.length() < 6 || password.length() > 16
        ) {
            showToast(getString(R.string.error_password_length));
            return;
        }
        showLoading();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPhone(accountEt.getText().toString());
        loginRequest.setType(loginRequest.TYPE_LOGIN_PASSWORD);
        loginRequest.setPassword(password);
        mModel.login(loginRequest, new BaseObserver<LoginResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
//                showNetError(errorResponse);
                accountErrorTv.setVisibility(View.VISIBLE);
                accountErrorTv.setText(errorResponse.getErrMsg());
            }

            @Override
            public void onNext(@NotNull LoginResponse loginResponse) {
                stopLoading();
                CacheUtil.cacheWithCurrentTime(SPKey.K_TOKEN, loginResponse.getToken());
                EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_LOGIN_SUCCESS));
                showToast(getString(R.string.login_success));
                finish();
            }
        });
    }

    /**
     * 获取验证码
     */
    private void onGetCodeTvClicked(View view) {
        if (!isMobileNO(phoneEt.getText().toString())) {
            showToast(getString(R.string.prompt_input_correct_phone));
            return;
        }
        myCountDownTimer.start();
        showLoading();
        SendCodeRequest sendCodeRequest = new SendCodeRequest();
        sendCodeRequest.setPhone(phoneEt.getText().toString());
        sendCodeRequest.setType(sendCodeRequest.TYPE_LOGIN);
        mModel.sendCode(sendCodeRequest, new BaseObserver<ResponseBody>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NotNull ResponseBody successResponseResponse) {
                stopLoading();
                showToast(getString(R.string.prompt_send_success));
            }
        });
    }

    private void onPhoneTvClicked(View view) {
        phoneTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        phoneTv.setTextColor(getResources().getColor(R.color.white));
        phoneLl.setVisibility(View.VISIBLE);
        accountTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        accountTv.setTextColor(getResources().getColor(R.color.white_FEFEFE));
        accountLl.setVisibility(View.GONE);
    }

    /**
     * 点击账号TextView
     */
    private void onAccountTvClicked(View view) {
        accountTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        accountTv.setTextColor(getResources().getColor(R.color.white));
        accountLl.setVisibility(View.VISIBLE);
        phoneTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        phoneTv.setTextColor(getResources().getColor(R.color.white_FEFEFE));
        phoneLl.setVisibility(View.GONE);
    }

    private void onPhoneChanged(boolean isHasContent) {
        if (isHasContent) {
            phoneLoginTv.setClickable(true);
            phoneLoginTv.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_loginbtn));
        } else {
            phoneLoginTv.setClickable(false);
            phoneLoginTv.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_loginbtnunclick));
        }
    }

    private void onEditTextChange(boolean isHasContent) {
        if (isHasContent) {
            accountLoginTv.setClickable(true);
            accountLoginTv.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_loginbtn));
        } else {
            accountLoginTv.setClickable(false);
            accountLoginTv.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_loginbtnunclick));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    //倒计时函数
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            getCodeTv.setClickable(false);
            getCodeTv.setText("重新发送(" + l / 1000 + ")");
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            getCodeTv.setText("重新获取");
            //设置可点击
            getCodeTv.setClickable(true);
        }
    }

}
