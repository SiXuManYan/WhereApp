package com.jiechengsheng.city.news.adapter;

import com.jiechengsheng.city.api.response.NewsChannelResponse;
import com.jiechengsheng.city.news.fragment.NewsFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class NewsViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<NewsFragment> mNewsFragments;
    private List<NewsChannelResponse> mTabCategories;

    public NewsViewPagerAdapter(@androidx.annotation.NonNull FragmentManager fm, int behavior) {
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
        return mNewsFragments.get(position);
    }

    @Override
    public int getCount() {
        return mNewsFragments.size();
    }

    public void setNewsFragments(List<NewsFragment> newsFragments) {
        this.mNewsFragments = newsFragments;
    }

    public void setTabCategories(List<NewsChannelResponse> tabCategories) {
        this.mTabCategories = tabCategories;
    }
}