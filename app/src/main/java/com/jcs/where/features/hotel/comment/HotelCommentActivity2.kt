package com.jcs.where.features.hotel.comment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.StringUtils
import com.google.android.material.radiobutton.MaterialRadioButton
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.hotel.comment.child.HotelCommentFragment
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_hotel_comment_2.*

/**
 * Created by Wangsw  2021/8/17 10:46.
 * 酒店评价
 */
class HotelCommentActivity2 : BaseActivity() {

    val TAB_TITLES =
        arrayOf(
            StringUtils.getString(R.string.all),
            StringUtils.getString(R.string.picture),
            StringUtils.getString(R.string.bad_reviews),
            StringUtils.getString(R.string.high_option)
        )

    private var hotelId = 0

    override fun isStatusDark(): Boolean = true


    override fun getLayoutId() = R.layout.activity_hotel_comment_2

    override fun initView() {
        hotelId = intent.getIntExtra(Constant.PARAM_ID, 0)


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
            HotelCommentFragment.newInstance(hotelId, position)

        override fun getCount(): Int = TAB_TITLES.size
    }


}