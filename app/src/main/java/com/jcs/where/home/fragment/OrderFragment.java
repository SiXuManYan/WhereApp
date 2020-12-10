package com.jcs.where.home.fragment;

import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class OrderFragment extends BaseFragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private OrderAdapter mAdapter;

    private List<OrderListFragment> mOrderListFragments;
    private String[] mTabTitles = new String[]{"全部", "代付款", "待使用", "待评价", "退款/售后"};

    @Override
    protected void initView(View view) {
        mViewPager = view.findViewById(R.id.viewpager);
        mTabLayout = view.findViewById(R.id.orderTabLayout);
        for (int i = 0; i < mTabTitles.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText(mTabTitles[i]);
            mTabLayout.addTab(tab);
        }

//        mTabLayout.selectTab(mTabLayout.getTabAt(0));
    }

    @Override
    protected void initData() {
        mOrderListFragments = new ArrayList<>();
        mOrderListFragments.add(new OrderListFragment(OrderListFragment.OrderType.All));
        mOrderListFragments.add(new OrderListFragment(OrderListFragment.OrderType.WaitForPay));
        mOrderListFragments.add(new OrderListFragment(OrderListFragment.OrderType.WaitForUse));
        mOrderListFragments.add(new OrderListFragment(OrderListFragment.OrderType.WaitForComment));
        mOrderListFragments.add(new OrderListFragment(OrderListFragment.OrderType.AfterSale));

        mAdapter = new OrderAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        deployView();
    }

    private void deployView() {
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    class OrderAdapter extends FragmentStatePagerAdapter {

        public OrderAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mOrderListFragments.get(position);
        }

        @Override
        public int getCount() {
            return mOrderListFragments.size();
        }
    }
}
