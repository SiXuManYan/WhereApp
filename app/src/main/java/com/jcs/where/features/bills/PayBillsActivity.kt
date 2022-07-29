package com.jcs.where.features.bills

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.bills.channel.BillsChannelActivity
import com.jcs.where.features.bills.guide.BillGuideAdapter
import com.jcs.where.features.bills.guide.GuideItemClickListener
import com.jcs.where.features.bills.record.BillsRecordActivity
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocalLanguageUtil
import kotlinx.android.synthetic.main.activity_pay_bills.*

/**
 * Created by Wangsw  2021/4/15 14:12.
 * 水电缴费
 */
class PayBillsActivity : BaseActivity() {


    override fun getLayoutId() = R.layout.activity_pay_bills

    override fun isStatusDark() = true

    override fun initView() {
        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this)
        if (languageLocale.language == "zh") {
            hydropower_banner_iv.setImageResource(R.mipmap.ic_hydropower_chn)
        } else {
            hydropower_banner_iv.setImageResource(R.mipmap.ic_hydropower_en)
        }
        initPager()

    }

    private fun initPager() {
        val alreadyShow = CacheUtil.getShareDefault().getBoolean(Constant.SP_ALREADY_SHOW_BILLS_GUIDE, false)
        if (alreadyShow) {
            real_content_ll.visibility = View.VISIBLE
            pager_vp.visibility = View.GONE
            return
        }

        real_content_ll.visibility = View.GONE
        pager_vp.visibility = View.VISIBLE

        val adapter = BillGuideAdapter()
        adapter.itemClickListener = object : GuideItemClickListener {
            override fun pagerItemClick(position: Int) {
                if (position == adapter.count - 1) {
                    CacheUtil.getShareDefault().put(Constant.SP_ALREADY_SHOW_BILLS_GUIDE, true)
                    real_content_ll.visibility = View.VISIBLE
                    pager_vp.visibility = View.GONE
                }else {
                    pager_vp.setCurrentItem(position + 1, true)
                }
            }
        }
        pager_vp.adapter = adapter
        pager_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {

            }
        })

    }

    override fun initData() = Unit


    override fun bindListener() {
        prepaid_ll.setOnClickListener {
            startActivityAfterLogin(BillsChannelActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 1)
            })
        }
        water_ll.setOnClickListener {
            startActivityAfterLogin(BillsChannelActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 2)
            })
        }
        electric_ll.setOnClickListener {
            startActivityAfterLogin(BillsChannelActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 3)
            })
        }
        internet_ll.setOnClickListener {
            startActivityAfterLogin(BillsChannelActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 4)
            })
        }
        record_iv.setOnClickListener {
            startActivityAfterLogin(BillsRecordActivity::class.java)
        }

    }




}