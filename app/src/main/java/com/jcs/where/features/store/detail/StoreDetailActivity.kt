package com.jcs.where.features.store.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.response.store.StoreDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil
import com.jcs.where.view.XBanner.AbstractUrlLoader
import com.jcs.where.view.XBanner.XBanner
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_store_detail.*
import pl.droidsonroids.gif.GifImageView

/**
 * Created by Wangsw  2021/6/15 10:33.
 * 商城店铺详情
 */
class StoreDetailActivity : BaseMvpActivity<StoreDetailPresenter>(), StoreDetailView {

    var id: Int = 0

    override fun getLayoutId() = R.layout.activity_store_detail

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
        id = bundle.getInt(Constant.PARAM_ID, 0)

        presenter = StoreDetailPresenter(this)
        presenter.getDetail(id)
    }

    override fun bindListener() {
        fold_ll.setOnClickListener {
            if (hide_ll.visibility != View.VISIBLE) {
                hide_ll.visibility = View.VISIBLE
            } else {
                hide_ll.visibility = View.GONE
            }
        }
    }

    override fun bindDetail(data: StoreDetail) {

        top_banner.setImageUrls(data.images).start()
        title_tv.text = data.title

        nav_tv.setOnClickListener {
            if (data.lat != 0f && data.lng != 0f) {
                FeaturesUtil.startNaviGoogle(this, data.lat, data.lng)
            }
        }

        if (data.im_status == 1) {
            phone_tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.ic_store_service, 0, 0)
            phone_tv.setOnClickListener {
                if (!TextUtils.isEmpty(data.mer_uuid)) {
                    RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, data.mer_uuid, data.mer_name, null)
                }
            }
        } else {
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
    }
}