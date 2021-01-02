package com.jcs.where.government.activity;

import android.content.Intent;

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

    private int mMechanismId;
    private MechanismDetailModel mModel;
    private MechanismDetailResponse mMechanismDetailResponse;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mModel = new MechanismDetailModel();
        Intent intent = getIntent();
        String temp = intent.getStringExtra(K_MECHANISM_ID);
        if (temp != null) {
            try {
                mMechanismId = Integer.parseInt(temp);
            }catch (NumberFormatException e){
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
                public void onNext(@NonNull MechanismDetailResponse mechanismDetailResponse) {
                    stopLoading();
                    mMechanismDetailResponse = mechanismDetailResponse;
                }
            });
        }else {
            showToast("not mechanism id");
        }
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mechanism_detail;
    }
}
