package com.jcs.where.features.store.cart

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.gourmet.comment.FoodCommentFragment
import kotlinx.android.synthetic.main.activity_store_cart.*

/**
 * Created by Wangsw  2021/7/5 10:30.
 *
 */
class StoreCartActivity : BaseActivity() {

    val TAB_TITLES =
            arrayOf(
                    getString(R.string.take_carts),
                    getString(R.string.delivery_carts)
            )

    override fun getLayoutId() = R.layout.activity_store_cart

    override fun isStatusDark(): Boolean = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        pager.offscreenPageLimit = TAB_TITLES.size
        pager.adapter = InnerPagerAdapter(supportFragmentManager, 0)
        tabs_type.setViewPager(pager)
    }

    override fun initData() {

    }

    override fun bindListener() {

    }

    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {


        override fun getPageTitle(position: Int): CharSequence? = TAB_TITLES[position]


        override fun getItem(position: Int): Fragment =
                FoodCommentFragment.newInstance("", position)


        override fun getCount(): Int = TAB_TITLES.size
    }

}