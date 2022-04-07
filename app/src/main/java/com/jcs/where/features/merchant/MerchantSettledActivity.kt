package com.jcs.where.features.merchant

import android.content.Intent
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jcs.where.R
import com.jcs.where.api.request.merchant.MerchantSettledData
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.merchant.form.SettledFormFragment
import com.jcs.where.features.merchant.record.MerchantRecordActivity
import com.jcs.where.features.merchant.recult.SettledResultFragment
import kotlinx.android.synthetic.main.activity_merchant_settled_home.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/11/19 14:15.
 * 商家入住
 */
class MerchantSettledActivity : BaseMvpActivity<MerchantSettledPresenter>(), MerchantSettledView {


    override fun isStatusDark() = true

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
        presenter.getData()
        pager_vp.currentItem = 0
    }

    override fun bindListener() {
        pager_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {

                when (position) {
                    0 -> {
                        type_form_tv.isChecked = true
                        type_period_tv.isChecked = true
                        type_result_tv.isChecked = false
                    }
                    1 -> {
                        type_form_tv.isChecked = true
                        type_period_tv.isChecked = true
                        type_result_tv.isChecked = false
                    }
                    2 -> {
                        type_form_tv.isChecked = true
                        type_period_tv.isChecked = true
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

        record_tv.setOnClickListener {
            startActivity(MerchantRecordActivity::class.java)
        }
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
                EventBus.getDefault().post(BaseEvent(EventCode.EVENT_MERCHANT_CHANGE_TYPE, 1))
                pager_vp.currentItem = 1
            }
            EventCode.EVENT_MERCHANT_RECOMMIT -> {
                pager_vp.currentItem = 0
            }

            EventCode.EVENT_MERCHANT_COMMIT_NEW -> {
                pager_vp.currentItem = 0


            }
            else -> {

            }
        }
    }

    // 审核状态（1：待审核，2：审核通过，3：审核未通过）
    override fun bindData(response: MerchantSettledData?) {
        // 未提交过
        if (response == null) {
            pager_vp.currentItem = 0
            return
        }
        // 提交过后的审核状态
        when (response.is_verify) {
            1, 3 -> {
                EventBus.getDefault().post(BaseEvent(EventCode.EVENT_MERCHANT_CHANGE_TYPE, response))
                pager_vp.currentItem = 1
            }
            2 -> {
                pager_vp.currentItem = 2
            }
        }
    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = ""

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> SettledFormFragment()
                1 -> SettledResultFragment().apply {
                    type = 1
                }
                else -> SettledResultFragment().apply {
                    type = 2
                }
            }
        }

        override fun getCount(): Int = 3
    }


}