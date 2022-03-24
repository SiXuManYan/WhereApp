package com.jcs.where.features.mall.order

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallOrderDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.comment.CommentPostActivity
import com.jcs.where.features.mall.detail.MallDetailActivity
import com.jcs.where.features.store.comment.detail.StoreCommentDetailActivity
import com.jcs.where.features.store.pay.StorePayActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_mall_order_detail.*
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/16 10:43.
 * 新版商城订单详情
 */
class MallOrderDetailActivity : BaseMvpActivity<MallOrderDetailPresenter>(), MallOrderDetailView {


    override fun getLayoutId() = R.layout.activity_mall_order_detail

    private var orderId = 0

    private var totalPrice = 0.0

    private lateinit var mAdapter: MallOrderDetailAdapter

    private var tel = ""

    override fun isStatusDark() = true


    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
        }

        mAdapter = MallOrderDetailAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val itemData = mAdapter.data[position]
                when (itemData.delete_status) {
                    0 -> {
                        if (itemData.good_status == 0) {
                            ToastUtils.showShort(R.string.product_removed)
                        } else {
                            MallDetailActivity.navigation(this@MallOrderDetailActivity, itemData.good_id)
                        }
                    }
                    1 -> {
                        ToastUtils.showShort(R.string.product_delete)
                    }

                }


            }
        }
        good_rv.apply {
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically() = false
            }
            adapter = mAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.white), SizeUtils.dp2px(10f), 0, 0))
        }
    }

    override fun initData() {
        presenter = MallOrderDetailPresenter(this)
        presenter.getOrderDetail(orderId)

    }

    override fun bindListener() = Unit


    override fun bindData(data: MallOrderDetail) {
        status_tv.text = presenter.getStatusText(data.order_status)
        presenter.getStatusDescText(status_desc_tv, data.order_status)
        order_number_tv.text = data.trade_no
        created_date_tv.text = data.created_at
        val price = data.price
        totalPrice = price.toDouble()
        price_tv.text = getString(R.string.price_unit_format, price.toPlainString())


        // 商家服务
        recipient_info_ll.visibility = View.VISIBLE
        data.address?.let {
            address_name_tv.text = it.address
            recipient_tv.text = getString(R.string.star_text_format, it.contact_name, it.contact_number)
        }

        // 商品备注
        delivery_rl.visibility = View.VISIBLE
        remark_rl.visibility = View.VISIBLE
        delivery_price_tv.text = getString(R.string.price_unit_format, data.delivery_fee)
        remarks_value_tv.text = data.remark

        // 支付信息
        if (data.order_status != 1 && data.order_status != 6) {
            pay_way_tv.text = data.pay_channel
            payment_name_tv.text = getString(R.string.payment_name_format, data.bank_card_account)
            payment_account_tv.text = getString(R.string.payment_account_format, data.bank_card_number)
            pay_container_ll.visibility = View.VISIBLE
        } else {
            pay_container_ll.visibility = View.GONE
        }


        val shopName = data.shop_title
        val shopImage = data.shop_images
        business_name_tv.text = shopName


        mAdapter.setNewInstance(data.goods)

        // 商品信息
        if (data.im_status == 1 && !TextUtils.isEmpty(data.mer_uuid)) {
            im_ll.visibility = View.VISIBLE
            im_ll.setOnClickListener {
                BusinessUtils.startRongCloudConversationActivity(this, data.mer_uuid, data.mer_name, data.tel)
            }
        } else {
            im_ll.visibility = View.GONE
        }

        // 物流
        val companyName = data.company_name

        if (companyName.isNotBlank()) {
            logistics_container_ll.visibility = View.VISIBLE
            logistics_company_tv.text = getString(R.string.logistics_company_format, companyName)
            logistics_number_tv.text = getString(R.string.logistics_number_format, data.logistics)

        } else {
            logistics_container_ll.visibility = View.GONE
        }

        // ## 价格明细 ##
        // 优惠前的总价
        total_price_old_tv.text = getString(R.string.price_unit_format, data.total_price)

        // 总运费
        total_delivery_fee_tv.text = getString(R.string.price_unit_format, data.delivery_fee)

        // 商家优惠
        shop_offers_tv.text = getString(R.string.price_unit_format, data.shop_coupon_money)

        // 平台优惠
        coupon_tv.text = getString(R.string.price_unit_format, data.platform_coupon_money)

        // 合计
        total_price_copy_tv.text = getString(R.string.price_unit_format, data.price.toPlainString())


        // 底部
        when (data.order_status) {
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
                bottom_container_rl.visibility = View.GONE
            }
            4 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.visibility = View.GONE
                right_tv.visibility = View.VISIBLE
                right_tv.text = getString(R.string.confirm_receipt)
                right_tv.setOnClickListener {
                    // 确认收货
                    AlertDialog.Builder(this@MallOrderDetailActivity)
                        .setTitle(R.string.prompt)
                        .setMessage(R.string.confirm_receipt_hint)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ensure) { dialogInterface, i ->
                            presenter.confirmReceipt(orderId)
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.dismiss() }
                        .create().show()

                }
            }
            5 -> {

                val commentStatus = data.comment_status
                if (commentStatus == 3) {
                    bottom_container_rl.visibility = View.GONE
                } else {
                    bottom_container_rl.visibility = View.VISIBLE
                }

                left_tv.visibility = View.GONE

                if (commentStatus == 3) {
                    right_tv.visibility = View.GONE
                } else {
                    right_tv.visibility = View.VISIBLE

                    if (commentStatus == 1) {
                        right_tv.text = getString(R.string.evaluation_go)
                        right_tv.setOnClickListener {
                            // 去评价
                            CommentPostActivity.navigation(this, 2, null, orderId, shopName, shopImage)
                        }
                    }

                    if (commentStatus == 2) {
                        right_tv.text = getString(R.string.view_evaluation)
                        right_tv.setOnClickListener {
                            // 查看评价
                            startActivity(StoreCommentDetailActivity::class.java, Bundle().apply {
                                putInt(Constant.PARAM_COMMENT_ID, data.comments_id)
                                putInt(Constant.PARAM_TYPE, 1)
                            })
                        }
                    }
                }
            }
            8, 9, 10, 11, 12 -> {
                // 新增了单独的售后详情页，订单详情不处理售后相关

                bottom_container_rl.visibility = View.GONE

            }
            else -> {
                bottom_container_rl.visibility = View.GONE
            }
        }
        bottom_v.visibility = bottom_container_rl.visibility

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
        startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
            putDouble(Constant.PARAM_TOTAL_PRICE, price.toDouble())
            putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_ESTORE)
        })
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }

        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                presenter.getOrderDetail(orderId)
            }
            else -> {

            }
        }
    }

    override fun orderCancelSuccess() {
        EventBus.getDefault().post(BaseEvent<Boolean>(EventCode.EVENT_REFRESH_ORDER_LIST))
        finish()
    }


    override fun confirmReceipt() {
        presenter.getOrderDetail(orderId)
        EventBus.getDefault().post(BaseEvent<Boolean>(EventCode.EVENT_REFRESH_ORDER_LIST))
    }
}