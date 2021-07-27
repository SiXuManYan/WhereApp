package com.jcs.where.features.gourmet.order.detail2

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.BarUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.customer.ExtendChatActivity
import com.jcs.where.features.pay.PayActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_delicacy_order_detail.*

/**
 * Created by Wangsw  2021/7/23 15:59.
 * 美食订单详情
 */
class DelicacyOrderDetailActivity : BaseMvpActivity<DelicacyOrderDetailPresenter>(), DelicacyOrderDetailView {

    private var orderId = ""
    private var merUuid = ""
    private var restaurantName = ""
    private var tel = ""

    override fun getLayoutId() = R.layout.activity_delicacy_order_detail

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
        presenter = DelicacyOrderDetailPresenter(this)
        presenter.getDetail(orderId)
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }
        service_iv.setOnClickListener {
            startActivity(ExtendChatActivity::class.java)
        }

        phone_ll.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$tel")
            }
            startActivity(intent)
        }
        service_ll.setOnClickListener {
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
        val paymentChannel = it.payment_channel

        merUuid = restaurantData.mer_uuid
        restaurantName = restaurantData.name
        tel = restaurantData.tel

        FeaturesUtil.bindFoodOrderStatus(orderData.status, status_tv)
        val price = goodData.price

        price_tv.text = getString(R.string.price_unit_format, price.toPlainString())
        order_number_tv.text = orderData.trade_no
        created_date_tv.text = orderData.created_at
        phone_tv.text = orderData.phone
        valid_period_tv.text = getString(R.string.valid_period_format2, orderData.start_date, orderData.end_date)
        valid_period_rl.visibility = if (orderData.status == 3) {
            View.VISIBLE
        } else {
            View.GONE
        }
        pay_way_tv.text = paymentChannel.payment_channel
        payment_name_tv.text = paymentChannel.bank_card_account
        payment_account_tv.text = paymentChannel.bank_card_number
        business_name_tv.text = restaurantData.mer_name

        GlideUtil.load(this, goodData.good_image, image_iv, 4)
        good_name_tv.text = goodData.name
        good_count_tv.text = getString(R.string.quantity_format, goodData.good_num)
        good_price_tv.text = getString(R.string.price_unit_format, price.toPlainString())



        when (orderData.status) {
            1 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.apply {
                    text = getString(R.string.to_cancel_order)
                    visibility = View.VISIBLE
                }
                right_tv.apply {
                    text = getString(R.string.to_pay_2)
                    visibility = View.VISIBLE
                }

                left_tv.setOnClickListener {

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

                right_tv.setOnClickListener {
                    startActivity(PayActivity::class.java, Bundle().apply {
                        putString(Constant.PARAM_TOTAL_PRICE, price.toPlainString())
                    })
                }
            }
            3->{
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.apply {
                    text = getString(R.string.to_refund)
                    visibility = View.VISIBLE
                }
                right_tv.apply {
                    visibility = View.GONE
                }
                left_tv.setOnClickListener {
                    // 申请退款
                }
            }
            4 -> {
                bottom_container_rl.visibility = View.VISIBLE
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.apply {

                    visibility = View.GONE
                }
                right_tv.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.evaluation)
                }
                right_tv.setOnClickListener {
                    // 评价
                }
            }
            else -> {
                bottom_container_rl.visibility = View.GONE
            }
        }


    }

    override fun cancelSuccess() {

    }

}