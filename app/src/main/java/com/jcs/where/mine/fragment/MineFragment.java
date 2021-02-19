package com.jcs.where.mine.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.Utils;
import com.jcs.where.BaseApplication;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.UserInfoResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.base.EventCode;
import com.jcs.where.customer.ExtendChatActivity;
import com.jcs.where.features.account.login.LoginActivity;
import com.jcs.where.features.setting.SettingActivity;
import com.jcs.where.features.setting.information.ModifyInfoActivity;
import com.jcs.where.hotel.activity.CityPickerActivity;
import com.jcs.where.integral.IntegralActivity;
import com.jcs.where.mine.activity.AboutActivity;
import com.jcs.where.mine.activity.CollectionListActivity;
import com.jcs.where.mine.activity.FootprintActivity;
import com.jcs.where.mine.activity.LanguageActivity;
import com.jcs.where.mine.model.MineModel;
import com.jcs.where.presenter.UploadFilePresenter;
import com.jcs.where.storage.WhereDataBase;
import com.jcs.where.storage.entity.User;
import com.jcs.where.storage.entity.UserRongyunData;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.widget.VerticalSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static android.app.Activity.RESULT_OK;

public class MineFragment extends BaseFragment {


    private static final int REQ_SELECT_CITY = 100;
    private static final int REQ_TO_LOGIN = 101;
    private View view;
    private TextView nicknameTv, accountTv;
    private UploadFilePresenter mUploadPresenter;
    private CircleImageView headerIv;
    private MineModel mModel;
    private VerticalSwipeRefreshLayout mSwipeLayout;
    private TextView mToSeeBalanceTv;
    private View mLikeLayout;
    private TextView mPointTv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);

        mSwipeLayout = view.findViewById(R.id.mineSwipeLayout);

        nicknameTv = view.findViewById(R.id.nicknameTv);
        accountTv = view.findViewById(R.id.tv_account);
        mUploadPresenter = new UploadFilePresenter(getContext());
        headerIv = view.findViewById(R.id.iv_header);
        mLikeLayout = view.findViewById(R.id.collectionLayout);
        mPointTv = view.findViewById(R.id.point_tv);

        view.findViewById(R.id.toSeeBalanceTv).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.mineBalanceLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.pointLayout).setOnClickListener(this::onIntegralIvClicked);
        view.findViewById(R.id.couponLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.toSeeAllOrderTv).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.unpaidLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.bookedLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.reviewsLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.afterSalesLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.managerAddressLayout).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.footprintLayout).setOnClickListener(this::onFootprintClicked);
        view.findViewById(R.id.inviteLayout).setOnClickListener(this::onIntegralIvClicked);
        view.findViewById(R.id.aboutUsLayout).setOnClickListener(this::onAboutClick);
        view.findViewById(R.id.ll_settlement).setOnClickListener(this::toShowComing);
        view.findViewById(R.id.rl_minemessage).setOnClickListener(this::onUserDataClicked);
        view.findViewById(R.id.ll_changelangue).setOnClickListener(this::onChangeLanguageClicked);
        view.findViewById(R.id.customer_service_ll).setOnClickListener(this::onCustomerServiceClick);
        view.findViewById(R.id.setting_ll).setOnClickListener(this::onSettingClick);
        view.findViewById(R.id.integral_iv).setOnClickListener(this::onIntegralIvClicked);
        view.findViewById(R.id.message_iv).setOnClickListener(this::toShowComing);
    }

    private void onAboutClick(View view) {
        startActivity(AboutActivity.class);
    }

    /**
     * 足迹
     */
    private void onFootprintClicked(View view) {
        toActivityIfSigned(FootprintActivity.class);
    }

    /**
     * 积分签到
     *
     * @param view
     */
    private void onIntegralIvClicked(View view) {
        if (User.isLogon()) {
            toActivity(IntegralActivity.class);
        } else {
            startActivity(LoginActivity.class);
        }
    }

    private void onUserDataClicked(View view) {
        if (CacheUtil.needUpdateBySpKey(SPKey.K_TOKEN).equals("")) {
            startActivity(LoginActivity.class);
        } else {
            startActivity(ModifyInfoActivity.class);
        }
    }

    private void toShowComing(View view) {
        showComing();
    }

    private void onChangeLanguageClicked(View view) {
        toActivity(LanguageActivity.class);
    }


    private void onCustomerServiceClick(View view) {
        startActivity(new Intent(getActivity(), ExtendChatActivity.class));
    }


    private void onSettingClick(View view) {

        String token = CacheUtil.needUpdateBySpKey(SPKey.K_TOKEN);
        if (TextUtils.isEmpty(token)) {
            startActivity(LoginActivity.class);
        } else {
            toActivity(SettingActivity.class);
        }
    }


    @Override
    protected void initData() {
        mModel = new MineModel();
        updateUserInfo();
    }

    @Override
    protected void bindListener() {
        mSwipeLayout.setOnRefreshListener(this::onRefreshListener);
        mLikeLayout.setOnClickListener(this::onLikeClicked);
    }

    private void onLikeClicked(View view) {
        toActivityIfSigned(CollectionListActivity.class);
    }

    private void onRefreshListener() {
        updateUserInfo();
    }

    public void updateUserInfo() {
        if (!User.isLogon()) {
            return;
        }
        if (!mSwipeLayout.isRefreshing()) {
            showLoading();
        }
        mModel.getUserInfo(new BaseObserver<UserInfoResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                mSwipeLayout.setRefreshing(false);
//                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NotNull UserInfoResponse userInfoResponse) {
                stopLoading();
                mSwipeLayout.setRefreshing(false);
                nicknameTv.setText(userInfoResponse.getNickname());
                mPointTv.setText(userInfoResponse.getIntegral());
                GlideUtil.load(getContext(), userInfoResponse.getAvatar(), headerIv);
                saveData(userInfoResponse);
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
    public void onEventReceived(BaseEvent<?> baseEvent) {

        switch (baseEvent.code) {
            case EventCode.EVENT_LOGIN_SUCCESS:
            case EventCode.EVENT_REFRESH_USER_INFO:
            case EventCode.EVENT_SIGN_IN_CHANGE_STATUS:
                updateUserInfo();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void saveData(@NotNull UserInfoResponse response) {
        User user = User.Builder.anUser()
                .id(response.getId())
                .nickName(response.getNickname())
                .phone(response.getPhone())
                .email(response.getEmail())
                .avatar(response.getAvatar())
                .balance(response.getBalance())
                .createdAt(response.getCreatedAt())
                .name(response.getName())
                .type(response.getType())
                .countryCode(response.getCountryCode())
                .merchantApplyStatus(response.getMerchantApplyStatus())
                .faceBookBindStatus(response.getFacebookBindStatus())
                .googleBindStatus(response.getGoogleBindStatus())
                .twitterBindStatus(response.getTwitterBindStatus())
                .signStatus(response.getSignStatus())
                .integral(response.getIntegral())
                .rongData(response.rongData).build();

        BaseApplication app = (BaseApplication) Utils.getApp();
        WhereDataBase database = app.getDatabase();
        database.userDao().addUser(user);
        User.update();
        connectRongCloud(response.rongData);
    }


    private boolean alreadyConnectRongCloud = false;

    /**
     * 连接融云
     */
    private void connectRongCloud(UserRongyunData rongData) {

        if (alreadyConnectRongCloud) {
            return;
        }

        RongIM.connect(rongData.token, new RongIMClient.ConnectCallback() {
            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {
                //消息数据库打开，可以进入到主页面
            }

            @Override
            public void onSuccess(String s) {
                //连接成功
                alreadyConnectRongCloud = true;
            }

            @Override
            public void onError(RongIMClient.ConnectionErrorCode errorCode) {
                if (errorCode.equals(RongIMClient.ConnectionErrorCode.RC_CONN_TOKEN_INCORRECT)) {
                    //从 APP 服务获取新 token，并重连
                    alreadyConnectRongCloud = false;
                } else {
                    //无法连接 IM 服务器，请根据相应的错误码作出对应处理
                }
            }
        });


    }


}
