package com.jcs.where.features.bills.hydropower.detail

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.response.order.bill.BillOrderDetails
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.record.result.BillsRecommitResultActivity
import com.jcs.where.features.com100.ExtendChatActivity
import com.jcs.where.features.gourmet.refund.ComplexRefundActivity
import com.jcs.where.features.gourmet.refund.ComplexRefundPresenter
import com.jcs.where.features.gourmet.refund.detail.FoodRefundInfoActivity
import com.jcs.where.features.gourmet.refund.detail.FoodRefundInfoPresenter
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_bills_detail.*


/**
 * Created by Wangsw  2021/7/20 10:49.
 * 水电订单详情
 */
class BillsDetailActivity : BaseMvpActivity<BillsDetailPresenter>(), BillsDetailView {

    private var orderId = 0

    private lateinit var mAdapter: BillDetailAdapter

    override fun getLayoutId() = R.layout.activity_bills_detail

    override fun isStatusDark() = true

    override fun initView() {

        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
        }

        mAdapter = BillDetailAdapter()
        content_rv.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f),
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
        price_tv.text = data.total_price.toPlainString()


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
                        ComplexRefundActivity.navigation(this@BillsDetailActivity,
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
                        presenter.recommit(orderId)
                    }
                }
            }
            5, 6, 7, 8 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.apply {
                    visibility = View.VISIBLE
                    text = StringUtils.getString(R.string.refund_information)
                    setOnClickListener {
                        FoodRefundInfoActivity.navigation(this@BillsDetailActivity,
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
                        FoodRefundInfoActivity.navigation(this@BillsDetailActivity,
                            orderId, FoodRefundInfoPresenter.TYPE_BILL)
                    }
                }
                right_tv.apply {
                    visibility = View.VISIBLE
                    text = StringUtils.getString(R.string.apply_again)
                    setOnClickListener {
                        ComplexRefundActivity.navigation(this@BillsDetailActivity,
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

    }

    override fun recommitSuccess(orderId: Int) {
        startActivity(BillsRecommitResultActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ORDER_ID, orderId)
        })
    }

}