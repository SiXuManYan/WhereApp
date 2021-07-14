package com.jcs.where.features.store.cart

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.features.store.cart.child.StoreCartFragment
import kotlinx.android.synthetic.main.activity_store_cart.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/7/5 10:30.
 *
 */
class StoreCartActivity : BaseActivity() {

    val titles = arrayOf(StringUtils.getString(R.string.take_carts), StringUtils.getString(R.string.delivery_carts))

    override fun getLayoutId() = R.layout.activity_store_cart

    override fun isStatusDark(): Boolean = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        pager.offscreenPageLimit = titles.size
        pager.adapter = StorePagerAdapter(supportFragmentManager, 0)
        tabs_type.setViewPager(pager)
    }

    override fun initData() = Unit

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }

        edit_tv.setOnClickListener {
            right_vs.displayedChild = 1
            EventBus.getDefault().post(BaseEvent<Boolean>(EventCode.EVENT_STORE_CART_HANDLE, true))

        }

        cancel_tv.setOnClickListener {
            right_vs.displayedChild = 0
            EventBus.getDefault().post(BaseEvent<Boolean>(EventCode.EVENT_STORE_CART_HANDLE, false))

        }


    }

    private inner class StorePagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {


        override fun getPageTitle(position: Int): CharSequence = titles[position]

        override fun getItem(position: Int): Fragment = StoreCartFragment.newInstance(position)

        override fun getCount(): Int = 2
    }

}