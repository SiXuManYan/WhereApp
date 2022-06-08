package com.jcs.where.features.mall.shop.home.info

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.request.MallShop
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.shop.home.CollectionHandle
import com.jcs.where.features.mall.shop.home.MallShopHomePresenter
import com.jcs.where.features.mall.shop.home.MallShopHomeView
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_mall_shop_info.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/5/31 15:12.
 * 店铺信息
 */
class MallShopInfoActivity : BaseMvpActivity<MallShopHomePresenter>(), MallShopHomeView {


    /** 收藏状态（0：未收藏，1：已收藏） */
    private var collectStatus = 0

    private var collectCount = 0

    private lateinit var data: MallShop

    companion object {

        fun navigation(context: Context, data: MallShop) {
            val bundle = Bundle().apply {
                putParcelable(Constant.PARAM_DATA, data)
            }
            val intent = Intent(context, MallShopInfoActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_shop_info

    override fun initView() {
        intent.extras?.let {
            data = it.getParcelable<MallShop>(Constant.PARAM_DATA)!!
            collectCount = data.collect_count
        }
        GlideUtil.load(this, data.logo, shop_logo_iv)
        shop_name_tv.text = data.title
        updateFanCount(collectCount)


        collectStatus = data.collect_status
        setLikeImage()
        service_name_tv.text = data.nickname
        address_tv.text = data.address
        if (TextUtils.isEmpty(data.qualification)) {
            qualification_rl.visibility = View.GONE
        } else {
            qualification_rl.visibility = View.VISIBLE
        }


    }

    private fun updateFanCount(count: Int) {
        fans_number_tv.text = getString(R.string.fans_format, count)
    }

    override fun initData() {
        presenter = MallShopHomePresenter(this)
    }

    override fun bindListener() {
        follow_bt.setOnClickListener {
            if (collectStatus == 0) {
                presenter.collection(data.id, follow_bt)
            } else {
                presenter.unCollection(data.id, follow_bt)
            }
        }

        qualification_rl.setOnClickListener {
            QualificationActivity.navigation(this, data.qualification)
        }
    }

    override fun collectionHandleSuccess(collectionStatus: Boolean) {
        if (collectionStatus) {
            collectStatus = 1
            ToastUtils.showShort(R.string.collection_success)
            collectCount++
            updateFanCount(collectCount)
        } else {
            collectStatus = 0
            ToastUtils.showShort(R.string.cancel_collection_success)
            collectCount--
            if (collectCount < 0) collectCount = 0
            updateFanCount(collectCount)
        }

        val apply = CollectionHandle().apply {
            fansCount = collectCount
            status = collectStatus
        }
        EventBus.getDefault().post(BaseEvent(EventCode.EVENT_REFRESH_FOLLOW, apply))
        setLikeImage()
    }

    private fun setLikeImage() {

        if (collectStatus == 0) {
            follow_bt.setBackgroundResource(R.drawable.shape_blue_radius_16)
            follow_bt.text = getString(R.string.shop_follow)
            follow_bt.setTextColor(ColorUtils.getColor(R.color.white))
        } else {
            follow_bt.setBackgroundResource(R.drawable.stock_blue_radius_16)
            follow_bt.text = getString(R.string.shop_following)
            follow_bt.setTextColor(ColorUtils.getColor(R.color.blue_377BFF))
        }

    }

}