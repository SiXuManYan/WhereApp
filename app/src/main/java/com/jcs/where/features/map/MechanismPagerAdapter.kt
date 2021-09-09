package com.jcs.where.features.map

import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jcs.where.api.response.category.Category
import com.jcs.where.features.map.child.MechanismChildFragment

/**
 * Created by Wangsw  2021/8/26 14:38.
 *
 */
class MechanismPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {


    var category: ArrayList<Category> = ArrayList()
    var total_count_tv:TextView ? = null


    override fun getPageTitle(position: Int): CharSequence = category[position].name

    override fun getCount() = category.size

    override fun getItem(position: Int): MechanismChildFragment = MechanismChildFragment().apply {
        categoryId = category[position].id.toString()
        totalCountView = total_count_tv
    }
}