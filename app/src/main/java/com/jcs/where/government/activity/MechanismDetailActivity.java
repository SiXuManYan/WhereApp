package com.jcs.where.government.activity;

import android.content.Intent;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.MechanismDetailResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.model.MechanismDetailModel;

import io.reactivex.annotations.NonNull;

/**
 * 机构详情页
 * create by zyf on 2021/1/2 11:16 AM
 */
public class MechanismDetailActivity extends BaseActivity {
    public static final String K_MECHANISM_ID = "mechanismId";
    private final int STATUS_UNCOLLECTED = 1;
    private final int STATUS_COLLECTED = 2;

    private TextView mBusinessWeekTv, mBusinessTimeTv, mTelTv,
            mWebsiteTv, mEmailTv, mFacebookTv, mAddressTv, mIntroduceTv;

    private int mMechanismId;
    private MechanismDetailModel mModel;
    private MechanismDetailResponse mMechanismDetailResponse;
    private String[] weekStr = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    @Override
    protected void initView() {
        mBusinessWeekTv = findViewById(R.id.businessWeekTv);
        mBusinessTimeTv = findViewById(R.id.businessTimeTv);
        mTelTv = findViewById(R.id.telTv);
        mWebsiteTv = findViewById(R.id.websiteTv);
        mEmailTv = findViewById(R.id.emailTv);
        mFacebookTv = findViewById(R.id.facebookTv);
        mAddressTv = findViewById(R.id.addressTv);
        mIntroduceTv = findViewById(R.id.introduceTv);
    }

    @Override
    protected void initData() {
        mModel = new MechanismDetailModel();
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
                    if (mMechanismDetailResponse != null) {

                        // 配制数据
                        injectResponseToView();


                    }
                }
            });
        } else {
            showToast("not mechanism id");
        }
    }

    @Override
    protected void bindListener() {

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

        // 周一至周五营业
        mBusinessWeekTv.setText(getBusinessWeek());

        // 09:00-17:00 营业
        mBusinessTimeTv.setText(getBusinessTime());

        String tel = mMechanismDetailResponse.getTel();
        if (tel != null && !tel.isEmpty()) {
            String telStr = "：" + tel;
            mTelTv.setText(telStr);
        }

        String website = mMechanismDetailResponse.getWeb_site();
        if (website != null && !website.isEmpty()) {
            String websiteStr = "：" + website;
            mWebsiteTv.setText(websiteStr);
        }

        String email = mMechanismDetailResponse.getEmail();
        if (email != null && !email.isEmpty()) {
            String emailStr = "：" + email;
            mEmailTv.setText(emailStr);
        }

        String facebook = mMechanismDetailResponse.getFacebook();
        if (facebook != null && !facebook.isEmpty()) {
            String facebookStr = "：" + facebook;
            mFacebookTv.setText(facebookStr);
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
        String startWeek = weekStr[mMechanismDetailResponse.getWeek_start()];
        String endWeek = weekStr[mMechanismDetailResponse.getWeek_end()];
        return startWeek + "至" + endWeek;
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
    protected int getLayoutId() {
        return R.layout.activity_mechanism_detail;
    }
}
