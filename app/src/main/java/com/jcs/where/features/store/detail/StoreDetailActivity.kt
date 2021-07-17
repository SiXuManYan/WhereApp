package com.jcs.where.features.store.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.request.StoreShopRequest
import com.jcs.where.api.response.store.StoreDetail
import com.jcs.where.api.response.store.StoreGoods
import com.jcs.where.api.response.store.StoreGoodsCommit
import com.jcs.where.api.response.store.StoreOrderCommitData
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.gourmet.comment.FoodCommentFragment
import com.jcs.where.features.store.detail.comment.StoreCommentFragment
import com.jcs.where.features.store.detail.good.StoreGoodFragment
import com.jcs.where.features.store.order.StoreOrderCommitActivity
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_store_detail.*
import kotlinx.android.synthetic.main.layout_store_good_detail_cart.*
import org.greenrobot.eventbus.EventBus
import pl.droidsonroids.gif.GifImageView
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/15 10:33.
 * 商城店铺详情
 */
class StoreDetailActivity : BaseMvpActivity<StoreDetailPresenter>(), StoreDetailView {

    var shop_id: Int = 0
    var shop_name: String = ""

    /**
     * 配送费
     */
    var delivery_fee: Float = 0f


    /**
     * 配送时间
     */
    var delivery_times = ""

    /**
     * 自取时间
     */
    var take_times = ""

    val TAB_TITLES =
            arrayOf(StringUtils.getString(R.string.good),
                    StringUtils.getString(R.string.comment))

    var isBuyNow = false

    var goodSelect: StoreGoods? = null

    var now_price: BigDecimal = BigDecimal.ZERO

    var good_id = 0
    var good_image = ""
    var good_name = ""

    override fun getLayoutId() = R.layout.activity_store_detail

    override fun isStatusDark() = true

    override fun initView() {


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

        pager.setNoScroll(true)
        pager.offscreenPageLimit = TAB_TITLES.size
        pager.adapter = InnerPagerAdapter(supportFragmentManager, 0)
        tabs_type.setViewPager(pager)

        // number

        number_view.apply {
            MIN_GOOD_NUM = 1
            updateNumber(1)
            alwaysEnableCut(true)
        }

        handle_tv.text = getString(R.string.buy_now)
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

    override fun initData() {
        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        shop_id = bundle.getInt(Constant.PARAM_ID, 0)

        presenter = StoreDetailPresenter(this)
        presenter.getDetail(shop_id)
    }

    override fun bindListener() {
        fold_ll.setOnClickListener {
            if (hide_ll.visibility != View.VISIBLE) {
                hide_ll.visibility = View.VISIBLE
                fold_tv.text = getString(R.string.collapse_details)
                fold_iv.rotation = 180f
            } else {
                hide_ll.visibility = View.GONE
                fold_tv.text = getString(R.string.look_more)
                fold_iv.rotation = 0f
            }
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
                    good_id = this@StoreDetailActivity.good_id
                    delivery_type = deliveryType
                    image = good_image
                    goodName = good_name
                    good_num = goodNum
                    price = finalPrice
                }

                val apply = StoreOrderCommitData().apply {
                    shop_id = this@StoreDetailActivity.shop_id
                    shop_title = this@StoreDetailActivity.shop_name
                    delivery_type = deliveryType
                    delivery_fee = this@StoreDetailActivity.delivery_fee
                    goods.add(goodInfo)
                }

                val appList: ArrayList<StoreOrderCommitData> = ArrayList()
                appList.add(apply)

                startActivityAfterLogin(StoreOrderCommitActivity::class.java, Bundle().apply {
                    putSerializable(Constant.PARAM_ORDER_COMMIT_DATA, appList)
                })
                cart_ll.visibility = View.GONE
                number_view.updateNumber(1)
            }


        }


    }

    override fun bindDetail(data: StoreDetail) {

        top_banner.setImageUrls(data.images).start()
        title_tv.text = data.title
        shop_name = data.title

        nav_tv.setOnClickListener {
            if (data.lat != 0f && data.lng != 0f) {
                FeaturesUtil.startNaviGoogle(this, data.lat, data.lng)
            }
        }

        if (data.im_status == 1 && !TextUtils.isEmpty(data.mer_uuid)) {
            phone_tv.text = getString(R.string.merchant)
            phone_tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.ic_store_service, 0, 0)
            phone_tv.setOnClickListener {
                RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, data.mer_uuid, data.mer_name, null)
            }
        } else {
            phone_tv.text = getString(R.string.telephone)
            phone_tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.ic_store_phone_blue, 0, 0)
            phone_tv.setOnClickListener {
                val tel = data.tel
                if (!TextUtils.isEmpty(tel)) {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        this.data = Uri.parse("tel:$tel")
                    }
                    startActivity(intent)
                }
            }
        }

        val stringArray = StringUtils.getStringArray(R.array.weeks)
        time_value_tv.text = getString(R.string.mechanism_to_format, data.start_time, data.end_time, stringArray[data.week_start - 1], stringArray[data.week_end - 1])
        phone_value_tv.text = data.tel
        web_value_tv.text = data.web_site
        email_value_tv.text = data.email
        facebook_value_tv.text = data.facebook
        address_tv.text = data.address
        delivery_fee = data.delivery_fee
        EventBus.getDefault().post(BaseEvent<StoreShopRequest>(StoreShopRequest(shop_name, delivery_fee)))

        delivery_times = data.delivery_times
        take_times = data.take_times

    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {


        override fun getPageTitle(position: Int): CharSequence? = TAB_TITLES[position]


        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                StoreGoodFragment.newInstance(shop_id)
            } else {
                StoreCommentFragment.newInstance(shop_id.toString(),false)
            }
        }

        override fun getCount(): Int = TAB_TITLES.size
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        val data = baseEvent.data
        if (data is StoreGoods) {
            cart_ll.visibility = View.VISIBLE
            isBuyNow = true
            now_price = data.price
            good_id = data.id

            if (data.images.isNotEmpty()) {
                good_image = data.images[0]
            }
            good_name = data.title


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

            stock_tv.text = getString(R.string.stock_format, data.inventory)
        }

    }
}