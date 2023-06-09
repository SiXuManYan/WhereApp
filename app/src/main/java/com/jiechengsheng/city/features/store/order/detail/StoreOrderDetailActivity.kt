package com.jiechengsheng.city.features.store.order.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.payment.PayUrlGet
import com.jiechengsheng.city.api.response.order.store.StoreOrderDetail
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.EventCode.EVENT_REFRESH_ORDER_LIST
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.com100.ExtendChatActivity
import com.jiechengsheng.city.features.payment.counter.PayCounterActivity
import com.jiechengsheng.city.features.store.comment.detail.StoreCommentDetailActivity
import com.jiechengsheng.city.features.store.comment.post.StoreCommentPostActivity
import com.jiechengsheng.city.features.store.refund.StoreRefundActivity
import com.jiechengsheng.city.features.store.refund.detail.StoreRefundDetailActivity
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_store_order_detail.*
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal


/**
 * Created by Wangsw  2021/6/26 11:15.
 *  商城订单详情
 */
class StoreOrderDetailActivity : BaseMvpActivity<StoreOrderDetailPresenter>(), StoreOrderDetailView {


    private var orderId = 0

    private var totalPrice = 0.0

    private lateinit var mAdapter: StoreOrderDetailAdapter

    private var tel = ""

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_store_order_detail

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
        }

        mAdapter = StoreOrderDetailAdapter()
        good_rv.apply {
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically() = false
            }
            adapter = mAdapter
            addItemDecoration(
                DividerDecoration(ColorUtils.getColor(R.color.white), SizeUtils.dp2px(10f), 0, 0)
            )
        }


    }

    override fun initData() {
        presenter = StoreOrderDetailPresenter(this)
        presenter.getOrderDetail(orderId)

    }

    override fun bindListener() {
        service_ll.setOnClickListener {
            startActivityAfterLogin(ExtendChatActivity::class.java)
        }
        phone_ll.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$tel")
            }
            startActivity(intent)
        }

    }


    override fun bindData(data: StoreOrderDetail) {
        tel = data.phone
        status_tv.text = presenter.getStatusText(data.delivery_type, data.status)
        presenter.getStatusDescText(status_desc_tv, data.status)
        order_number_tv.text = data.trade_no
        created_date_tv.text = data.created_at
        val price = data.price
        totalPrice = price.toDouble()
        price_tv.text = getString(R.string.price_unit_format, price.toPlainString())


        // 商家服务
        if (data.delivery_type == 1) {
            delivery_value_tv.text = getString(R.string.self_extraction)
            service_second_title_tv.text = getString(R.string.phone_number)
            service_second_value_tv.text = data.phone
            recipient_info_ll.visibility = View.GONE

            // 商品备注
            delivery_rl.visibility = View.GONE
            remark_rl.visibility = View.GONE
        } else {
            delivery_value_tv.text = getString(R.string.express)
            service_second_title_tv.text = getString(R.string.delivery_time)
            service_second_value_tv.text = data.delivery_times
            recipient_info_ll.visibility = View.VISIBLE
            data.address?.let {
                address_name_tv.text = it.address
                recipient_tv.text = getString(R.string.star_text_format, it.contact_name, it.contact_number)
            }

            // 商品备注
            delivery_rl.visibility = View.VISIBLE
            remark_rl.visibility = View.VISIBLE
            delivery_price_tv.text = getString(R.string.price_unit_format, data.delivery_fee.toPlainString())
            remarks_value_tv.text = data.remark

        }

        // 支付信息
        if (data.status != 1 && data.status != 6) {
            pay_way_tv.text = data.pay_channel
            payment_name_tv.text = getString(R.string.payment_name_format, data.bank_card_account)
            payment_account_tv.text = getString(R.string.payment_account_format, data.bank_card_number)
            pay_container_ll.visibility = View.VISIBLE
        } else {
            pay_container_ll.visibility = View.GONE
        }

        if (data.status == 12) {
            service_ll.visibility = View.INVISIBLE
        } else {
            service_ll.visibility = View.GONE
        }

        var shopName = ""
        var shopImage = ""

        // 商品信息
        data.shop?.let {
            mAdapter.setNewInstance(it.goods)
            shopName = it.title
            business_name_tv.text = it.title

            if (it.im_status == 1 && !TextUtils.isEmpty(it.mer_uuid)) {
                im_ll.visibility = View.VISIBLE
                im_ll.setOnClickListener { _ ->
                    BusinessUtils.startRongCloudConversationActivity(this, it.mer_uuid, it.mer_name)
                }
            } else {
                im_ll.visibility = View.GONE
            }

            if (it.images.isNotEmpty()) {
                shopImage = it.images[0]
            }

        }

        // 底部
        when (data.status) {
            1 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.visibility = View.VISIBLE
                left_tv.text = getString(R.string.to_cancel_order)
                left_tv.setOnClickListener {
                    // 取消订单
                    cancelOrder(data.id)
                }

                right_tv.visibility = View.VISIBLE
                right_tv.text = getString(R.string.to_pay_2)
                right_tv.setOnClickListener {
                    // 去付款
                    handlePay(data.id, data.price)
                }
            }
            3 -> {
                if (data.delivery_type == 2) {
                    bottom_container_rl.visibility = View.VISIBLE
                    left_tv.visibility = View.VISIBLE
                    left_tv.text = getString(R.string.to_refund)
                    left_tv.setOnClickListener {
                        // 申请退款
                        doRefund()
                    }
                    right_tv.visibility = View.GONE
                } else {
                    bottom_container_rl.visibility = View.GONE
                }
            }
            4 -> {
                if (data.delivery_type == 1) {
                    bottom_container_rl.visibility = View.VISIBLE
                    left_tv.visibility = View.VISIBLE
                    left_tv.text = getString(R.string.to_refund)
                    left_tv.setOnClickListener {
                        // 申请退款
                        doRefund()
                    }
                    right_tv.visibility = View.GONE
                } else {
                    bottom_container_rl.visibility = View.GONE
                }
            }
            5 -> {
                bottom_container_rl.visibility = View.VISIBLE

                left_tv.visibility = View.VISIBLE
                left_tv.text = getString(R.string.apply_return)
                left_tv.setOnClickListener {
                    // 申请退货
                    doRefund()
                }
                val commentStatus = data.comment_status
                if (commentStatus == 3) {
                    right_tv.visibility = View.GONE
                } else {

                    right_tv.visibility = View.VISIBLE

                    if (commentStatus == 1) {
                        right_tv.text = getString(R.string.evaluation)
                        right_tv.setOnClickListener {
                            // 去评价
                            startActivity(StoreCommentPostActivity::class.java, Bundle().apply {
                                putInt(Constant.PARAM_ORDER_ID, orderId)
                                putString(Constant.PARAM_SHOP_NAME, shopName)
                                putString(Constant.PARAM_SHOP_IMAGE, shopImage)
                            })
                        }
                    }

                    if (commentStatus == 2) {
                        right_tv.text = getString(R.string.view_evaluation)
                        right_tv.setOnClickListener {
                            startActivity(StoreCommentDetailActivity::class.java, Bundle().apply {
                                putInt(Constant.PARAM_ORDER_ID, orderId)
                            })
                        }
                    }
                }

            }
            8, 9, 10, 11, 12 -> {
                //  查看售后详情
                if (data.aftersale_status == 2) {
                    bottom_container_rl.visibility = View.VISIBLE
                    left_tv.visibility = View.VISIBLE
                    left_tv.text = getString(R.string.after_sale_details)
                    left_tv.setOnClickListener {
                        // 查看售后详情
                        viewRefundDetail()
                    }
                    right_tv.visibility = View.GONE
                } else {
                    bottom_container_rl.visibility = View.GONE
                }

            }
            else -> {
                bottom_container_rl.visibility = View.GONE
            }
        }
    }

    private fun cancelOrder(orderId: Int) {
        AlertDialog.Builder(this)
            .setTitle(R.string.prompt)
            .setMessage(R.string.cancel_order_confirm)
            .setCancelable(false)
            .setPositiveButton(R.string.confirm) { dialogInterface, i ->
                presenter.cancelStoreOrder(orderId)
                dialogInterface.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create().show()

    }

    private fun handlePay(orderId: Int, price: BigDecimal) {
        val orderIds = ArrayList<Int>()
        orderIds.add(orderId)
//        startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
//            putDouble(Constant.PARAM_TOTAL_PRICE, price.toDouble())
//            putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
//            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_ESTORE)
//        })
//        WebPayActivity.navigation(this, Constant.PAY_INFO_ESTORE, orderIds)

        PayCounterActivity.navigation(this, PayUrlGet.MALL, orderIds, totalPrice.toString())
    }


    /**
     * 申请售后
     */
    private fun doRefund() {

        startActivity(StoreRefundActivity::class.java, Bundle().apply {
            putParcelableArrayList(Constant.PARAM_DATA, ArrayList(mAdapter.data))
            putInt(Constant.PARAM_ORDER_ID, orderId)
            putDouble(Constant.PARAM_TOTAL_PRICE, totalPrice)
        })

    }


    /**
     * 查看售后详情
     */
    private fun viewRefundDetail() {
        startActivity(StoreRefundDetailActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ORDER_ID, orderId)
        })
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }

        when (baseEvent.code) {
            EVENT_REFRESH_ORDER_LIST -> {
                presenter.getOrderDetail(orderId)
            }
            else -> {

            }
        }
    }

    override fun orderCancelSuccess() {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
        finish()
    }


}