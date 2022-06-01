package com.jcs.where.features.mall.shop.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.request.MallShop
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.shop.home.category.MallShopCategoryFragment
import com.jcs.where.features.mall.shop.home.good.MallShopGoodFragment
import com.jcs.where.features.mall.shop.home.info.MallShopInfoActivity
import com.jcs.where.features.mall.shop.home.recommend.MallShopRecommendFragment
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.frames.common.Html5Url
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.MobUtil

import kotlinx.android.synthetic.main.activity_mall_shop_home.*


/**
 * Created by Wangsw  2022/1/19 17:21.
 * 店铺首页
 */
class MallShopHomeActivity : BaseMvpActivity<MallShopHomePresenter>(), MallShopHomeView {

    private lateinit var mData: MallShop
    private var shopId = 0


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_shop_home

    val tabTitle = arrayOf(
        StringUtils.getString(R.string.news_recommend),
        StringUtils.getString(R.string.all_products),
        StringUtils.getString(R.string.view_category))

    companion object {
        fun navigation(context: Context, shopId: Int) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_SHOP_ID, shopId)
            }
            val intent = Intent(context, MallShopHomeActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        shopId = intent.getIntExtra(Constant.PARAM_SHOP_ID, 0)

        viewpager.offscreenPageLimit = tabTitle.size
        viewpager.adapter = InnerPagerAdapter(supportFragmentManager, 0)
        tabs_type.setViewPager(viewpager)
    }

    override fun initData() {
        presenter = MallShopHomePresenter(this)
        presenter.getDetail(shopId)
    }

    override fun bindListener() {

        follow_bt.setOnClickListener {
            if (mData.collect_status == 0) {
                presenter.collection(shopId)
            } else {
                presenter.unCollection(shopId)
            }
        }

        share_iv.setOnClickListener {
            val url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_MALL_SHOP, shopId)
            MobUtil.shareFacebookWebPage(url, this@MallShopHomeActivity)
        }

        search_ll.setOnClickListener {
            startActivity(SearchAllActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 8)
                putInt(Constant.PARAM_SHOP_ID, shopId)
            })
        }

        shop_name_tv.setOnClickListener {
            MallShopInfoActivity.navigation(this, mData)
        }

    }

    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence? = tabTitle[position]

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    MallShopRecommendFragment.newInstance(shopId)
                }
                1 -> {
                    MallShopGoodFragment.newInstance(shopId, hideListShopName = true)
                }
                else -> {
                    MallShopCategoryFragment.newInstance(shopId)
                }
            }

        }

        override fun getCount(): Int = tabTitle.size
    }


    override fun bindDetail(response: MallShop) {
        this.mData = response
        GlideUtil.load(this, response.image, shop_bg_iv)
        GlideUtil.load(this, response.logo, shop_logo_iv, 4)
        shop_name_tv.text = response.title
        setLikeImage()
    }

    override fun collectionHandleSuccess(collectionStatus: Boolean) {
        if (collectionStatus) {
            mData.collect_status = 1
            ToastUtils.showShort(R.string.collection_success)
        } else {
            mData.collect_status = 0
            ToastUtils.showShort(R.string.cancel_collection_success)
        }

        setLikeImage()
    }

    private fun setLikeImage() {

        if (mData.collect_status == 0) {
            follow_bt.setBackgroundResource(R.drawable.shape_blue_radius_16)
            follow_bt.text = getString(R.string.shop_follow)
        } else {
            follow_bt.setBackgroundResource(R.drawable.stock_white_radius_16)
            follow_bt.text = getString(R.string.shop_following)
        }

    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_FOLLOW -> {
                mData.collect_status = baseEvent.data as Int
                setLikeImage()
            }
            else -> {}
        }


    }

}