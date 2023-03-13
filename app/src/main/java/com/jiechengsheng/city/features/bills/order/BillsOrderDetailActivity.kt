package com.jiechengsheng.city.features.bills.order

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.order.bill.BillOrderDetails
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.bills.record.result.BillsRecommitResultActivity
import com.jiechengsheng.city.features.com100.ExtendChatActivity
import com.jiechengsheng.city.features.gourmet.refund.ComplexRefundActivity
import com.jiechengsheng.city.features.gourmet.refund.ComplexRefundPresenter
import com.jiechengsheng.city.features.gourmet.refund.detail.FoodRefundInfoActivity
import com.jiechengsheng.city.features.gourmet.refund.detail.FoodRefundInfoPresenter
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_bills_detail.*
import java.math.BigDecimal


/**
 * Created by Wangsw  2021/7/20 10:49.
 * 水电订单详情
 */
class BillsOrderDetailActivity : BaseMvpActivity<BillsDetailPresenter>(), BillsDetailView {

    private var orderId = 0

    private lateinit var mAdapter: BillOrderDetailAdapter

    override fun getLayoutId() = R.layout.activity_bills_detail

    override fun isStatusDark() = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
        }

        mAdapter = BillOrderDetailAdapter()
        content_rv.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                1,
                SizeUtils.dp2px(15f),
                0))

        }
    }

    override fun initData() {

        presenter = BillsDetailPresenter(this)
        presenter.getDetail(orderId)
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }
        service_ll.setOnClickListener {
            startActivityAfterLogin(ExtendChatActivity::class.java)
        }
    }

    override fun bindDetail(data: BillOrderDetails) {


        /**
         * 订单状态（0-待支付，1-缴费中，2-缴费成功，3-缴费失败，4-订单关闭，5-退款审核中，6-拒绝退款，7-退款中，8-退款成功，9-退款失败）
         */
        val orderStatus = data.order_status
        status_tv.text = BusinessUtils.getBillsStatusText(orderStatus)
        status_desc_tv.text = BusinessUtils.getBillsStatusDesc(orderStatus)


        // 退款失败
        if (orderStatus == 9) {
            fail_reason_rl.visibility = View.VISIBLE
            fail_reason_tv.text = data.refund_refuse_reason
        } else {
            fail_reason_rl.visibility = View.GONE
        }

        order_number_tv.text = data.trade_no
        created_date_tv.text = data.created_at

        // 支付方式
        if (orderStatus == 0) {
            payment_container_ll.visibility = View.GONE
        } else {
            payment_container_ll.visibility = View.VISIBLE
            pay_way_tv.text = data.payment_method
        }

        // 联系客服
        if (orderStatus == 3) {
            service_ll.visibility = View.VISIBLE
        } else {
            service_ll.visibility = View.GONE
        }


        // 字段
        val billsParams = data.bills_params
        mAdapter.setNewInstance(billsParams)

        when (orderStatus) {
            3 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.apply {
                    visibility = View.VISIBLE
                    text = StringUtils.getString(R.string.to_cancel_order)
                    setOnClickListener {
                        ComplexRefundActivity.navigation(this@BillsOrderDetailActivity,
                            orderId,
                            data.refund_price.toPlainString(),
                            data.total_price.toPlainString(),
                            ComplexRefundPresenter.TYPE_BILL)
                    }
                }
                right_tv.apply {
                    visibility = View.VISIBLE
                    text = StringUtils.getString(R.string.resubmit)
                    setOnClickListener {
                        presenter.recommit(orderId, data.order_type)
                    }
                }
            }
            5, 6, 7, 8 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.apply {
                    visibility = View.VISIBLE
                    text = StringUtils.getString(R.string.refund_information)
                    setOnClickListener {
                        FoodRefundInfoActivity.navigation(this@BillsOrderDetailActivity,
                            orderId, FoodRefundInfoPresenter.TYPE_BILL)
                    }
                }
                right_tv.visibility = View.GONE
            }
            9 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.apply {
                    visibility = View.VISIBLE
                    text = StringUtils.getString(R.string.refund_information)
                    setOnClickListener {
                        FoodRefundInfoActivity.navigation(this@BillsOrderDetailActivity,
                            orderId, FoodRefundInfoPresenter.TYPE_BILL)
                    }
                }
                right_tv.apply {
                    visibility = View.VISIBLE
                    text = StringUtils.getString(R.string.apply_again)
                    setOnClickListener {
                        ComplexRefundActivity.navigation(this@BillsOrderDetailActivity,
                            orderId,
                            data.refund_price.toPlainString(),
                            data.total_price.toPlainString(),
                            ComplexRefundPresenter.TYPE_BILL)
                    }
                }
            }
            else -> {
                bottom_container_rl.visibility = View.GONE
            }
        }


        // 顶部价格
        price_tv.text = getString(R.string.price_unit_format, data.total_price.toPlainString())
        // 支付明细
        payment_amount_bill_tv.text = getString(R.string.price_unit_format, data.bills_price.toPlainString())
        service_charge_bill_tv.text = getString(R.string.price_unit_format, data.service_price)
        // 满减价格
        val discounts = data.discounts


        when (data.type) {
            1 -> {
                discount_title_tv.text = getString(R.string.sale_price_bill)
                setDiscount(discounts)
            }
            2 -> {
                discount_title_tv.text = getString(R.string.rebate_price_bill)
                setDiscount(discounts)
            }
            else -> {
                discount_rl.visibility = View.GONE
            }
        }

        actual_payment_bill_tv.text = getString(R.string.price_unit_format, data.total_price.toPlainString())

        coupon_money_tv.text =getString(R.string.price_unit_format, data.coupon_price)

    }

    private fun setDiscount(discounts: String) {
        if (BusinessUtils.getSafeBigDecimal(discounts).compareTo(BigDecimal.ZERO) == 1) {
            discount_rl.visibility = View.VISIBLE
            discount_v.visibility = View.VISIBLE
            discount_value_tv.text = getString(R.string.price_unit_discount_format, discounts)
        } else {
            discount_rl.visibility = View.GONE
            discount_v.visibility = View.GONE
        }
    }

    override fun recommitSuccess(orderId: Int) {
        startActivity(BillsRecommitResultActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ORDER_ID, orderId)
        })
    }

}