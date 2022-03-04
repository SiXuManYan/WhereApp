package com.jcs.where.features.coupon.good

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallCategory
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.coupon.good.child.CouponGoodFragment
import com.jcs.where.features.mall.home.MallHomePresenter
import com.jcs.where.features.mall.home.MallHomeView
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_mall_home.*

/**
 * Created by Wangsw  2022/3/4 14:29.
 * 优惠券商品列表
 */
class CouponGoodActivity :BaseMvpActivity<MallHomePresenter>(), MallHomeView {


    private var couponId = 0

    private var firstCategory: ArrayList<MallCategory> = ArrayList()

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.actitiy_coupon_good

    override fun initView() {
        couponId = intent.getIntExtra(Constant.PARAM_ID, 0)
    }

    override fun initData() {
        presenter = MallHomePresenter(this)
        presenter.getFirstCategory()
    }

    override fun bindListener() {

    }

    override fun bindCategory(response: ArrayList<MallCategory>, titles: ArrayList<String>) {
        if (response.isEmpty()) {
            return
        }
        firstCategory.clear()
        firstCategory.addAll(response)

        pager_vp.offscreenPageLimit = response.size
        pager_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageSelected(position: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit
        })

        val innerPagerAdapter = InnerPagerAdapter(supportFragmentManager, 0)
        innerPagerAdapter.notifyDataSetChanged()
        pager_vp.adapter = innerPagerAdapter
        tabs_stl.setViewPager(pager_vp, titles.toTypedArray())

    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = firstCategory[position].name


        override fun getItem(position: Int): Fragment = CouponGoodFragment().apply {
            mCategoryId = firstCategory[position].id
            mCouponId = couponId
        }

        override fun getCount(): Int = firstCategory.size
    }
}