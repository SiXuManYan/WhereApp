package com.jcs.where.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.UserBean;
import com.jcs.where.home.event.TokenEvent;
import com.jcs.where.hotel.CityPickerActivity;
import com.jcs.where.login.LoginActivity;
import com.jcs.where.login.event.LoginEvent;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.manager.UserManager;
import com.jcs.where.mine.PersonalDataActivity;
import com.jcs.where.presenter.UploadFilePresenter;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import co.tton.android.base.app.fragment.BaseFragment;
import co.tton.android.base.manager.ImageLoader;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.ToastUtils;

import static android.app.Activity.RESULT_OK;

public class MineFragment extends BaseFragment implements View.OnClickListener {


    private static final int REQ_SELECT_CITY = 100;
    private View view;
    private ImageView settingIv;
    private TextView nameTv, accountTv;
    private UploadFilePresenter mUploadPresenter;
    private RoundedImageView headerIv;

    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        return view;
    }

    private void initView() {
        settingIv = V.f(view, R.id.iv_setting);
        settingIv.setOnClickListener(this);
        nameTv = V.f(view, R.id.tv_name);
        accountTv = V.f(view, R.id.tv_account);
        mUploadPresenter = new UploadFilePresenter(getContext());
        headerIv = V.f(view, R.id.iv_header);
        V.f(view, R.id.ll_changelangue).setOnClickListener(this);
        V.f(view, R.id.ll_settlement).setOnClickListener(this);
        V.f(view, R.id.rl_minemessage).setOnClickListener(this);
        initData(TokenManager.get().getToken(getContext()));
    }

    private void initData(String token) {
        if (token != null) {
            showLoading();
            HttpUtils.doHttpReqeust("GET", "userapi/v1/user/info", null, "", token, new HttpUtils.StringCallback() {
                @Override
                public void onSuccess(int code, String result) {
                    stopLoading();
                    if (code == 200) {
                        UserBean userBean = new Gson().fromJson(result, UserBean.class);
                        UserManager.get().login(getContext(), userBean);
                        accountTv.setVisibility(View.VISIBLE);
                        nameTv.setText(userBean.nickname);
                        accountTv.setText(userBean.phone);
                        ImageLoader.get().loadAvatar(headerIv, userBean.avatar);
                    } else {
                        ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                        ToastUtils.showLong(getContext(), errorBean.message);
                    }
                }

                @Override
                public void onFaileure(int code, Exception e) {
                    stopLoading();
                    ToastUtils.showLong(getContext(), e.getMessage());
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                break;
            case R.id.ll_settlement:
                break;
            case R.id.ll_changelangue:
                break;
            case R.id.rl_minemessage:
                if (!nameTv.getText().toString().equals("登录/注册")) {
                    PersonalDataActivity.goTo(getContext());
                } else {
                    LoginActivity.goTo(getContext());
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_SELECT_CITY) {
            ToastUtils.showLong(getContext(), "您选择了：" + data.getStringExtra(CityPickerActivity.EXTRA_CITY));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Evect(TokenEvent tokenEvent) throws InterruptedException {
        initData(tokenEvent.getToken());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        if (event == LoginEvent.LOGIN) {
            Log.d("ssss", "收到EVENT");
            initData(TokenManager.get().getToken(getContext()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
