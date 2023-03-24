package com.jiechengsheng.city.features.integral.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.integral.IntegralGoodDetail
import com.jiechengsheng.city.api.response.integral.IntegralPlaceOrderResponse
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.integral.place.IntegralOrderActivity
import com.jiechengsheng.city.features.integral.place.IntegralOrderPresenter
import com.jiechengsheng.city.features.integral.place.IntegralOrderView
import com.jiechengsheng.city.features.integral.result.ExchangeResultActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_integral_detail.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/9/22 10:23.
 * 兑换商品详情
 * 2023-2-10 , 普通优惠券可以直接在此页面兑换
 */
class IntegralDetailActivity : BaseMvpActivity<IntegralOrderPresenter>(), IntegralOrderView {

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
        presenter = IntegralOrderPresenter(this)
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
            1 -> {
                confirm_tv.alpha = 1.0f
                confirm_tv.isClickable = true
                confirm_tv.text = getString(R.string.exchange)
                confirm_tv.setOnClickListener {
                    if (couponType == 1) {
                        IntegralOrderActivity.navigation(this, goodId, goodTitle, image, price, couponType)
                    } else {
                        showDialog()
                    }

                }
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

    private fun showDialog() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.dialog_integral_place_order, null)

        val cancelTv = view.findViewById<TextView>(R.id.cancel_tv)
        val confirmTv = view.findViewById<TextView>(R.id.confirm_tv)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        val window: Window? = alertDialog.window
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setContentView(view)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // 更改默认宽度
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(window.attributes)
            lp.width = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(80f)
            window.attributes = lp
        }


        cancelTv.setOnClickListener {
            alertDialog.dismiss()
        }

        confirmTv.setOnClickListener {
            presenter.makeOrder(goodId)
            alertDialog.dismiss()
        }

    }

    override fun submitSuccess(response: IntegralPlaceOrderResponse) {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_INTEGRAL))
        startActivityAfterLogin(ExchangeResultActivity::class.java)
        finish()

    }


}