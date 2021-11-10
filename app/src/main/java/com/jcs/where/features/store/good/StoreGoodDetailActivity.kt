package com.jcs.where.features.store.good

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.store.StoreGoodDetail
import com.jcs.where.api.response.store.StoreGoodsCommit
import com.jcs.where.api.response.store.StoreOrderCommitData
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.store.order.StoreOrderCommitActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import kotlinx.android.synthetic.main.activity_store_good_detail.*
import kotlinx.android.synthetic.main.layout_store_good_detail_cart.*
import kotlinx.android.synthetic.main.vp_item_image.*
import pl.droidsonroids.gif.GifImageView
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/18 14:23.
 * 商城商品详情
 */
class StoreGoodDetailActivity : BaseMvpActivity<StoreGoodDetailPresenter>(), StoreGoodDetailView {

    var good_id: Int = 0
    var shop_id: Int = 0

    var isBuyNow = false

    /**
     * 配送时间
     */
    var delivery_times = ""

    /**
     * 自取时间
     */
    var take_times = ""

    /**
     * 配送费
     */
    var delivery_fee: Float = 0f

    /**
     * 商家名称
     */
    var shop_name = ""

    /**
     * 商品图片
     */
    var good_image = ""
    var good_name = ""

    var now_price: BigDecimal = BigDecimal.ZERO


    override fun getLayoutId() = R.layout.activity_store_good_detail

    override fun initView() {

        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        good_id = bundle.getInt(Constant.PARAM_ID, 0)
        delivery_fee = bundle.getFloat(Constant.PARAM_DELIVERY_FEE, 0f)
        shop_name = bundle.getString(Constant.PARAM_SHOP_NAME, "")
        shop_id = bundle.getInt(Constant.PARAM_SHOP_ID)

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
            alwaysEnableCut = true
            MIN_GOOD_NUM = 1
            cut_iv.setImageResource(R.mipmap.ic_cut_blue)
            add_iv.setImageResource(R.mipmap.ic_add_blue)
            updateNumberJudgeMin(1)
            cut_iv.visibility = View.VISIBLE
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
            if (!User.isLogon()) {
                startActivity(LoginActivity::class.java)
                return@setOnClickListener
            }
            cart_ll.visibility = View.VISIBLE
            handle_tv.text = buy_after_tv.text
            isBuyNow = false
        }
        buy_now_tv.setOnClickListener {
            if (!User.isLogon()) {
                startActivity(LoginActivity::class.java)
                return@setOnClickListener
            }
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

            val deliveryType = if (express_rb.isChecked) {
                2
            } else {
                1
            }

            val goodNum = number_view.goodNum
            val finalPrice = BigDecimalUtil.mul(now_price, BigDecimal(goodNum))

            if (isBuyNow) {
                val goodInfo = StoreGoodsCommit().apply {
                    good_id = this@StoreGoodDetailActivity.good_id
                    delivery_type = deliveryType
                    image = good_image
                    goodName = good_name
                    good_num = goodNum
                    price = finalPrice
                }

                val apply = StoreOrderCommitData().apply {
                    shop_id = this@StoreGoodDetailActivity.shop_id
                    shop_title = this@StoreGoodDetailActivity.shop_name
                    delivery_type = deliveryType
                    delivery_fee = this@StoreGoodDetailActivity.delivery_fee
                    goods.add(goodInfo)

                }

                val appList: ArrayList<StoreOrderCommitData> = ArrayList()
                appList.add(apply)

                startActivityAfterLogin(StoreOrderCommitActivity::class.java, Bundle().apply {
                    putSerializable(Constant.PARAM_ORDER_COMMIT_DATA, appList)
                })
                cart_ll.visibility = View.GONE
                number_view.updateNumberJudgeMin(1)
            } else {
                presenter.addCart(good_id, goodNum, deliveryType)
            }


        }


    }


    override fun bindData(data: StoreGoodDetail) {
        val images = data.images
        if (images.isNotEmpty()) {
            top_banner.setImageUrls(images).start()
            good_image = images[0]
        }

        title_tv.text = data.title
        good_name = data.title
        now_price = data.price
        now_price_tv.text = StringUtils.getString(R.string.price_unit_format, now_price.toPlainString())
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
        val inventory = data.inventory
        stock_tv.text = getString(R.string.stock_format, inventory)
        number_view.MAX_GOOD_NUM = inventory
    }


    override fun addSuccess() {
        cart_ll.visibility = View.GONE
        number_view.updateNumberJudgeMin(1)
    }
}