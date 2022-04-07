package com.jcs.where.government.adapter;

import com.jcs.where.api.response.CategoryResponse;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * create by zyf on 2021/1/5 4:30 下午
 */
public class MapListFragmentAdapter extends FragmentStatePagerAdapter {
    private List<? extends Fragment> mListFragment;
    private List<CategoryResponse> mTabCategories;

    public MapListFragmentAdapter(@androidx.annotation.NonNull FragmentManager fm, int behavior) {
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
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }

    public void setListFragments(List<? extends Fragment> listFragments) {
        this.mListFragment = listFragments;
    }

    public void setTabCategories(List<CategoryResponse> tabCategories) {
        this.mTabCategories = tabCategories;
    }
}
