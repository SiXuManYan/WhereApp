package com.jcs.where.features.gourmet.restaurant.list;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jcs.where.features.gourmet.restaurant.list.filter.area.AreaFilterFragment;
import com.jcs.where.features.gourmet.restaurant.list.filter.food.FoodCategoryFilterFragment;
import com.jcs.where.features.gourmet.restaurant.list.filter.more.MoreFilterFragment;

/**
 * Created by Wangsw  2021/3/30 11:06.
 */
public class RestaurantPagerAdapter extends FragmentStatePagerAdapter {

    public RestaurantPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AreaFilterFragment();
        } else if (position == 1) {
            return new FoodCategoryFilterFragment();
        } else {
            return new MoreFilterFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
