package com.jcs.where.government.adapter;

import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.government.fragment.MechanismListFragment;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * create by zyf on 2021/1/5 4:30 下午
 */
public class MechanismAdapter extends FragmentStatePagerAdapter {
    private List<? extends Fragment> mMechanismListFragments;
    private List<CategoryResponse> mTabCategories;

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

    public void setMechanismListFragments(List<? extends Fragment> mechanismListFragments) {
        this.mMechanismListFragments = mechanismListFragments;
    }

    public void setTabCategories(List<CategoryResponse> tabCategories) {
        this.mTabCategories = tabCategories;
    }
}
