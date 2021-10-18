package com.jcs.where.features.travel.map

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jcs.where.api.response.category.Category
import com.jcs.where.features.travel.map.child.TravelChildFragment

/**
 * Created by Wangsw  2021/10/18 9:52.
 * 旅游地图
 */
class TravelPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {


    var category: ArrayList<Category> = ArrayList()


    override fun getPageTitle(position: Int): CharSequence = category[position].name

    override fun getCount() = category.size

    override fun getItem(position: Int): TravelChildFragment = TravelChildFragment().apply {
        categoryId = category[position].id
    }
}