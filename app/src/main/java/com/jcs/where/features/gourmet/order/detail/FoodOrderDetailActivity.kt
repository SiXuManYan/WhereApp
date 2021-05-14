package com.jcs.where.features.gourmet.order.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.BarUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.pay.PayActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_order_detail_food.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/5/10 9:47.
 *  美食订单详情
 */
class FoodOrderDetailActivity : BaseMvpActivity<FoodOrderDetailPresenter>(), FoodOrderDetailView {

    private var orderId = "";
    private var merUuid = "";
    private var restaurantName = "";
    private var totalPrice = "";

    override fun getLayoutId() = R.layout.activity_order_detail_food

    override fun isStatusDark() = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        orderId = bundle.getString(Constant.PARAM_ORDER_ID, "")
    }

    override fun initData() {
        presenter = FoodOrderDetailPresenter(this);
        presenter.getDetail(orderId)
    }

    override fun bindListener() {

        back_iv.setOnClickListener {
            finish()
        }

        pay_tv.setOnClickListener {
            startActivity(PayActivity::class.java, Bundle().apply {
                putString(Constant.PARAM_TOTAL_PRICE, totalPrice)
            })
        }

        cancel_tv.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle(R.string.prompt)
                    .setMessage(R.string.cancel_order_confirm)
                    .setPositiveButton(R.string.ensure) { dialogInterface, i ->
                        presenter.cancelOrder(orderId)
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.dismiss() }
                    .create().show()
        }

        chat_iv.setOnClickListener {
            if (merUuid.isBlank()) {
                return@setOnClickListener
            }
            RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, merUuid, restaurantName, null)
        }

    }

    override fun bindDetail(it: FoodOrderDetail) {

        val goodData = it.good_data
        val restaurantData = it.restaurant_date
        val orderData = it.order_data

        merUuid = restaurantData.mer_uuid
        restaurantName = restaurantData.name
        totalPrice = goodData.price.toPlainString()

        GlideUtil.load(this, goodData.good_image, image_iv)
        restaurant_name_tv.text = restaurantData.name
        good_name_tv.text = goodData.name
        FeaturesUtil.bindFoodOrderStatus(orderData.status, state_tv)

        count_tv.text = getString(R.string.quantity_format, goodData.good_num)
        total_price_tv.text = getString(R.string.price_unit_format, goodData.price.toPlainString())
        chat_iv.visibility = if (restaurantData.im_status == 1) {
            View.VISIBLE
        } else {
            View.GONE
        }


        // 券码
        if (orderData.status == 3) {
            coupon_ll.visibility = View.VISIBLE
            coupon_expire_tv.text = getString(R.string.coupon_expire_format, orderData.coupon_expire)
            coupon_no_tv.text = orderData.coupon_no
            GlideUtil.load(this, orderData.coupon_qr_code, coupon_qr_code_iv)
            coupon_ll.visibility = View.VISIBLE
        } else {
            coupon_ll.visibility = View.GONE
        }


        // 订单信息
        trade_no_tv.text = getString(R.string.trade_no_format, orderData.trade_no)
        phone_tv.text = getString(R.string.phone_format, orderData.phone)
        valid_period_tv.text = getString(R.string.valid_period_format, orderData.start_date, orderData.end_date)
        created_at_tv.text = getString(R.string.pay_time_format, orderData.created_at)

        // 底部
        if (orderData.status == 1) {
            bottom_ll.visibility = View.VISIBLE
            // 倒计时
            presenter.countdown(countdown_tv, "")
        } else {
            bottom_ll.visibility = View.GONE
        }
    }

    override fun cancelSuccess() {
        EventBus.getDefault().post(EventCode.EVENT_REFRESH_ORDER_LIST)
    }

    override fun countdownEnd() {
        pay_tv.setBackgroundResource(R.drawable.shape_gradient_orange_un_enable)
    }


}