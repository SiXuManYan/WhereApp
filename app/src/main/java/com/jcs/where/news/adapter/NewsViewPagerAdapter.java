package com.jcs.where.news.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class NewsViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<? extends Fragment> mNewsFragments;
    private List<String> mTabCategories;

    public NewsViewPagerAdapter(@androidx.annotation.NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabCategories.get(position);
    }

    @androidx.annotation.NonNull
    @Override
    public Fragment getItem(int position) {
        return mNewsFragments.get(position);
    }

    @Override
    public int getCount() {
        return mNewsFragments.size();
    }

    public void setNewsFragments(List<? extends Fragment> mNewsFragments) {
        this.mNewsFragments = mNewsFragments;
    }

    public void setTabCategories(List<String> tabCategories) {
        this.mTabCategories = tabCategories;
    }
}