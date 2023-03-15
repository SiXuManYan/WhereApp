package com.jiechengsheng.city.features.gourmet.takeaway.order2

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.hotel.ComplaintRequest
import com.jiechengsheng.city.api.request.payment.PayUrlGet
import com.jiechengsheng.city.api.response.gourmet.order.TakeawayOrderDetail
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.com100.ExtendChatActivity
import com.jiechengsheng.city.features.gourmet.comment.post.FoodCommentPostActivity
import com.jiechengsheng.city.features.gourmet.refund.ComplexRefundActivity
import com.jiechengsheng.city.features.gourmet.refund.ComplexRefundPresenter
import com.jiechengsheng.city.features.gourmet.refund.detail.FoodRefundInfoActivity
import com.jiechengsheng.city.features.gourmet.refund.detail.FoodRefundInfoPresenter
import com.jiechengsheng.city.features.gourmet.takeaway.order.TakeawayGoodDataAdapter
import com.jiechengsheng.city.features.gourmet.takeaway.order.TakeawayOrderDetailPresenter
import com.jiechengsheng.city.features.gourmet.takeaway.order.TakeawayOrderDetailView
import com.jiechengsheng.city.features.mall.refund.complaint.ComplaintActivity
import com.jiechengsheng.city.features.payment.counter.PayCounterActivity
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_takeaway_order_detail_2.*
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal


/**
 * Created by Wangsw  2021/7/28 16:12.
 * 美食外卖模块订单详情
 */
class TakeawayOrderDetailActivity : BaseMvpActivity<TakeawayOrderDetailPresenter>(), TakeawayOrderDetailView {

    private var orderId = 0;
    private var contactNumber = "";
    private var merUuid = "";
    private var restaurantName = "";
    private lateinit var mAdapter: TakeawayGoodDataAdapter
    private var tel = ""
    private var alreadyComplaint = false

    override fun getLayoutId() = R.layout.activity_takeaway_order_detail_2

    /** 处理申诉 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            alreadyComplaint = true
            ToastUtils.showShort(R.string.complained_success)
        }
    }

    override fun isStatusDark() = true

    override fun initView() {

        BarUtils.setStatusBarColor(this, Color.WHITE)
        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        orderId = bundle.getInt(Constant.PARAM_ORDER_ID, 0)

        mAdapter = TakeawayGoodDataAdapter()
        good_rv.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.white), SizeUtils.dp2px(10f), 0, 0).apply {
                setDrawHeaderFooter(false)
            })
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically(): Boolean = false
            }
            isNestedScrollingEnabled = true
        }


    }

    override fun initData() {
        presenter = TakeawayOrderDetailPresenter(this)
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
                    .putExtra(Constant.PARAM_TYPE, ComplaintRequest.TYPE_FOOD_TAKEAWAY)
                searchLauncher.launch(intent)
            }
        }

    }

    override fun bindDetail(it: TakeawayOrderDetail) {
        val goodData = it.good_data
        val restaurantData = it.restaurant_data
        val orderData = it.order_data
        val paymentChannel = it.payment_data
        merUuid = restaurantData.mer_uuid
        restaurantName = restaurantData.name
        tel = restaurantData.tel

        alreadyComplaint = orderData.complaint_status == 1

        /** 1-待支付，2-支付审核中，3-交易取消，4-交易关闭，5-待接单，6-已接单，7-待收货，8-交易成功，9-退款中，10-退款成功 11-商家审核中 12-拒绝售后 13-退款失败 */
        val status = orderData.status
        status_tv.text = BusinessUtils.getTakeawayStatusText(status)
        status_desc_tv.text = BusinessUtils.getTakeawayOrderStatusDesc(status)

        val orderPrice = orderData.price
        price_tv.text = getString(R.string.price_unit_format, orderPrice.toPlainString())
        order_number_tv.text = orderData.trade_no
        created_date_tv.text = orderData.created_at

        delivery_time_tv.text = if (orderData.delivery_time_type == 1) {
            getString(R.string.delivery_now)
        } else {
            orderData.delivery_time
        }

        val address = orderData.address
        contactNumber = address.contact_number
        val contactName = address.contact_name
        address_name_tv.text = address.address
        contact_name_tv.text = getString(R.string.address_name_format, contactName, contactNumber)


        if (status != 1 && status != 3) {
            payment_container_ll.visibility = View.VISIBLE
            pay_way_tv.text = paymentChannel.payment_channel
        } else {
            payment_container_ll.visibility = View.GONE

        }

        business_name_tv.text = restaurantName
        packaging_fee_tv.text = getString(R.string.price_unit_format, orderData.packing_charges.toPlainString())
        delivery_fee_tv.text = getString(R.string.price_unit_format, orderData.delivery_cost.toPlainString())
        remarks_tv.text = orderData.remark

        mAdapter.setNewInstance(goodData)


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
                    orderIds.add(orderId)
                    /*      startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
                              putDouble(Constant.PARAM_TOTAL_PRICE, price.toDouble())
                              putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                              putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_TAKEAWAY)
                          })*/

//                    WebPayActivity.navigation(this, Constant.PAY_INFO_TAKEAWAY, orderIds)
                    PayCounterActivity.navigation(this, PayUrlGet.TAKEAWAY, orderIds, orderPrice.toPlainString())

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
                    doRefund(orderPrice)
                }
            }
            8 -> {
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
                            putInt(Constant.PARAM_TYPE, 2)
                        })
                    }
                } else {
                    bottom_container_rl.visibility = View.GONE
                }
            }
            9, 11, 12 -> {
                bottom_container_rl.visibility = View.VISIBLE
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
            13 -> {
                bottom_container_rl.visibility = View.VISIBLE
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
                        doRefund(orderPrice)
                    }
                }
            }
            else -> {
                bottom_container_rl.visibility = View.GONE
            }
        }
        bottom_v.visibility = bottom_container_rl.visibility


        // 投诉
        if (status == 12) {
            service_iv.visibility = View.GONE
            complaint_tv.visibility = View.VISIBLE
        } else {
            service_iv.visibility = View.VISIBLE
            complaint_tv.visibility = View.GONE
        }

        // 退款失败
        if (status == 13) {
            fail_reason_rl.visibility = View.VISIBLE
            reason_split_v.visibility = View.VISIBLE
            fail_reason_tv.text = orderData.error_reason
        } else {
            fail_reason_rl.visibility = View.GONE
            reason_split_v.visibility = View.GONE
        }

    }

    override fun cancelSuccess() {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
        presenter.getDetail(orderId)
    }


    private fun doRefund(price: BigDecimal) {
        ComplexRefundActivity.navigation(this,
            orderId,
            price.toPlainString(),
            price.toPlainString(),
            ComplexRefundPresenter.TYPE_TAKEAWAY)
    }

    private fun viewRefundInfo() = FoodRefundInfoActivity.navigation(this, orderId, FoodRefundInfoPresenter.TYPE_TAKEAWAY)


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