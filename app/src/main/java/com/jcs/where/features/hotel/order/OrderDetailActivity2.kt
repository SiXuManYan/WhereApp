package com.jcs.where.features.hotel.order

import android.view.View
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelOrderDetail
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by Wangsw  2021/8/3 14:45.
 *
 */
class OrderDetailActivity2 : BaseMvpActivity<OrderDetailPresenter>(), OrderDetailView {

    var order_id = 0


    override fun getLayoutId() = R.layout.activity_order_detail

    override fun initView() {
        findViewById<View>(android.R.id.content).background = ResourceUtils.getDrawable(R.drawable.shape_gradient_hotel_detail)
        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        order_id = bundle.getInt(Constant.PARAM_ORDER_ID, 0)

    }

    override fun initData() {

        presenter = OrderDetailPresenter(this)
        presenter.getDetail(order_id)

    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }
    }

    override fun bindDetail(response: HotelOrderDetail) {
        val order_data = response.order_data!!
        val paymentData = response.payment_data!!
        val hotelData = response.hotel_data!!
        val roomData = response.room_data!!
        val status = order_data.order_status

        status_tv.text = BusinessUtils.getHotelStatusText(status)
        amount_tv.text = getString(R.string.price_unit_format, order_data.price.toPlainString())

        if (status != 1) {
            pay_way_ll.visibility = View.VISIBLE
            payment_method_tv.text = paymentData.payment_channel
            payment_name_tv.text = getString(R.string.payment_name_format, paymentData.bank_card_account)
            payment_account_tv.text = getString(R.string.payment_account_format, paymentData.bank_card_number)
        } else {
            pay_way_ll.visibility = View.GONE
        }

        val images = hotelData.images
        if (images.isNotEmpty()) {
            GlideUtil.load(this, images[0], image_iv, 5)
        }
        hotel_name_tv.text = hotelData.name
        location_tv.text = hotelData.address
        room_name_tv.text = getString(R.string.room_name_format, roomData.name, roomData.room_num)

        room_type_tv.text = getString(R.string.room_type_format, roomData.room_type)

        val breakfast = if (roomData.breakfast_type == 1) {
            getString(R.string.breakfast_support)
        } else {
            getString(R.string.breakfast_un_support)
        }
        val wifi = if (roomData.wifi_type == 1) {
            getString(R.string.free_wifi)
        } else {
            getString(R.string.charged_wifi)
        }

        room_content_tv.text = getString(R.string.room_content_format, breakfast, wifi)
        time_tv.text = getString(R.string.room_time_format, roomData.start_date, roomData.end_date, roomData.days)
        user_name_tv.text = roomData.username
        user_phone_tv.text = roomData.phone
        order_number_tv.text = order_data.trade_no
        created_date_tv.text = order_data.created_at


        desc_tv.apply {

            // 订单状态（1-待付款，2-支付审核中，3-商家待确认，4-待使用，5-交易成功，6-订单取消（未支付时取消），7-交易关闭，8-退款中，9-退款成功）
            when (status) {
                1 -> {
                    text = getString(R.string.hotel_order_status_1)
                    visibility = View.VISIBLE
                }
                6 -> {
                    text = getString(R.string.hotel_order_status_6)
                    visibility = View.VISIBLE
                }
                3 -> {
                    text = getString(R.string.hotel_order_status_3)
                    visibility = View.VISIBLE
                }
                4 -> {
                    text = getString(R.string.hotel_order_status_4)
                    visibility = View.VISIBLE
                }
                5 -> {
                    text = getString(R.string.hotel_order_status_5)
                    visibility = View.VISIBLE
                }
                else -> {
                    visibility = View.GONE
                }
            }

        }

        bottom_ll.apply {

            // 订单状态（1-待付款，2-支付审核中，3-商家待确认，4-待使用，5-交易成功，6-订单取消（未支付时取消），7-交易关闭，8-退款中，9-退款成功）
            when (status) {
                1 -> {
                    bottom_ll.visibility = View.VISIBLE
                    left_tv.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.to_pay_2)
                        setOnClickListener {
                            // 立即支付

                        }
                    }

                    right_tv.apply {
                        text = getString(R.string.to_cancel_order)
                        visibility = View.VISIBLE
                        setOnClickListener {
                            presenter.cancelOrder(order_id)
                        }
                    }

                }
                3, 4 -> {
                    bottom_ll.visibility = View.VISIBLE
                    left_tv.visibility = View.GONE
                    right_tv.apply {
                        if (order_data.is_cancel) {
                            text = getString(R.string.to_refund)
                            visibility = View.VISIBLE
                            setOnClickListener {
                                presenter.refundOrder(order_id)
                            }
                        } else {
                            visibility = View.GONE
                        }
                    }
                }
//                5 -> {
//                    left_tv.apply {
//                        if (order_data.comment_status == 1) {
//                            text = getString(R.string.to_review)
//                            visibility = View.VISIBLE
//                            setOnClickListener {
//                              // 去评价
//                            }
//                        } else {
//                            visibility = View.GONE
//                        }
//                    }
//                    right_tv.visibility = View.GONE
//                }
                else -> {
                    bottom_ll.visibility = View.GONE
                }
            }


        }

    }

    override fun cancelSuccess() {
        EventBus.getDefault().post(EventCode.EVENT_REFRESH_ORDER_LIST)
        presenter.getDetail(order_id)
    }


    override fun refundCommitSuccess() {
        EventBus.getDefault().post(EventCode.EVENT_REFRESH_ORDER_LIST)
        ToastUtils.showShort(getString(R.string.refund_commit_success))
        finish()
    }


}