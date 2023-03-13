package com.jiechengsheng.city.features.gourmet.restaurant.list;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jiechengsheng.city.features.gourmet.restaurant.list.filter.area.AreaFilterFragment;
import com.jiechengsheng.city.features.gourmet.restaurant.list.filter.food.FoodCategoryFilterFragment;
import com.jiechengsheng.city.features.gourmet.restaurant.list.filter.more.MoreFilterFragment;

/**
 * Created by Wangsw  2021/3/30 11:06.
 */
public class RestaurantPagerAdapter extends FragmentStatePagerAdapter {

    public int pid = 0;
    public String pidName = "";

    public RestaurantPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AreaFilterFragment();
        } else if (position == 1) {
            FoodCategoryFilterFragment fragment = new FoodCategoryFilterFragment();
            fragment.pid = pid;
            fragment.pidName = pidName;
            return fragment;
        } else {
            return new MoreFilterFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
