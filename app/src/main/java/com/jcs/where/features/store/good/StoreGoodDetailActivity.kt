package com.jcs.where.features.store.good

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.store.StoreGoodDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import kotlinx.android.synthetic.main.activity_store_good_detail.*
import kotlinx.android.synthetic.main.layout_store_good_detail_cart.*
import pl.droidsonroids.gif.GifImageView

/**
 * Created by Wangsw  2021/6/18 14:23.
 * 商城商品详情
 */
class StoreGoodDetailActivity : BaseMvpActivity<StoreGoodDetailPresenter>(), StoreGoodDetailView {

    var good_id: Int = 0

    var isBuyNow = false

    /**
     * 配送时间
     */
    var delivery_times = ""

    /**
     * 自取时间
     */
    var take_times = ""


    override fun getLayoutId() = R.layout.activity_store_good_detail

    override fun initView() {

        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        good_id = bundle.getInt(Constant.PARAM_ID, 0)


        val bannerParams = ll_banner.layoutParams.apply {
            height = ScreenUtils.getScreenWidth() * 206 / 375
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
                        GlideUtil.load(context, url, image)
                    }

                    override fun loadGifs(context: Context, url: String, gifImageView: GifImageView, scaleType: ImageView.ScaleType) {
                        GlideUtil.load(context, url, gifImageView)
                    }
                })

        number_view.apply {
            MIN_GOOD_NUM = 1
            updateNumber(1)
            alwaysEnableCut(true)
        }
    }

    override fun isStatusDark() = true

    override fun initData() {
        presenter = StoreGoodDetailPresenter(this)
        presenter.getData(good_id)
    }

    override fun onResume() {
        super.onResume()
        top_banner.startPlayIfNeeded()
    }

    override fun onDestroy() {
        super.onDestroy()
        top_banner?.releaseBanner()
    }

    override fun onPause() {
        super.onPause()
        top_banner.pause()
    }


    override fun bindListener() {
        buy_after_tv.setOnClickListener {
            cart_ll.visibility = View.VISIBLE
            handle_tv.text = buy_after_tv.text
            isBuyNow = false
        }
        buy_now_tv.setOnClickListener {
            cart_ll.visibility = View.VISIBLE
            handle_tv.text = buy_now_tv.text
            isBuyNow = true
        }

        close_cart_v.setOnClickListener {
            cart_ll.visibility = View.GONE
        }

        business_service_rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.express_rb -> time_tv.text = delivery_times
                R.id.self_rb -> time_tv.text = take_times
            }
        }

        handle_tv.setOnClickListener {
            if (!express_rb.isChecked && !self_rb.isChecked) {
                ToastUtils.showShort(R.string.delivery_method_none)
                return@setOnClickListener
            }
        }


    }


    override fun bindData(data: StoreGoodDetail) {
        top_banner.setImageUrls(data.images).start()
        title_tv.text = data.title

        now_price_tv.text = StringUtils.getString(R.string.price_unit_format, data.price.toPlainString())
        val oldPrice = StringUtils.getString(R.string.price_unit_format, data.original_price.toPlainString())
        val builder = SpanUtils().append(oldPrice).setStrikethrough().create()
        old_price_tv.text = builder
        desc_tv.text = data.desc
        when (data.delivery_type) {
            1 -> {
                express_rb.visibility = View.GONE
                self_rb.visibility = View.VISIBLE
            }
            2 -> {
                express_rb.visibility = View.VISIBLE
                self_rb.visibility = View.GONE
            }
            3 -> {
                express_rb.visibility = View.VISIBLE
                self_rb.visibility = View.VISIBLE
            }
        }

        delivery_times = data.delivery_times
        take_times = data.take_times
        stock_tv.text = getString(R.string.stock_format ,data.inventory)
    }
}