package com.jcs.where.features.gourmet.takeaway.order2

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.order.TakeawayOrderDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.customer.ExtendChatActivity
import com.jcs.where.features.gourmet.takeaway.order.TakeawayGoodDataAdapter
import com.jcs.where.features.gourmet.takeaway.order.TakeawayOrderDetailPresenter
import com.jcs.where.features.gourmet.takeaway.order.TakeawayOrderDetailView
import com.jcs.where.features.pay.PayActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_takeaway_order_detail_2.*


/**
 * Created by Wangsw  2021/7/28 16:12.
 *
 */
class TakeawayOrderDetailActivity2 : BaseMvpActivity<TakeawayOrderDetailPresenter>(), TakeawayOrderDetailView {

    private var orderId = "";
    private var contactNumber = "";
    private var merUuid = "";
    private var restaurantName = "";
    private lateinit var mAdapter: TakeawayGoodDataAdapter
    private var tel = ""

    override fun getLayoutId() = R.layout.activity_takeaway_order_detail_2

    override fun isStatusDark() = true

    override fun initView() {

        BarUtils.setStatusBarColor(this, Color.WHITE)
        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
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
            RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, merUuid, restaurantName, null)
        }
    }

    override fun bindDetail(it: TakeawayOrderDetail) {
        val goodData = it.good_data
        val restaurantData = it.restaurant_data
        val orderData = it.order_data
        val paymentChannel = it.payment_channel
        merUuid = restaurantData.mer_uuid
        restaurantName = restaurantData.name




        //
        status_tv.text = BusinessUtils.getDelicacyOrderStatusText(orderData.status)

        price_tv.text = getString(R.string.price_unit_format, orderData.price.toPlainString())
        order_number_tv.text = orderData.trade_no
        created_date_tv.text = orderData.created_at
        delivery_time_tv.text = if (orderData.delivery_time_type == 1) {
            getString(R.string.delivery_now)
        } else {
            orderData.delivery_time
        }

        val address = orderData.address
        contactNumber = address.contact_number
        address_tv.text = address.address
        contact_name_tv.text = getString(R.string.star_text_format, address.contact_name ,address.contact_number)


        if (orderData.status != 1 && orderData.status != 2) {
            payment_ll.visibility = View.VISIBLE
            pay_way_tv.text = paymentChannel.payment_channel
            payment_name_tv.text = paymentChannel.bank_card_account
            payment_account_tv.text = paymentChannel.bank_card_number
        } else {
            payment_ll.visibility = View.GONE
        }

        business_name_tv.text = restaurantData.mer_name
        packaging_fee_tv.text = getString(R.string.price_unit_format, orderData.packing_charges.toPlainString())
        delivery_fee_tv.text = getString(R.string.price_unit_format, orderData.delivery_cost.toPlainString())
        remarks_tv.text = orderData.remark

        mAdapter.setNewInstance(goodData)


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
//                                presenter.cancelOrder(orderId)
                                dialogInterface.dismiss()
                            }
                            .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.dismiss() }
                            .create().show()

                }

                right_tv.setOnClickListener {
                    startActivity(PayActivity::class.java, Bundle().apply {
//                        putString(Constant.PARAM_TOTAL_PRICE, price.toPlainString())
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
//                                presenter.refundOrder(orderId)
                                dialogInterface.dismiss()
                            }
                            .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.dismiss() }
                            .create().show()
                }
            }
/*            6 -> {

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
                        // 评价
                    }
                } else {
                    bottom_container_rl.visibility = View.GONE
                }

            }*/
            else -> {
                bottom_container_rl.visibility = View.GONE
            }
        }

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

}