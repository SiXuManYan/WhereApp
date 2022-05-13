package com.jcs.where.features.gourmet.order.detail2

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.request.hotel.ComplaintRequest
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.com100.ExtendChatActivity
import com.jcs.where.features.gourmet.comment.post.FoodCommentPostActivity
import com.jcs.where.features.gourmet.refund.ComplexRefundActivity
import com.jcs.where.features.gourmet.refund.detail.FoodRefundInfoActivity
import com.jcs.where.features.mall.refund.complaint.ComplaintActivity
import com.jcs.where.features.payment.WebPayActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_delicacy_order_detail.*
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/7/23 15:59.
 * 美食订单详情
 */
class DelicacyOrderDetailActivity : BaseMvpActivity<DelicacyOrderDetailPresenter>(), DelicacyOrderDetailView {

    private var orderId = 0
    private var merUuid = ""
    private var restaurantName = ""
    private var tel = ""
    private var alreadyComplaint = false

    override fun getLayoutId() = R.layout.activity_delicacy_order_detail


    /** 处理申诉 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            alreadyComplaint = true
            ToastUtils.showShort(R.string.complained_success)
        }
    }

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
            BusinessUtils.startRongCloudConversationActivity(this, merUuid, restaurantName)
        }
        complaint_tv.setOnClickListener {
            // 投诉
            if (alreadyComplaint) {
                ToastUtils.showShort(R.string.complained_success)
            } else {
                val intent = Intent(this, ComplaintActivity::class.java)
                    .putExtra(Constant.PARAM_ORDER_ID, orderId)
                    .putExtra(Constant.PARAM_TYPE, ComplaintRequest.TYPE_FOOD)
                searchLauncher.launch(intent)
            }
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

        /**
         * 订单状态（1-待支付，2-支付审核中，3-交易取消，4-交易关闭，5-待使用，6-交易成功，7-退款中，8-退款成功 9-商家审核中 10-拒绝售后 11-退款失败）
         */
        val status = orderData.status
        status_tv.text = BusinessUtils.getDelicacyOrderStatusText(status)
        val price = goodData.price

        price_tv.text = getString(R.string.price_unit_format, price.toPlainString())
        order_number_tv.text = orderData.trade_no
        created_date_tv.text = orderData.created_at
        phone_tv.text = orderData.phone
        valid_period_tv.text = getString(R.string.valid_period_format2, orderData.start_date, orderData.end_date)
        valid_period_rl.visibility = if (status == 3) {
            View.VISIBLE
        } else {
            View.GONE
        }

        if (status != 1 && status != 3) {
            pay_container_ll.visibility = View.VISIBLE
            pay_way_tv.text = paymentChannel.payment_channel
        } else {
            pay_container_ll.visibility = View.GONE
        }


        business_name_tv.text = restaurantData.name

        GlideUtil.load(this, goodData.good_image, image_iv, 4)
        good_name_tv.text = goodData.name
        good_count_tv.text = getString(R.string.quantity_format, goodData.good_num)
        good_price_tv.text = getString(R.string.price_unit_format, price.toPlainString())

        order_code_information.text = orderData.coupon_no
        if (status == 10) {
            service_iv.visibility = View.GONE
            complaint_tv.visibility = View.VISIBLE
        } else {
            service_iv.visibility = View.VISIBLE
            complaint_tv.visibility = View.GONE
        }
        // 退款失败
        if (status == 11) {
            fail_reason_rl.visibility = View.VISIBLE
            reason_split_v.visibility = View.VISIBLE
            fail_reason_tv.text = orderData.error_reason
        } else {
            fail_reason_rl.visibility = View.GONE
            reason_split_v.visibility = View.GONE
        }

        // 处理状态描述文案
        status_desc_tv.text = BusinessUtils.getDelicacyOrderStatusDesc(status)


        // 处理底部
        when (status) {
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
/*
                    startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
                        putDouble(Constant.PARAM_TOTAL_PRICE, price.toDouble())
                        putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                        putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_FOOD)
                    })
*/
                    WebPayActivity.navigation(this, Constant.PAY_INFO_FOOD, orderIds)

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
                    doRefund(price)
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
                        startActivity(FoodCommentPostActivity::class.java, Bundle().apply {
                            putInt(Constant.PARAM_ORDER_ID, orderId)
                            putInt(Constant.PARAM_RESTAURANT_ID, restaurantData.id)
                            putInt(Constant.PARAM_TYPE, 1)
                        })
                    }
                } else {
                    bottom_container_rl.visibility = View.GONE
                }

            }
            7, 8, 9, 10 -> {
                left_tv.apply {
                    visibility = View.VISIBLE
                    text = StringUtils.getString(R.string.refund_information)
                    setOnClickListener {
                        // 查看退款信息
                        viewRefundInfo()
                    }
                }
                right_tv.visibility = View.GONE
            }
            11 -> {
                left_tv.apply {
                    visibility = View.VISIBLE
                    text = StringUtils.getString(R.string.refund_information)
                    setOnClickListener {
                        // 查看退款信息
                        viewRefundInfo()
                    }
                }
                right_tv.apply {
                    visibility = View.VISIBLE
                    text = StringUtils.getString(R.string.apply_again)
                    setOnClickListener {
                        // 再次申请
                        doRefund(price)
                    }
                }
            }
            else -> {
                bottom_container_rl.visibility = View.GONE
            }
        }
        bottom_v.visibility = bottom_container_rl.visibility


    }

    private fun doRefund(price: BigDecimal) {
        ComplexRefundActivity.navigation(this, orderId, price.toPlainString(), price.toPlainString())
    }

    private fun viewRefundInfo() = FoodRefundInfoActivity.navigation(this, orderId, FoodRefundInfoActivity.TYPE_FOOD)

    override fun cancelSuccess() {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
        presenter.getDetail(orderId)

    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        when (baseEvent.code) {
            EventCode.EVENT_CANCEL_PAY -> finish()
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                // 退款成功
                presenter.getDetail(orderId)
            }
            else -> {}
        }


    }

}