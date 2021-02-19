package com.jcs.where.features.message;

import android.graphics.Typeface;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

/**
 * Created by Wangsw  2021/2/19 17:07.
 * 消息中心
 */
public class MessageCenterActivity extends BaseActivity {


    private RadioButton mTaskRb;
    private RadioButton mDetailRb;
    private RadioGroup mTabRg;
    private ViewPager mPagerVp;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initView() {
        mPagerVp = findViewById(R.id.pager_vp);
        mTabRg = findViewById(R.id.tab_rg);
        mTaskRb = findViewById(R.id.task_rb);
        mDetailRb = findViewById(R.id.detail_rb);
    }

    @Override
    protected void initData() {

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


}
