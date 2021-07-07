package com.jcs.where.features.store.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.request.StoreShopRequest
import com.jcs.where.api.response.store.StoreDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.gourmet.comment.FoodCommentFragment
import com.jcs.where.features.store.detail.good.StoreGoodFragment
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_store_detail.*
import org.greenrobot.eventbus.EventBus
import pl.droidsonroids.gif.GifImageView

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


    val TAB_TITLES =
            arrayOf(StringUtils.getString(R.string.good),
                    StringUtils.getString(R.string.comment))


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
    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {


        override fun getPageTitle(position: Int): CharSequence? = TAB_TITLES[position]


        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                StoreGoodFragment.newInstance(shop_id)
            } else {
                FoodCommentFragment.newInstance(shop_id.toString())
            }
        }

        override fun getCount(): Int = TAB_TITLES.size
    }
}