package com.jcs.where.government.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.fragment.MechanismListFragment;
import com.jcs.where.government.model.GovernmentMapModel;
import com.jcs.where.view.popup.PopupConstraintLayout;
import com.jcs.where.view.popup.PopupConstraintLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.annotations.NonNull;

/**
 * 政府机构地图页
 * 政府机构列表是本页的fragment
 * create by zyf on 2020/12/28 7:44 PM
 */
public class GovernmentMapActivity extends BaseActivity {
    private PopupConstraintLayout mBottomPopupLayout;
    private ImageView mTopArrowIv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MechanismAdapter mViewPagerAdapter;
    private List<MechanismListFragment> mMechanismListFragments;

    private GovernmentMapModel mModel;
    private List<CategoryResponse> mTabCategories;

    @Override
    protected void initView() {
        mTopArrowIv = findViewById(R.id.topArrowIv);
        mTabLayout = findViewById(R.id.governmentTabs);
        mViewPager = findViewById(R.id.governmentViewPager);
        mBottomPopupLayout = findViewById(R.id.bottomPopupLayout);
        // 绑定PopupLayout适配器
        bindPopupLayoutAdapter();

    }

    private void bindPopupLayoutAdapter() {
        mBottomPopupLayout.setAdapter(new PopupConstraintLayoutAdapter() {
            @Override
            public boolean isGoneAfterBottom() {
                return false;
            }

            @Override
            public int getMinHeight() {
                return getPxFromDp(55);
            }

            @Override
            public int getMaxHeight() {
                return getPxFromDp(583);
            }
        });
    }

    @Override
    protected void initData() {
        mModel = new GovernmentMapModel();
        mTabCategories = new ArrayList<>();
        mMechanismListFragments = new ArrayList<>();
        mViewPagerAdapter = new MechanismAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mModel.getCategories(new BaseObserver<List<CategoryResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull List<CategoryResponse> categoryResponses) {
                mTabCategories.clear();
                mMechanismListFragments.clear();
                CategoryResponse allCategory = new CategoryResponse();
                allCategory.setName(getString(R.string.all));
                mTabCategories.add(allCategory);
                // 0 表示要获得全部的信息
                mMechanismListFragments.add(MechanismListFragment.newInstance(0));
                mTabCategories.addAll(categoryResponses);
                int size = categoryResponses.size();
                mTabLayout.removeAllTabs();
                for (int i = 0; i < size; i++) {
                    TabLayout.Tab tab = mTabLayout.newTab();
                    CategoryResponse categoryResponse = mTabCategories.get(i);
                    tab.setCustomView(makeTabView(categoryResponse.getName()));
                    mTabLayout.addTab(tab);
                    mMechanismListFragments.add(MechanismListFragment.newInstance(categoryResponse.getId()));
                }

                mViewPager.setAdapter(mViewPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setOffscreenPageLimit(mTabCategories.size());
            }
        });
    }

    private View makeTabView(String title) {
        View tabView = LayoutInflater.from(this).inflate(R.layout.tab_normal_only_text, null);
        TextView tabTitle = tabView.findViewById(R.id.tabTitle);
        tabTitle.setText(title);
        return tabView;
    }

    @Override
    protected void bindListener() {
        mTopArrowIv.setOnClickListener(this::onTopArrowClick);
    }

    private void onTopArrowClick(View view) {
        mBottomPopupLayout.showOrHide();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_government_map;
    }

    class MechanismAdapter extends FragmentStatePagerAdapter {

        public MechanismAdapter(@androidx.annotation.NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabCategories.get(position).getName();
        }

        @androidx.annotation.NonNull
        @Override
        public Fragment getItem(int position) {
            return mMechanismListFragments.get(position);
        }

        @Override
        public int getCount() {
            return mMechanismListFragments.size();
        }
    }
}
