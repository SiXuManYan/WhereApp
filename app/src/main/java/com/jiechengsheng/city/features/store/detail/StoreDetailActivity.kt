package com.jiechengsheng.city.features.store.detail

import android.app.Activity
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
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.StoreShopRequest
import com.jiechengsheng.city.api.response.store.StoreDetail
import com.jiechengsheng.city.api.response.store.StoreGoods
import com.jiechengsheng.city.api.response.store.StoreGoodsCommit
import com.jiechengsheng.city.api.response.store.StoreOrderCommitData
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.store.detail.comment.chiild.StoreCommentChildFragment
import com.jiechengsheng.city.features.store.detail.good.StoreGoodFragment
import com.jiechengsheng.city.features.store.order.StoreOrderCommitActivity
import com.jiechengsheng.city.frames.common.Html5Url
import com.jiechengsheng.city.utils.*
import com.jiechengsheng.city.view.XBanner.AbstractUrlLoader
import com.jiechengsheng.city.view.XBanner.XBanner
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
        arrayOf(
            StringUtils.getString(R.string.good),
            StringUtils.getString(R.string.comment)
        )

    var isBuyNow = false

    var goodSelect: StoreGoods? = null

    var now_price: BigDecimal = BigDecimal.ZERO

    var good_id = 0
    var good_image = ""
    var good_name = ""

    /**
     * 收藏状态（1：未收藏，2：已收藏）
     */
    private var collect_status = 0


    companion object {

        fun navigation(context: Context, id: Int) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, id)
            }
            val intent = Intent(context, StoreDetailActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


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
            alwaysEnableCut = true
            MIN_GOOD_NUM = 1
            cut_iv.setImageResource(R.mipmap.ic_cut_blue)
            add_iv.setImageResource(R.mipmap.ic_add_blue)
            updateNumberJudgeMin(1)
            cut_iv.visibility = View.VISIBLE
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
                number_view.updateNumberJudgeMin(1)
            }


        }


        like_iv.setOnClickListener {
            if (collect_status == 1) {
                presenter.collection(shop_id)
            } else {
                presenter.unCollection(shop_id)
            }
        }

        share_iv.setOnClickListener {
            val url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_E_STORE, shop_id)
            MobUtil.shareFacebookWebPage(url, this@StoreDetailActivity)
        }

    }

    override fun bindDetail(data: StoreDetail) {

        top_banner.setImageUrls(data.images).start()
        title_tv.text = data.title
        shop_name = data.title

        nav_ll.setOnClickListener {
            if (data.lat != 0f && data.lng != 0f) {
                FeaturesUtil.startNaviGoogle(this, data.lat, data.lng)
            }
        }

        if (data.im_status == 1 && !TextUtils.isEmpty(data.mer_uuid)) {
            phone_value_iv.setImageResource(R.mipmap.ic_phone_bold)
            phone_ll.setOnClickListener {
                BusinessUtils.startRongCloudConversationActivity(this, data.mer_uuid, data.mer_name ,data.tel )
            }
        } else {
            phone_value_iv.setImageResource(R.mipmap.ic_phone_bold)
            phone_ll.setOnClickListener {
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
        time_value_tv.text = getString(
            R.string.mechanism_to_format,
            data.start_time,
            data.end_time,
            stringArray[data.week_start - 1],
            stringArray[data.week_end - 1]
        )
        phone_value_tv.text = data.tel
        web_value_tv.text = data.web_site
        email_value_tv.text = data.email
        facebook_value_tv.text = data.facebook
        address_name_tv.text = data.address
        delivery_fee = data.delivery_fee
        EventBus.getDefault().post(BaseEvent<StoreShopRequest>(StoreShopRequest(shop_name, delivery_fee)))

        delivery_times = data.delivery_times
        take_times = data.take_times

        collect_status = data.collect_status
        setLikeImage()

    }


    private fun setLikeImage() {

        like_iv.setImageResource(
            if (collect_status == 1) {
                R.mipmap.ic_like_red_night
            } else {
                R.mipmap.ic_like_normal_night
            }
        )
    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {


        override fun getPageTitle(position: Int): CharSequence? = TAB_TITLES[position]


        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                StoreGoodFragment.newInstance(shop_id)
            } else {
                StoreCommentChildFragment.newInstance(shop_id, false)
            }
        }

        override fun getCount(): Int = TAB_TITLES.size
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        val data = baseEvent.data
        if (data is StoreGoods) {


            number_view.apply {
                updateNumber(1)
                MAX_GOOD_NUM = data.inventory
            }

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

    override fun changeCollection(isCollection: Boolean) {

        collect_status = if (isCollection) {
            2
        } else {
            1
        }
        setLikeImage()
        EventBus.getDefault().post(BaseEvent<String>(EventCode.EVENT_REFRESH_COLLECTION))
    }

}