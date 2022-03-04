package com.jcs.where.features.coupon.user

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.coupon.user.child.MyCouponFragment
import kotlinx.android.synthetic.main.activity_card_coupon.*

/**
 * Created by Wangsw  2021/8/16 11:19.
 * 我的卡券
 */
class MyCouponActivity : BaseActivity() {

    val TAB_TITLES =
        arrayOf(
            StringUtils.getString(R.string.coupon_unused),
            StringUtils.getString(R.string.coupon_used),
            StringUtils.getString(R.string.coupon_expired),
        )

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_card_coupon

    override fun initView() {
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

            return MyCouponFragment().apply {
                type = position+1
            }

        }

        override fun getCount(): Int = TAB_TITLES.size
    }


}