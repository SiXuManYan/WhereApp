package com.jcs.where.government.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.MechanismDetailResponse;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.dialog.CallPhoneDialog;
import com.jcs.where.government.dialog.ToNavigationDialog;
import com.jcs.where.government.model.MechanismDetailModel;
import com.jcs.where.widget.JcsBanner;

import androidx.constraintlayout.widget.Group;
import io.reactivex.annotations.NonNull;
import retrofit2.Response;

/**
 * 机构详情页
 * create by zyf on 2021/1/2 11:16 AM
 */
public class MechanismDetailActivity extends BaseActivity {
    public static final String K_MECHANISM_ID = "mechanismId";
    private final int STATUS_UNCOLLECTED = 1;
    private final int STATUS_COLLECTED = 2;

    private TextView mMechanismTitleTv, mBusinessWeekTv, mBusinessTimeTv, mTelTv,
            mWebsiteTv, mEmailTv, mFacebookTv, mAddressTv, mIntroduceTv;
    private View mToCallView, mToNavigationView;
    private Group mTelGroup, mWebsiteGroup, mEmailGroup, mFacebookGroup;
    private JcsBanner mJcsBanner;

    private CallPhoneDialog mCallDialog;
    private ToNavigationDialog mToNavigationDialog;

    private int mMechanismId;
    private MechanismDetailModel mModel;
    private MechanismDetailResponse mMechanismDetailResponse;
    private String[] weekStr;

    @Override
    protected void initView() {
        mTelGroup = findViewById(R.id.telGroup);
        mWebsiteGroup = findViewById(R.id.websiteGroup);
        mEmailGroup = findViewById(R.id.emailGroup);
        mFacebookGroup = findViewById(R.id.facebookGroup);

        mMechanismTitleTv = findViewById(R.id.mechanismTitleTv);
        mBusinessWeekTv = findViewById(R.id.businessWeekTv);
        mBusinessTimeTv = findViewById(R.id.businessTimeTv);
        mTelTv = findViewById(R.id.telTv);
        mWebsiteTv = findViewById(R.id.websiteTv);
        mEmailTv = findViewById(R.id.emailTv);
        mFacebookTv = findViewById(R.id.facebookTv);
        mAddressTv = findViewById(R.id.addressTv);
        mIntroduceTv = findViewById(R.id.introduceTv);

        mToCallView = findViewById(R.id.toCallView);
        mToNavigationView = findViewById(R.id.toNavigationView);

        mJcsBanner = findViewById(R.id.jcsBanner);
    }

    @Override
    protected void initData() {
        weekStr = new String[]{
                getString(R.string.monday),
                getString(R.string.tuesday),
                getString(R.string.wednesday),
                getString(R.string.thursday),
                getString(R.string.friday),
                getString(R.string.saturday),
                getString(R.string.sunday),
        };
        mModel = new MechanismDetailModel();
        mCallDialog = new CallPhoneDialog();
        mToNavigationDialog = new ToNavigationDialog();
        Intent intent = getIntent();
        String temp = intent.getStringExtra(K_MECHANISM_ID);
        if (temp != null) {
            try {
                mMechanismId = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                mMechanismId = 0;
            }
        }

        // 接收到了 机构id
        if (mMechanismId != 0) {
            showLoading();
            mModel.getMechanismDetailById(mMechanismId, new BaseObserver<MechanismDetailResponse>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                    stopLoading();
                    showNetError(errorResponse);
                }

                @Override
                public void onNext(@NonNull MechanismDetailResponse response) {
                    stopLoading();
                    mMechanismDetailResponse = response;
                    // 配置数据
                    injectResponseToView();

                    // 导航
                    mToNavigationDialog.setLatitude(mMechanismDetailResponse.getLat());
                    mToNavigationDialog.setLongitude(mMechanismDetailResponse.getLng());

                    // banner
                    mJcsBanner.setPicData(mMechanismDetailResponse.getImages());
                }
            });
        } else {
            showToast("not mechanism id");
        }
    }

    @Override
    protected void bindListener() {
        mJcsTitle.setSecondRightIvClickListener(this::onJcsFirstRightClicked);
        mJcsTitle.setSecondRightIvClickListener(this::onJcsSecondRightClicked);
        mToCallView.setOnClickListener(this::onToCallClicked);
        mToNavigationView.setOnClickListener(this::onToNavigationClicked);
    }

    /**
     * 导航
     */
    public void onToNavigationClicked(View view) {
        mToNavigationDialog.show(getSupportFragmentManager());
    }

    /**
     * 拨打电话
     */
    public void onToCallClicked(View view) {
        String tel = mMechanismDetailResponse.getTel();
        if (tel != null && !tel.isEmpty()) {
            mCallDialog.show(getSupportFragmentManager());
        } else {
            showToast(getString(R.string.not_provide_tel_number));
        }
    }

    /**
     * 对应当前页面的 分享
     */
    public void onJcsFirstRightClicked(View view) {

    }

    /**
     * 对应当前页面的 收藏
     */
    public void onJcsSecondRightClicked(View view) {
        showLoading();
        int collectStatus = mMechanismDetailResponse.getCollect_status();
        if (collectStatus == STATUS_UNCOLLECTED) {
            // 状态是未收藏，点击后收藏该机构
            toCollect();
        } else {
            // 状态是已收藏，点击后取消收藏该机构
            toDelCollect();
        }
    }

    private void toDelCollect() {
        mModel.delCollectMechanism(mMechanismId, new BaseObserver<Response<SuccessResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull Response<SuccessResponse> successResponseResponse) {
                stopLoading();
                // 取消收藏成功
                mJcsTitle.setSecondRightIcon(R.mipmap.ic_uncollected_black);
                mMechanismDetailResponse.setCollect_status(STATUS_UNCOLLECTED);
            }
        });
    }

    private void toCollect() {
        mModel.postCollectMechanism(mMechanismId, new BaseObserver<Response<SuccessResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull Response<SuccessResponse> successResponseResponse) {
                stopLoading();
                // 收藏成功
                mJcsTitle.setSecondRightIcon(R.mipmap.ic_collected_red);
                mMechanismDetailResponse.setCollect_status(STATUS_COLLECTED);
            }
        });
    }

    private void deployJcsTitle() {
        mJcsTitle.setMiddleTitle(mMechanismDetailResponse.getTitle());
        if (mMechanismDetailResponse.getCollect_status() == STATUS_UNCOLLECTED) {
            mJcsTitle.setSecondRightIcon(R.drawable.ic_hotelwhiteunlike);
        } else {
            mJcsTitle.setSecondRightIcon(R.drawable.ic_hotelwhitelike);
        }
    }

    private void injectResponseToView() {
        // title 设置
        deployJcsTitle();

        mMechanismTitleTv.setText(mMechanismDetailResponse.getTitle());

        // 周一至周五营业
        mBusinessWeekTv.setText(getBusinessWeek());

        // 09:00-17:00 营业
        mBusinessTimeTv.setText(getBusinessTime());

        String tel = mMechanismDetailResponse.getTel();
        if (tel != null && !tel.isEmpty()) {
            String telStr = tel;
            mTelTv.setText(telStr);
            mCallDialog.setPhoneNumber(tel);
        } else {
            mTelGroup.setVisibility(View.GONE);
        }

        String website = mMechanismDetailResponse.getWeb_site();
        if (website != null && !website.isEmpty()) {
            String websiteStr = "：" + website;
            mWebsiteTv.setText(websiteStr);
        } else {
            mWebsiteGroup.setVisibility(View.GONE);
        }

        String email = mMechanismDetailResponse.getEmail();
        if (email != null && !email.isEmpty()) {
            String emailStr = "：" + email;
            mEmailTv.setText(emailStr);
        } else {
            mEmailGroup.setVisibility(View.GONE);
        }

        String facebook = mMechanismDetailResponse.getFacebook();
        if (facebook != null && !facebook.isEmpty()) {
            String facebookStr = "：" + facebook;
            mFacebookTv.setText(facebookStr);
        } else {
            mFacebookGroup.setVisibility(View.GONE);
        }

        mAddressTv.setText(mMechanismDetailResponse.getAddress());

        String abstractX = mMechanismDetailResponse.getAbstractX();
        if (abstractX == null || abstractX.isEmpty()) {
            abstractX = getString(R.string.no_introduce);
        }
        mIntroduceTv.setText(abstractX);
    }

    /**
     * 获得营业时间（单位周）
     */
    private String getBusinessWeek() {
        String startWeek = weekStr[mMechanismDetailResponse.getWeek_start() - 1];
        String endWeek = weekStr[mMechanismDetailResponse.getWeek_end() - 1];
        return startWeek + getString(R.string.mechanism_to) + endWeek;
    }

    /**
     * 获得营业时间（单位小时）
     */
    private String getBusinessTime() {
        String startTime = mMechanismDetailResponse.getStart_time();
        String endTime = mMechanismDetailResponse.getEnd_time();
        return startTime + "-" + endTime;
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mechanism_detail;
    }
}
