package com.jcs.where.features.bills

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ScreenUtils
import com.jcs.where.R
import com.jcs.where.api.response.BannerResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.channel.BillsChannelActivity
import com.jcs.where.features.bills.guide.BillGuideAdapter
import com.jcs.where.features.bills.guide.GuideItemClickListener
import com.jcs.where.features.bills.record.BillsRecordActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import kotlinx.android.synthetic.main.activity_pay_bills.*
import pl.droidsonroids.gif.GifImageView

/**
 * Created by Wangsw  2021/4/15 14:12.
 * 水电缴费
 */
class PayBillsActivity : BaseMvpActivity<PayBillsPresenter>(), PayBillsView {


    override fun getLayoutId() = R.layout.activity_pay_bills

    override fun isStatusDark() = true

    override fun initView() {
        initBanner()
        initPager()

    }

    private fun initBanner() {
        val bannerParams = ll_banner.layoutParams.apply {
            height = ScreenUtils.getScreenWidth() * 177 / 345
        }
        ll_banner.layoutParams = bannerParams

        top_banner.setBannerTypes(XBanner.CIRCLE_INDICATOR)
            .setTitleHeight(50)
            .isAutoPlay(true)
            .setDelay(5000)
            .setUpIndicators(R.drawable.ic_selected, R.drawable.ic_unselected)
            .setUpIndicatorSize(6, 6)
            .setIndicatorGravity(XBanner.INDICATOR_CENTER)
            .setImageLoader(object : AbstractUrlLoader() {
                override fun loadImages(context: Context, url: String, image: ImageView) {
                    GlideUtil.load(context, url, image, 4)
                }

                override fun loadGifs(context: Context, url: String, gifImageView: GifImageView, scaleType: ImageView.ScaleType) {
                    GlideUtil.load(context, url, gifImageView, 4)
                }
            })
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
                } else {
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

    override fun initData() {
        presenter = PayBillsPresenter(this)
        presenter.getTopBanner()
    }


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


    override fun bindTopBannerData(bannerUrls: ArrayList<String>, response: ArrayList<BannerResponse>) {

        top_banner.setImageUrls(bannerUrls)
        top_banner.setBannerPageListener(object : XBanner.BannerPageListener {

            override fun onBannerDragging(item: Int) = Unit

            override fun onBannerIdle(item: Int) = Unit

            override fun onBannerClick(item: Int) {
                val data = response[item]
                BusinessUtils.handleBannerClick(this@PayBillsActivity, data)
            }

        }).start()
    }


    override fun onDestroy() {
        top_banner?.releaseBanner()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        top_banner?.start()
    }


    override fun onPause() {
        super.onPause()
        top_banner?.pause()
    }

}