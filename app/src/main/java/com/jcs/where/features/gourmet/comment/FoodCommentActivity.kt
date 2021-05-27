package com.jcs.where.features.gourmet.comment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_food_comment.*

/**
 * Created by Wangsw  2021/5/27 16:10.
 * 美食评论
 */
class FoodCommentActivity : BaseActivity() {

    val TAB_TITLES =
            arrayOf(
                    StringUtils.getString(R.string.all),
                    StringUtils.getString(R.string.picture),
                    StringUtils.getString(R.string.high_option),
                    StringUtils.getString(R.string.bad_reviews),
            )

    private var restaurantId: String = ""

    override fun getLayoutId() = R.layout.activity_food_comment

    override fun isStatusDark(): Boolean = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))

        intent.getStringExtra(Constant.PARAM_ID)?.let {
            restaurantId = it
        }
        pager.offscreenPageLimit = TAB_TITLES.size
        pager.adapter = InnerPagerAdapter(supportFragmentManager, 0)
        tabs_type.setViewPager(pager)
    }

    override fun initData() = Unit

    override fun bindListener() = Unit


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {


        override fun getPageTitle(position: Int): CharSequence? = TAB_TITLES[position]


        override fun getItem(position: Int): Fragment =
                FoodCommentFragment.newInstance(restaurantId, position)

        override fun getCount(): Int = TAB_TITLES.size
    }

}