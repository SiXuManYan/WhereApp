package com.jiechengsheng.city.features.gourmet.comment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.google.android.material.radiobutton.MaterialRadioButton
import com.jiechengsheng.city.R
import com.jiechengsheng.city.base.BaseActivity
import com.jiechengsheng.city.utils.Constant
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
                    StringUtils.getString(R.string.latest),
                    StringUtils.getString(R.string.low_rating),
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
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {
                val rb = sort_rg.getChildAt(position) as MaterialRadioButton
                rb.isChecked = true
            }

        })


    }

    override fun initData() = Unit

    override fun bindListener() {
        sort_rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.all_rb -> {
                    pager.currentItem = 0
                }
                R.id.picture_rb -> {
                    pager.currentItem = 1
                }
                R.id.bad_reviews_rb -> {
                    pager.currentItem = 2
                }
                R.id.high_option_rb -> {
                    pager.currentItem = 3
                }
                else -> {
                }
            }


        }
    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {


        override fun getPageTitle(position: Int): CharSequence? = TAB_TITLES[position]


        override fun getItem(position: Int): Fragment =
                FoodCommentFragment.newInstance(restaurantId, position)

        override fun getCount(): Int = TAB_TITLES.size
    }

}