package com.jcs.where.features.store.detail.comment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.StringUtils
import com.google.android.material.radiobutton.MaterialRadioButton
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.store.detail.comment.chiild.StoreCommentChildFragment
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_store_comment.*

/**
 * Created by Wangsw  2021/7/17 15:54.
 * 全部评价
 */
class StoreCommentActivity : BaseActivity() {

    var shop_id: Int = 0

    val TAB_TITLES =
            arrayOf(StringUtils.getString(R.string.all),
                    StringUtils.getString(R.string.has_image),
                    StringUtils.getString(R.string.newest)
            )

    override fun getLayoutId() = R.layout.activity_store_comment

    override fun isStatusDark() = true

    override fun initView() {

        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        shop_id = bundle.getInt(Constant.PARAM_SHOP_ID, 0)


//        pager.setNoScroll(true)
        pager.offscreenPageLimit = TAB_TITLES.size
        pager.adapter = InnerPagerAdapter(supportFragmentManager, 0)

    }

    override fun initData() = Unit

    override fun bindListener() {

        type_rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.all_rb -> pager.setCurrentItem(0, true)
                R.id.image_rb -> pager.setCurrentItem(1, true)
                R.id.newest_rb -> pager.setCurrentItem(2, true)
            }
        }
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {
                val child = type_rg.getChildAt(position) as MaterialRadioButton
                child.isChecked = true
            }

        })


    }

    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {


        override fun getPageTitle(position: Int): CharSequence? = TAB_TITLES[position]


        override fun getItem(position: Int): Fragment =
                StoreCommentChildFragment.newInstance(shop_id, true, position + 1)

        override fun getCount(): Int = TAB_TITLES.size
    }

}