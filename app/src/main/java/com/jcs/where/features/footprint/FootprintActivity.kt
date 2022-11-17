package com.jcs.where.features.footprint

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.footprint.child.CityFootprintFragment
import kotlinx.android.synthetic.main.actitiy_collection.*

/**
 * Created by Wangsw  2021/11/16 15:48.
 * 我的足迹
 */
class FootprintActivity : BaseActivity() {

    val TAB_TITLES =
        arrayOf(
            StringUtils.getString(R.string.collection_tab_same_city),
            StringUtils.getString(R.string.collection_tab_good)
        )

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.actitiy_collection

    override fun initView() {
        mJcsTitle.setMiddleTitle(getString(R.string.mine_footprint_title))
        pager.apply {
            offscreenPageLimit = TAB_TITLES.size
            adapter = InnerPagerAdapter(supportFragmentManager, 0)
        }
        tabs.setViewPager(pager)

    }

    override fun initData() = Unit

    override fun bindListener() = Unit

    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = TAB_TITLES[position]

        override fun getItem(position: Int): Fragment {

            return CityFootprintFragment().apply {
                type = position
            }


        }

        override fun getCount(): Int = TAB_TITLES.size
    }

}