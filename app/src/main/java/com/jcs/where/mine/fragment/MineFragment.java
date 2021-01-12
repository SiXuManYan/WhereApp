package com.jcs.where.mine.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.UserInfoResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.EventCode;
import com.jcs.where.hotel.activity.CityPickerActivity;
import com.jcs.where.login.LoginActivity;
import com.jcs.where.mine.activity.LanguageActivity;
import com.jcs.where.mine.activity.PersonalDataActivity;
import com.jcs.where.mine.model.MineModel;
import com.jcs.where.presenter.UploadFilePresenter;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.widget.VerticalSwipeRefreshLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;

import static android.app.Activity.RESULT_OK;

public class MineFragment extends BaseFragment {


    private static final int REQ_SELECT_CITY = 100;
    private static final int REQ_TO_LOGIN = 101;
    private View view;
    private ImageView settingIv;
    private TextView nicknameTv, accountTv;
    private UploadFilePresenter mUploadPresenter;
    private RoundedImageView headerIv;
    private MineModel mModel;
    private VerticalSwipeRefreshLayout mSwipeLayout;
    private TextView mToSeeBalanceTv;

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);

        mSwipeLayout = view.findViewById(R.id.mineSwipeLayout);
        settingIv = view.findViewById(R.id.iv_setting);
        nicknameTv = view.findViewById(R.id.nicknameTv);
        accountTv = view.findViewById(R.id.tv_account);
        mUploadPresenter = new UploadFilePresenter(getContext());
        headerIv = view.findViewById(R.id.iv_header);
        view.findViewById(R.id.toSeeBalanceTv).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.mineBalanceLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.pointLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.couponLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.toSeeAllOrderTv).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.unpaidLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.bookedLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.reviewsLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.afterSalesLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.managerAddressLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.collectionLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.footprintLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.inviteLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.aboutUsLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.ll_settlement).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.rl_minemessage).setOnClickListener(this::onUserDataClicked);
        view.findViewById(R.id.ll_changelangue).setOnClickListener(this::onChangeLanguageClicked);

    }

    private void onUserDataClicked(View view) {
        if (CacheUtil.needUpdateBySpKey(SPKey.K_TOKEN).equals("")) {
            LoginActivity.goTo(getContext());
        } else {
            PersonalDataActivity.goTo(getContext());
        }
    }

    private void toShowComing(View view) {
        showComing();
    }

    private void onChangeLanguageClicked(View view) {
        toActivity(LanguageActivity.class);
    }

    @Override
    protected void initData() {
        mModel = new MineModel();
        updateUserInfo();
    }

    @Override
    protected void bindListener() {

    }

    public void updateUserInfo() {
        showLoading();
        mModel.getUserInfo(new BaseObserver<UserInfoResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
//                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NotNull UserInfoResponse userInfoResponse) {
                stopLoading();
                nicknameTv.setText(userInfoResponse.getNickname());
            }
        });
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
    public void onEventReceive(BaseEvent baseEvent) {
        if (baseEvent.code == EventCode.EVENT_LOGIN_SUCCESS) {
            updateUserInfo();
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
