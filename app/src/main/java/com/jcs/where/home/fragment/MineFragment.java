package com.jcs.where.home.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.home.event.TokenEvent;
import com.jcs.where.hotel.CityPickerActivity;
import com.jcs.where.login.LoginActivity;
import com.jcs.where.login.event.LoginEvent;
import com.jcs.where.mine.PersonalDataActivity;
import com.jcs.where.presenter.UploadFilePresenter;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;

import static android.app.Activity.RESULT_OK;

public class MineFragment extends BaseFragment implements View.OnClickListener {


    private static final int REQ_SELECT_CITY = 100;
    private View view;
    private ImageView settingIv;
    private TextView nameTv, accountTv;
    private UploadFilePresenter mUploadPresenter;
    private RoundedImageView headerIv;

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);

        settingIv = view.findViewById(R.id.iv_setting);
        settingIv.setOnClickListener(this);
        nameTv = view.findViewById(R.id.tv_name);
        accountTv = view.findViewById(R.id.tv_account);
        mUploadPresenter = new UploadFilePresenter(getContext());
        headerIv = view.findViewById(R.id.iv_header);
        view.findViewById(R.id.ll_changelangue).setOnClickListener(this);
        view.findViewById(R.id.ll_settlement).setOnClickListener(this);
        view.findViewById(R.id.rl_minemessage).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {

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
    protected boolean needChangeStatusBarStatus() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_SELECT_CITY) {
            showToast("您选择了：" + data.getStringExtra(CityPickerActivity.EXTRA_CITY));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Evect(TokenEvent tokenEvent) throws InterruptedException {
        //TODO 更新UI
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        if (event == LoginEvent.LOGIN) {
            //TODO 更新UI
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }
}
