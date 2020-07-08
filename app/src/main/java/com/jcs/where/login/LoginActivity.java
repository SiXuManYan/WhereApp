package com.jcs.where.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.jcs.where.R;
import com.jcs.where.utils.IEditTextChangeListener;
import com.jcs.where.utils.PhoneCheckUtil;
import com.jcs.where.utils.PhoneTextChangeListener;
import com.jcs.where.utils.WorksSizeCheckUtil;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.ToastUtils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView accountTv, phoneTv;
    private EditText accountEt, passwordEt,phoneEt,codeEt;
    private LinearLayout accountLl, phoneLl;
    private TextView getCodeTv, accountLoginTv,phoneLoginTv;
    private MyCountDownTimer myCountDownTimer;
    private TextView accountErrorTv,phoneErrorTv;

    public static void goTo(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
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
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
        initView();
    }

    private void initView() {
        toolbar = V.f(this, R.id.toolbar);
        setMargins(toolbar, 0, getStatusBarHeight(), 0, 0);
        accountTv = V.f(this, R.id.tv_account);
        accountTv.setOnClickListener(this);
        phoneTv = V.f(this, R.id.tv_phone);
        phoneTv.setOnClickListener(this);
        accountEt = V.f(this, R.id.et_account);
        passwordEt = V.f(this, R.id.et_password);
        accountLl = V.f(this, R.id.ll_account);
        phoneLl = V.f(this, R.id.ll_phone);
        getCodeTv = V.f(this, R.id.tv_getcode);
        getCodeTv.setOnClickListener(this);
        accountLoginTv = V.f(this, R.id.tv_accountlogin);
        accountLoginTv.setOnClickListener(this);
        //1.创建工具类对象 把要改变颜色的btn先传过去
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(accountLoginTv);

        //2.把所有要监听的edittext都添加进去
        textChangeListener.addAllEditText(accountEt,passwordEt);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 btn 应该设置什么颜色
        WorksSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if(isHasContent){
                    accountLoginTv.setClickable(true);
                    accountLoginTv.setBackground(getResources().getDrawable(R.drawable.bg_loginbtn));
                }else{
                    accountLoginTv.setClickable(false);
                    accountLoginTv.setBackground(getResources().getDrawable(R.drawable.bg_loginbtnunclick));
                }
            }
        });
        phoneEt = V.f(this,R.id.et_phone);
        codeEt = V.f(this,R.id.et_code);
        phoneLoginTv = V.f(this, R.id.tv_phonelogin);
        phoneLoginTv.setOnClickListener(this);
        //1.创建工具类对象 把要改变颜色的btn先传过去
        PhoneCheckUtil.textChangeListener textChangeListener1 = new PhoneCheckUtil.textChangeListener(phoneLoginTv);

        //2.把所有要监听的edittext都添加进去
        textChangeListener1.addAllEditText(phoneEt,codeEt);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 btn 应该设置什么颜色
        PhoneCheckUtil.setChangeListener(new PhoneTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if(isHasContent){
                    phoneLoginTv.setClickable(true);
                    phoneLoginTv.setBackground(getResources().getDrawable(R.drawable.bg_loginbtn));
                }else{
                    phoneLoginTv.setClickable(false);
                    phoneLoginTv.setBackground(getResources().getDrawable(R.drawable.bg_loginbtnunclick));
                }
            }
        });
        accountErrorTv = V.f(this,R.id.tv_accounterror);
        phoneErrorTv = V.f(this,R.id.tv_phoneerror);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_account:
                accountTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                accountTv.setTextColor(getResources().getColor(R.color.white));
                accountLl.setVisibility(View.VISIBLE);
                phoneTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                phoneTv.setTextColor(getResources().getColor(R.color.white_FEFEFE));
                phoneLl.setVisibility(View.GONE);
                break;
            case R.id.tv_phone:
                phoneTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                phoneTv.setTextColor(getResources().getColor(R.color.white));
                phoneLl.setVisibility(View.VISIBLE);
                accountTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                accountTv.setTextColor(getResources().getColor(R.color.white_FEFEFE));
                accountLl.setVisibility(View.GONE);
                break;
            case R.id.tv_getcode:
                myCountDownTimer.start();
                break;
            case R.id.tv_accountlogin:
                if(!passwordEt.getText().toString().equals("1234")){
                    accountErrorTv.setVisibility(View.VISIBLE);
                    return;
                }else{
                    accountErrorTv.setVisibility(View.INVISIBLE);
                }
                ToastUtils.showLong(LoginActivity.this,"账号:"+accountEt.getText().toString()+"密码:"+passwordEt.getText().toString());
                break;
            case R.id.tv_phonelogin:
               if(!isMobileNO(phoneEt.getText().toString())){
                    ToastUtils.showLong(LoginActivity.this,"请输入正确手机号");
                    return;
                }
                if(!codeEt.getText().toString().equals("1234")){
                    phoneErrorTv.setVisibility(View.VISIBLE);
                    return;
                }else{
                    phoneErrorTv.setVisibility(View.INVISIBLE);
                }
                ToastUtils.showLong(LoginActivity.this,"手机号:"+phoneEt.getText().toString()+"验证码 :"+codeEt.getText().toString());
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


}
