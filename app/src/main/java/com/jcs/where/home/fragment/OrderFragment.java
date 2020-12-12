package com.jcs.where.home.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.OrderNumResponse;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.home.model.OrderModel;
import com.jcs.where.home.watcher.EmptyTextWatcher;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class OrderFragment extends BaseFragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private OrderAdapter mAdapter;
    private View mTopBg;
    private OrderModel mModel;
    private EditText mSearchEt;

    private List<OrderListFragment> mOrderListFragments;
    private String[] mTabTitles = new String[]{"全部", "待付款", "待使用", "待评价", "退款/售后"};

    @Override
    protected void initView(View view) {
        mTopBg = view.findViewById(R.id.topBg);
        setMargins(mTopBg, 0, getStatusBarHeight(), 0, 0);
        mViewPager = view.findViewById(R.id.viewpager);
        mTabLayout = view.findViewById(R.id.orderTabLayout);
        mSearchEt = view.findViewById(R.id.searchEt);
    }


    @Override
    protected void initData() {
        mModel = new OrderModel();
        mOrderListFragments = new ArrayList<>();
        mOrderListFragments.add(new OrderListFragment(OrderListFragment.OrderType.All));
        mOrderListFragments.add(new OrderListFragment(OrderListFragment.OrderType.WaitForPay));
        mOrderListFragments.add(new OrderListFragment(OrderListFragment.OrderType.WaitForUse));
        mOrderListFragments.add(new OrderListFragment(OrderListFragment.OrderType.WaitForComment));
        mOrderListFragments.add(new OrderListFragment(OrderListFragment.OrderType.AfterSale));

        mAdapter = new OrderAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        deployView();

        mModel.getOrderNum(new BaseObserver<OrderNumResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull OrderNumResponse orderNumResponse) {
                StringBuilder stringBuilder = new StringBuilder();
                mTabTitles[0] = mTabTitles[0] + getTabTitleSuffix(stringBuilder, orderNumResponse.getAll());
                clearStringBuilder(stringBuilder);
                mTabTitles[1] = mTabTitles[1] + getTabTitleSuffix(stringBuilder, orderNumResponse.getPay());
                clearStringBuilder(stringBuilder);
                mTabTitles[2] = mTabTitles[2] + getTabTitleSuffix(stringBuilder, orderNumResponse.getUse());
                clearStringBuilder(stringBuilder);
                mTabTitles[3] = mTabTitles[3] + getTabTitleSuffix(stringBuilder, orderNumResponse.getComment());
                clearStringBuilder(stringBuilder);
                mTabTitles[4] = mTabTitles[4] + getTabTitleSuffix(stringBuilder, orderNumResponse.getRefund());
                clearStringBuilder(stringBuilder);

                mViewPager.setAdapter(mAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setOffscreenPageLimit(5);
                initTabTitle();
            }
        });
    }

    private View makeTabView(String title) {
        View tabView = LayoutInflater.from(getContext()).inflate(R.layout.tab_order_fragment, null);
        TextView tabTitle = tabView.findViewById(R.id.tabTitle);
        tabTitle.setText(title);
        return tabView;
    }

    private void clearStringBuilder(StringBuilder stringBuilder) {
        stringBuilder.delete(0, stringBuilder.length());
    }

    private String getTabTitleSuffix(StringBuilder stringBuilder, int type) {
        if (type > 0) {
            return stringBuilder.append("(").append(type).append(")").toString();
        }
        return "";
    }

    private void deployView() {
    }

    @Override
    protected void bindListener() {
        mSearchEt.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String input = textView.getText().toString();
                OrderListFragment orderListFragment = getCurrentOrderListFragment();
                orderListFragment.getOrderByType(input);
                return true;
            }
            return false;
        });

        mSearchEt.addTextChangedListener(new EmptyTextWatcher() {
            @Override
            protected void onEtEmpty() {
                OrderListFragment orderListFragment = getCurrentOrderListFragment();
                orderListFragment.getOrderByType();
            }
        });
    }

    private OrderListFragment getCurrentOrderListFragment() {
        int currentItem = mViewPager.getCurrentItem();
        return mOrderListFragments.get(currentItem);
    }

    private void initTabTitle() {
        mTabLayout.removeAllTabs();
        for (String mTabTitle : mTabTitles) {
            TabLayout.Tab tab = mTabLayout.newTab();
            if (tab != null) {
                tab.setCustomView(makeTabView(mTabTitle));
                mTabLayout.addTab(tab);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    class OrderAdapter extends FragmentStatePagerAdapter {

        public OrderAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
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
