package com.jcs.where.integral;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

/**
 * 积分签到
 */
public class IntegralActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }


    @Override
    protected void initView() {


    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void initData() {

    }



    private class InnerPagerAdapter extends FragmentPagerAdapter{


        public InnerPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }



    }



}
