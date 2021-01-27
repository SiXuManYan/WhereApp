package com.jcs.where.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.UserBean;
import com.jcs.where.utils.IEditTextChangeListener;
import com.jcs.where.utils.WorksSizeCheckUtil;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText phoneEt, codeEt, accountEt, passEt, surePassEt, invitationEt;
    private TextView getCodeTv, registerTv, errorTv;
    private MyCountDownTimer myCountDownTimer;

    public static void goTo(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
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
    protected void initView() {
        myCountDownTimer = new MyCountDownTimer(60000, 1000);

        phoneEt = findViewById(R.id.et_phone);
        codeEt = findViewById(R.id.et_code);
        accountEt = findViewById(R.id.et_account);
        passEt = findViewById(R.id.et_password);
        surePassEt = findViewById(R.id.et_surepassword);
        invitationEt = findViewById(R.id.et_invitation);
        getCodeTv = findViewById(R.id.tv_getcode);
        registerTv = findViewById(R.id.tv_register);
        getCodeTv.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        registerTv.setClickable(false);
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(registerTv);

        //2.把所有要监听的edittext都添加进去
        textChangeListener.addAllEditText(phoneEt, codeEt, accountEt, passEt, surePassEt);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 btn 应该设置什么颜色
        WorksSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    registerTv.setClickable(true);
                    registerTv.setBackground(getResources().getDrawable(R.drawable.bg_loginbtn));
                } else {
                    registerTv.setClickable(false);
                    registerTv.setBackground(getResources().getDrawable(R.drawable.bg_loginbtnunclick));
                }
            }
        });
        errorTv = findViewById(R.id.tv_error);
        findViewById(R.id.tv_havecount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode:
                if (TextUtils.isEmpty(phoneEt.getText().toString())) {
                    showToast("请输入手机号");
                    return;
                }
                if (!isMobileNO(phoneEt.getText().toString())) {
                    showToast("请输入正确手机号");
                    return;
                }
                myCountDownTimer.start();
                showLoading();
                Map<String, String> params = new HashMap<>();
                params.put("phone", phoneEt.getText().toString());
                params.put("type", "2");
                HttpUtils.doHttpReqeust("POST", "userapi/v1/mobile/auth/code", params, "", "", new HttpUtils.StringCallback() {
                    @Override
                    public void onSuccess(int code, String result) {
                        stopLoading();
                        if (code == 200) {
                            showToast("发送成功，请注意查收");
                        } else {
                            ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                            showToast(errorBean.message);
                        }
                    }

                    @Override
                    public void onFaileure(int code, Exception e) {
                        stopLoading();
                        showToast(e.getMessage());
                    }
                });
                break;
            case R.id.tv_register:
                if (!isMobileNO(phoneEt.getText().toString())) {
                    errorTv.setVisibility(View.VISIBLE);
                    errorTv.setText("请输入正确手机号");
                    return;
                } else {
                    errorTv.setVisibility(View.INVISIBLE);
                }
                if (TextUtils.isEmpty(codeEt.getText().toString())) {
                    errorTv.setVisibility(View.VISIBLE);
                    errorTv.setText("请输入验证码");
                    return;
                } else {
                    errorTv.setVisibility(View.INVISIBLE);
                }
                if (accountEt.getText().toString().length() > 29) {
                    errorTv.setVisibility(View.VISIBLE);
                    errorTv.setText("账号长度过长");
                    return;
                } else {
                    errorTv.setVisibility(View.INVISIBLE);
                }
                if (!passEt.getText().toString().equals(surePassEt.getText().toString())) {
                    errorTv.setVisibility(View.VISIBLE);
                    errorTv.setText("两次输入密码不一致");
                    return;
                } else {
                    errorTv.setVisibility(View.INVISIBLE);
                }

                if (surePassEt.getText().toString().length() < 6) {
                    errorTv.setVisibility(View.VISIBLE);
                    errorTv.setText("密码长度不符");
                    showToast("密码长度不符");
                    return;
                } else {
                    errorTv.setVisibility(View.INVISIBLE);
                }
                if (surePassEt.getText().toString().length() > 16) {
                    errorTv.setText("密码长度不符");
                    showToast("密码长度不符");
                    return;
                } else {
                    errorTv.setVisibility(View.INVISIBLE);
                }
                showLoading();
                Map<String, String> params1 = new HashMap<>();
                params1.put("name", accountEt.getText().toString());
                params1.put("password", passEt.getText().toString());
                params1.put("password_confirmation", surePassEt.getText().toString());
                params1.put("type", "1");
                params1.put("verification_code", codeEt.getText().toString());
                params1.put("invite_code", invitationEt.getText().toString());
                params1.put("phone", phoneEt.getText().toString());
                params1.put("email", "");
                Log.d("ssss", params1 + "");
                HttpUtils.doHttpReqeust("POST", "userapi/v1/register", params1, "", "", new HttpUtils.StringCallback() {
                    @Override
                    public void onSuccess(int code, String result) {
                        stopLoading();
                        if (code == 200) {
                            UserBean userBean = new Gson().fromJson(result, UserBean.class);
                            showToast(userBean.id);
                        } else {
                            ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                            showToast(errorBean.message);
                        }
                    }

                    @Override
                    public void onFaileure(int code, Exception e) {
                        stopLoading();
                        showToast(e.getMessage());
                    }
                });

                break;
            default:
        }
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
