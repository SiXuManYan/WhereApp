package com.jcs.where.mine.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.base.BaseFragment;
import com.jcs.where.mine.fragment.ArticleListFragment;
import com.jcs.where.mine.fragment.SameCityListFragment;
import com.jcs.where.mine.fragment.VideoListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面-收藏列表
 * create by zyf on 2021/1/31 3:20 下午
 */
public class CollectionListActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] mTabTitle;
    private CollectionViewPagerAdapter mAdapter;

    private List<BaseFragment> mFragmentList;

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.collectionTabLayout);
        mViewPager = findViewById(R.id.viewPager);
    }

    @Override
    protected void initData() {
        mFragmentList = new ArrayList<>();
        mTabTitle = new String[]{
                getString(R.string.collection_tab_same_city),
                getString(R.string.collection_tab_article),
                getString(R.string.collection_tab_video)
        };

        mFragmentList.add(new SameCityListFragment());
        mFragmentList.add(new ArticleListFragment());
        mFragmentList.add(new VideoListFragment());
        mAdapter = new CollectionViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection_list;
    }

    public class CollectionViewPagerAdapter extends FragmentStatePagerAdapter {
        public CollectionViewPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitle[position];
        }

        @NonNull
        @NotNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


}
