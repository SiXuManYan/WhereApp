package com.jcs.where.integral;

import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.jcs.where.R;
import com.jcs.where.api.response.SignListResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.integral.child.detail.IntegralChildDetailFragment;
import com.jcs.where.integral.child.task.IntegralChildTaskFragment;
import com.jcs.where.widget.IntegralItemView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.jcs.where.R.id.task_rb;

/**
 * 我的积分
 */
public class IntegralActivity extends BaseMvpActivity<IntegralPresenter> implements IntegralView {

    private TextView mIntegralTv;
    private TextView mSignInTv;
    private RadioButton mTaskRb;
    private RadioButton mDetailRb;
    private RadioGroup mTabRg;

    private ViewPager mPagerVp;
    private LinearLayout mSignInContainerLl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }


    @Override
    protected void initView() {

        BarUtils.setStatusBarColor(this, R.drawable.shape_gradient_integral, true);

        mPagerVp = findViewById(R.id.pager_vp);
        mIntegralTv = findViewById(R.id.my_integral_tv);
        mSignInTv = findViewById(R.id.sign_in_tv);
        mTabRg = findViewById(R.id.tab_rg);
        mTaskRb = findViewById(task_rb);
        mDetailRb = findViewById(R.id.detail_rb);
        mSignInContainerLl = findViewById(R.id.sign_in_container_ll);

        InnerPagerAdapter mPagerAdapter = new InnerPagerAdapter(getSupportFragmentManager(), 0);
        mPagerVp.setAdapter(mPagerAdapter);
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
                MaterialRadioButton child = (MaterialRadioButton) mTabRg.getChildAt(position);
                child.setSelected(true);

                if (position == 0) {
                    mTaskRb.setTypeface(null, Typeface.BOLD);
                    mDetailRb.setTypeface(null, Typeface.NORMAL);
                } else {
                    mTaskRb.setTypeface(null, Typeface.NORMAL);
                    mDetailRb.setTypeface(null, Typeface.BOLD);
                }

            }

        });
        mTaskRb.setOnCheckedChangeListener((compoundButton, isCheck) -> {
            if (isCheck) {
                mTaskRb.setTypeface(null, Typeface.BOLD);
                mPagerVp.setCurrentItem(0);
            } else {
                mTaskRb.setTypeface(null, Typeface.NORMAL);
            }

        });
        mDetailRb.setOnCheckedChangeListener((compoundButton, isCheck) -> {
            if (isCheck) {
                mDetailRb.setTypeface(null, Typeface.BOLD);
                mPagerVp.setCurrentItem(1);
            } else {
                mDetailRb.setTypeface(null, Typeface.NORMAL);
            }

        });
    }

    @Override
    protected void initData() {
        presenter = new IntegralPresenter(this);
        presenter.getUserInfo();
        presenter.getSignInList();
    }

    /**
     * 绑定积分列表
     *
     * @param response
     */
    @Override
    public void bindSignInList(SignListResponse response) {
        stopLoading();
        List<SignListResponse.DataBean> data = response.getData();
        for (int i = 0; i < data.size(); i++) {
            IntegralItemView child = (IntegralItemView) mSignInContainerLl.getChildAt(i);
            child.setContent(i, data.get(i));
        }
        if (mSignInContainerLl.getVisibility() != View.VISIBLE) {
            mSignInContainerLl.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置用户积分
     *
     * @param integral 积分
     * @param isSigned
     */
    @Override
    public void bindUserIntegral(String integral, boolean isSigned) {
        mIntegralTv.setText(integral);
        changeSignStatus(isSigned);

//        new Handler(Looper.myLooper()).postDelayed(() -> {
//
//        }, 2000);
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_SIGN_IN_CHANGE_STATUS, isSigned));
    }

    @Override
    public void signInSuccess() {
        changeSignStatus(true);
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_SIGN_IN_CHANGE_STATUS, true));
    }




    @Override
    public void onEventReceived(BaseEvent<?> baseEvent) {
        if (baseEvent.code == EventCode.EVENT_SIGN_IN_REQUEST) {
            mSignInTv.performClick();
        }

    }

    /**
     * 签到
     *
     * @param view
     */
    public void signInClick(View view) {
        presenter.signIn();
    }


    private void changeSignStatus(boolean isSigned) {
        if (mSignInTv.getVisibility() != View.VISIBLE) {
            mSignInTv.setVisibility(View.VISIBLE);
        }
        mSignInTv.setEnabled(!isSigned);
        if (isSigned) {
            mSignInTv.setText(R.string.already_sign_in);
        } else {
            mSignInTv.setText(R.string.sign_in_now);
        }
    }

    /**
     * 签到规则
     *
     * @param view
     */
    public void onRuleClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_in_rule)
                .setMessage(R.string.sign_in_rule_message)
                .setCancelable(false)
                .setPositiveButton(R.string.ensure, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .create().show();
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
