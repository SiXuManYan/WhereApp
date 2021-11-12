package com.jcs.where.features.gourmet.order.detail2

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.customer.ExtendChatActivity
import com.jcs.where.features.gourmet.comment.post.FoodCommentPostActivity
import com.jcs.where.features.store.pay.StorePayActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_delicacy_order_detail.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/7/23 15:59.
 * 美食订单详情
 */
class DelicacyOrderDetailActivity : BaseMvpActivity<DelicacyOrderDetailPresenter>(), DelicacyOrderDetailView {

    private var orderId =  0
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
        orderId = bundle.getInt(Constant.PARAM_ORDER_ID, 0)
    }

    override fun isStatusDark() = true

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
        val restaurantData = it.restaurant_data
        val orderData = it.order_data
        val paymentChannel = it.payment_data

        merUuid = restaurantData.mer_uuid
        restaurantName = restaurantData.name
        tel = restaurantData.tel

        status_tv.text = BusinessUtils.getDelicacyOrderStatusText(orderData.status)
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

        if (orderData.status != 1 && orderData.status != 3) {
            pay_container_ll.visibility = View.VISIBLE
            pay_way_tv.text = paymentChannel.payment_channel
            payment_name_tv.text = getString(R.string.payment_name_format, paymentChannel.bank_card_account)
            payment_account_tv.text = getString(R.string.payment_account_format, paymentChannel.bank_card_number)
        } else {
            pay_container_ll.visibility = View.GONE
        }


        business_name_tv.text = restaurantData.name

        GlideUtil.load(this, goodData.good_image, image_iv, 4)
        good_name_tv.text = goodData.name
        good_count_tv.text = getString(R.string.quantity_format, goodData.good_num)
        good_price_tv.text = getString(R.string.price_unit_format, price.toPlainString())


        // 处理底部
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

                    val orderIds = ArrayList<Int>()
                    orderIds.add(orderData.id)
                    startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
                        putDouble(Constant.PARAM_TOTAL_PRICE, price.toDouble())
                        putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                        putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_FOOD)
                    })


                }
            }
            5 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.apply {
                    text = getString(R.string.to_refund)
                    visibility = View.VISIBLE
                }
                right_tv.apply {
                    visibility = View.GONE
                }
                left_tv.setOnClickListener {
                    AlertDialog.Builder(this)
                            .setTitle(R.string.prompt)
                            .setMessage(R.string.delicacy_return_hint)
                            .setPositiveButton(R.string.ensure) { dialogInterface, i ->
                                presenter.refundOrder(orderId)
                                dialogInterface.dismiss()
                            }
                            .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.dismiss() }
                            .create().show()
                }
            }
            6 -> {

                if (orderData.comment_status == 1) {
                    bottom_container_rl.visibility = View.VISIBLE
                    left_tv.apply {

                        visibility = View.GONE
                    }
                    right_tv.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.evaluation)
                    }
                    right_tv.setOnClickListener {
                        startActivity(FoodCommentPostActivity::class.java,Bundle().apply {
                            putInt(Constant.PARAM_ORDER_ID , orderId)
                            putInt(Constant.PARAM_RESTAURANT_ID , restaurantData.id)
                            putInt(Constant.PARAM_TYPE , 1)
                        })
                    }
                } else {
                    bottom_container_rl.visibility = View.GONE
                }

            }
            else -> {
                bottom_container_rl.visibility = View.GONE
            }
        }
        bottom_v.visibility = bottom_container_rl.visibility

        // 处理状态描述文案
        when (orderData.status) {
            4 -> {
                status_desc_tv.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.store_status_desc_7)
                }
            }
            7 -> {
                status_desc_tv.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.store_status_desc_8)
                }
            }
            8 -> {
                status_desc_tv.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.store_status_desc_9)
                }
            }
            else -> {
                status_desc_tv.visibility = View.GONE
            }
        }


    }

    override fun cancelSuccess() {
        EventBus.getDefault().post(EventCode.EVENT_REFRESH_ORDER_LIST)
        presenter.getDetail(orderId)

    }

    override fun refundSuccess() {
        EventBus.getDefault().post(EventCode.EVENT_REFRESH_ORDER_LIST)
        ToastUtils.showShort(getString(R.string.refund_commit_toast))
        presenter.getDetail(orderId)
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        if (baseEvent.code == EventCode.EVENT_CANCEL_PAY) {
            finish()
        }
    }

}