package com.jcs.where.integral;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.SignListResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.integral.child.detail.IntegralChildDetailFragment;
import com.jcs.where.integral.child.task.IntegralChildTaskFragment;

/**
 * 我的积分
 */
public class IntegralActivity extends BaseActivity implements IntegralView {


    private InnerPagerAdapter mPagerAdapter;
    private ViewPager mPagerVp;
    private TextView mIntegralTv;
    private IntegralModel mModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }


    @Override
    protected void initView() {
        mPagerVp = findViewById(R.id.pager_vp);
        mPagerAdapter = new InnerPagerAdapter(getSupportFragmentManager(), 0);
        mPagerVp.setAdapter(mPagerAdapter);
        mIntegralTv = findViewById(R.id.my_integral_tv);

    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {
        mModel = new IntegralModel(this);
        mModel.getSignInList();
    }

    @Override
    public void bindDetailData(SignListResponse response) {
        stopLoading();
    }

    @Override
    public void onDetailError(ErrorResponse errorResponse) {
        stopLoading();
        showNetError(errorResponse);
    }


    /**
     * 立即签到
     *
     * @param view
     */
    public void signInClick(View view) {


    }


    private static class InnerPagerAdapter extends FragmentPagerAdapter {

        public InnerPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return IntegralChildTaskFragment.newInstance();
            }
            return IntegralChildDetailFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }


    }


}
