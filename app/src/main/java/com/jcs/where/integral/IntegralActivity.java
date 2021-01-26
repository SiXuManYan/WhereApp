package com.jcs.where.integral;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.SignListResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.integral.child.detail.IntegralChildDetailFragment;
import com.jcs.where.integral.child.task.IntegralChildTaskFragment;

import static com.jcs.where.R.id.task_rb;

/**
 * 我的积分
 */
public class IntegralActivity extends BaseActivity implements IntegralView {

    private TextView mIntegralTv;
    private TextView mSignInTv;
    private RadioButton taskRb;
    private RadioButton detailRb;
    private RadioGroup tabRg;

    private IntegralModel mModel;
    private ViewPager mPagerVp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }


    @Override
    protected void initView() {

        BarUtils.setStatusBarColor(this, R.drawable.shape_gradient_integral, true);

        mPagerVp = findViewById(R.id.pager_vp);
        InnerPagerAdapter mPagerAdapter = new InnerPagerAdapter(getSupportFragmentManager(), 0);
        mPagerVp.setAdapter(mPagerAdapter);

        mIntegralTv = findViewById(R.id.my_integral_tv);
        mSignInTv = findViewById(R.id.sign_in_tv);
        tabRg = findViewById(R.id.tab_rg);
        taskRb = findViewById(task_rb);
        detailRb = findViewById(R.id.detail_rb);
    }



    @Override
    protected void bindListener() {
        mPagerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {
                MaterialRadioButton child = (MaterialRadioButton) tabRg.getChildAt(position);
                child.setSelected(true);
            }

        });

        taskRb.setOnCheckedChangeListener((compoundButton, isCheck) -> {
            if (!isCheck) {
                return;
            }
            mPagerVp.setCurrentItem(0);
        });
        detailRb.setOnCheckedChangeListener((compoundButton, isCheck) -> {
            if (!isCheck) {
                return;
            }
            mPagerVp.setCurrentItem(1);
        });
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
    public void onError(ErrorResponse errorResponse) {
        stopLoading();
    }

    @Override
    public void bindIntegral(String integral) {
        mIntegralTv.setText(integral);
    }

    @Override
    public void signInSuccess() {
        mSignInTv.setText(R.string.already_sign_in);
        mSignInTv.setEnabled(false);
    }


    /**
     * 立即签到
     */
    public void signInClick(View view) {
        mModel.signIn();
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
