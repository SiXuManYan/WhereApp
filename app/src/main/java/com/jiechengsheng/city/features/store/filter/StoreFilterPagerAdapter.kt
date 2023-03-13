package com.jiechengsheng.city.features.store.filter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jiechengsheng.city.features.store.filter.classification.SecondCategoryFragment
import com.jiechengsheng.city.features.store.filter.screen.ScreenFilterFragment

/**
 * Created by Wangsw  2021/6/10 16:12.
 */
class StoreFilterPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    var pid = 0

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            SecondCategoryFragment.newInstance(pid)
        } else {
            ScreenFilterFragment()
        }
    }

    override fun getCount(): Int = 2
}