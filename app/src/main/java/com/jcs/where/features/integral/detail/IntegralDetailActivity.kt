package com.jcs.where.features.integral.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.integral.IntegralGoodDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.integral.place.IntegralOrderActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_integral_detail.*

/**
 * Created by Wangsw  2022/9/22 10:23.
 * 兑换商品详情
 */
class IntegralDetailActivity : BaseMvpActivity<IntegralDetailPresenter>(), IntegralDetailView {

    var goodId = 0
    var goodTitle = ""


    companion object {
        fun navigation(context: Context, goodId: Int) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, goodId)
            }
            val intent = Intent(context, IntegralDetailActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_integral_detail

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        goodId = intent.getIntExtra(Constant.PARAM_ID, 0)

    }

    override fun initData() {
        presenter = IntegralDetailPresenter(this)
        presenter.getData(goodId)

    }

    override fun bindListener() {

    }


    override fun bindDetail(response: IntegralGoodDetail) {
        val image = response.image
        GlideUtil.load(this, image, good_iv)

        /** 1：商品 其他：优惠券 */
        val couponType = response.type
        if (couponType == 1) {
            mJcsTitle.setMiddleTitle(getString(R.string.exchange_product_details))
            desc_title_tv.setText(R.string.product_description)
        } else {
            mJcsTitle.setMiddleTitle(getString(R.string.coupon_details))
            desc_title_tv.setText(R.string.use_rule)
        }

        val status = response.operation_status

        tag_tv.visibility = if (status == 3) {
            View.VISIBLE
        } else {
            View.GONE
        }

        val goodTitle = response.title
        good_name_tv.text = goodTitle
        (response.start_time + "-" + response.end_time).also { time_tv.text = it }
        val price = response.price
        point_tv.text = getString(R.string.points_format, price)
        balance_tv.text = getString(R.string.points_format, response.user_integral)

        desc_tv.text = response.desc

        /** 0积分不足 1可以兑换 2超出限制 3即将上线 */
        when (status) {
            0 -> {
                confirm_tv.alpha = 0.5f
                confirm_tv.isClickable = false
                confirm_tv.text = getString(R.string.insufficient_points)
            }
            1 -> {
                confirm_tv.alpha = 1.0f
                confirm_tv.isClickable = true
                confirm_tv.text = getString(R.string.exchange)
                confirm_tv.setOnClickListener {
                    IntegralOrderActivity.navigation(this, goodId, goodTitle, image, price,couponType)
                }
            }
            2 -> {
                confirm_tv.alpha = 1.0f
                confirm_tv.isClickable = true
                confirm_tv.text = getString(R.string.exchange)
                confirm_tv.setOnClickListener {
                    ToastUtils.showShort(R.string.quantity_over)
                }
            }
            3 -> {
                confirm_tv.alpha = 0.5f
                confirm_tv.isClickable = false
                confirm_tv.text = getString(R.string.exchange)
            }


            else -> {}
        }

    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        if (baseEvent.code == EventCode.EVENT_REFRESH_INTEGRAL) {
            finish()
        }
    }

}