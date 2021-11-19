package com.jcs.where.features.merchant

import android.content.Intent
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jcs.where.R
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.merchant.form.SettledFormFragment
import kotlinx.android.synthetic.main.activity_merchant_settled_home.*

/**
 * Created by Wangsw  2021/11/19 14:15.
 * 商家入住
 */
class MerchantSettledActivity : BaseMvpActivity<MerchantSettledPresenter>(), MerchantSettledView {

    override fun getLayoutId() = R.layout.activity_merchant_settled_home


    override fun initView() {
        pager_vp.apply {
            setNoScroll(true)
            offscreenPageLimit = 3
            adapter = InnerPagerAdapter(supportFragmentManager, 0)
        }

    }

    override fun initData() {
        presenter = MerchantSettledPresenter(this)
    }

    override fun bindListener() {
        pager_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {

                when (position) {
                    0 -> {
                        type_form_tv.isChecked = true
                        type_period_tv.isChecked = false
                        type_result_tv.isChecked = false
                    }
                    1 -> {
                        type_form_tv.isChecked = false
                        type_period_tv.isChecked = true
                        type_result_tv.isChecked = false
                    }
                    2 -> {
                        type_form_tv.isChecked = false
                        type_period_tv.isChecked = false
                        type_result_tv.isChecked = true
                    }
                }
                one_v.isChecked = type_period_tv.isChecked
                two_v.isChecked = type_result_tv.isChecked

                for (index in 0 until desc_ll.childCount) {
                    val child = desc_ll.getChildAt(position) as AppCompatCheckedTextView
                    child.isChecked = false
                }
                val child = desc_ll.getChildAt(position) as AppCompatCheckedTextView
                child.isChecked = true

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
        super.onActivityResult(requestCode, resultCode, data)

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }

        when (baseEvent.code) {
            EventCode.EVENT_MERCHANT_POST_SUCCESS -> {
                pager_vp.currentItem = 1
            }
            else -> {

            }
        }
    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = ""

        override fun getItem(position: Int): Fragment = SettledFormFragment()

        override fun getCount(): Int = 3
    }


}