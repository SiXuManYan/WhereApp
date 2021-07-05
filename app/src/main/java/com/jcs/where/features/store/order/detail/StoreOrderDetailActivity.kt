package com.jcs.where.features.store.order.detail

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jcs.where.R
import com.jcs.where.api.response.order.store.StoreOrderDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode.EVENT_REFRESH_ORDER_LIST
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.refund.StoreRefundActivity
import com.jcs.where.features.store.refund.detail.StoreRefundDetailActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_store_order_detail.*


/**
 * Created by Wangsw  2021/6/26 11:15.
 *  商城订单详情
 */
class StoreOrderDetailActivity : BaseMvpActivity<StoreOrderDetailPresenter>(), StoreOrderDetailView {


    private var orderId = 0

    private var totalPrice = 0.0

    private lateinit var mAdapter: StoreOrderDetailAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_store_order_detail

    override fun initView() {

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
        }


    }

    override fun initData() {
        presenter = StoreOrderDetailPresenter(this)
        presenter.getOrderDetail(orderId)

    }

    override fun bindListener() {

        mJcsTitle.setFirstRightIvClickListener {

        }

    }


    override fun bindData(data: StoreOrderDetail) {
        status_tv.text = presenter.getStatusText(data.delivery_type, data.status)
        status_desc_tv.text = presenter.getStatusDescText(status_desc_tv, data.status)
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
                address_tv.text = it.address
                recipient_tv.text = getString(R.string.star_text_format, it.contact_name, it.contact_number)
            }

            // 商品备注
            delivery_rl.visibility = View.GONE
            remark_rl.visibility = View.GONE
            delivery_price_tv.text = getString(R.string.price_unit_format, data.delivery_fee.toPlainString())
            remarks_value_tv.text = data.remark

        }

        // 支付信息
        if (data.status != 1) {
            pay_way_tv.text = data.pay_channel
            payment_name_tv.text = getString(R.string.payment_name_format, data.bank_card_account)
            payment_account_tv.text = getString(R.string.payment_account_format, data.bank_card_number)
            pay_container_ll.visibility = View.VISIBLE
        } else {
            pay_container_ll.visibility = View.GONE
        }

        // 商品信息
        data.shop?.let {
            mAdapter.setNewInstance(it.goods)
            business_name_tv.text = it.title
        }

        // 底部
        when (data.status) {
            1 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.visibility = View.VISIBLE
                left_tv.text = getString(R.string.to_cancel_order)
                left_tv.setOnClickListener {
                    // 取消订单
                    showComing()
                }

                right_tv.visibility = View.VISIBLE
                right_tv.text = getString(R.string.to_pay_2)
                right_tv.setOnClickListener {
                    // 去付款
                    showComing()
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

                right_tv.visibility = View.VISIBLE
                right_tv.text = getString(R.string.evaluation)
                right_tv.setOnClickListener {
                    // 评价
                    showComing()
                }


            }
//            8, 9, 10, 11, 12 -> { }
            else -> {
                bottom_container_rl.visibility = View.GONE
            }
        }

//         查看售后详情
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


}