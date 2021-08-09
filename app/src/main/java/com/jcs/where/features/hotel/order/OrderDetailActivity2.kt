package com.jcs.where.features.hotel.order

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelOrderDetail
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.pay.StorePayActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by Wangsw  2021/8/3 14:45.
 *
 */
class OrderDetailActivity2 : BaseMvpActivity<OrderDetailPresenter>(), OrderDetailView {

    var order_id = 0
    var lat = 0f
    var lng = 0f
    var contactNumber = ""
    private var merUuid = "";
    private var merName = "";


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

        nav_ll.setOnClickListener {
            FeaturesUtil.startNaviGoogle(this, lat, lng)
        }

        call_ll.setOnClickListener {
            if (contactNumber.isBlank()) {
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$contactNumber")
            }
            startActivity(intent)
        }

        chat_ll.setOnClickListener {
            if (merUuid.isBlank()) {
                return@setOnClickListener
            }
            RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, merUuid, merName, null)
        }
    }

    override fun bindDetail(response: HotelOrderDetail) {
        val order_data = response.order_data!!
        val paymentData = response.payment_data!!
        val hotelData = response.hotel_data!!
        val roomData = response.room_data!!
        val status = order_data.order_status

        lat = hotelData.lat
        lng = hotelData.lng
        contactNumber = hotelData.tel
        merUuid = hotelData.mer_uuid
        merName = hotelData.mer_name

        if (hotelData.im_status == 1 && merUuid.isNotBlank()) {
            chat_v.visibility = View.VISIBLE
            chat_ll.visibility = View.VISIBLE
        } else {
            chat_v.visibility = View.GONE
            chat_ll.visibility = View.GONE
        }

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
        location_tv.text = hotelData.address.replace("\n", "")
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
                7 -> {
                    text = getString(R.string.store_status_desc_7)
                    visibility = View.VISIBLE
                }
                8 -> {
                    text = getString(R.string.store_status_desc_8)
                    visibility = View.VISIBLE
                }
                9 -> {
                    text = getString(R.string.store_status_desc_9)
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
                            val orderIds = ArrayList<Int>()
                            orderIds.add(order_id)
                            startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
                                putDouble(Constant.PARAM_TOTAL_PRICE, order_data.price.toDouble())
                                putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                                putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_HOTEL)
                            })
                        }
                    }

                    right_tv.apply {
                        text = getString(R.string.to_cancel_order)
                        visibility = View.VISIBLE
                        setOnClickListener {

                            AlertDialog.Builder(this@OrderDetailActivity2)
                                    .setTitle(R.string.prompt)
                                    .setMessage(R.string.cancel_order_confirm)
                                    .setPositiveButton(R.string.ensure) { dialogInterface, i ->
                                        presenter.cancelOrder(order_id)
                                        dialogInterface.dismiss()
                                    }
                                    .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.dismiss() }
                                    .create().show()
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
                                AlertDialog.Builder(this@OrderDetailActivity2)
                                        .setTitle(R.string.prompt)
                                        .setMessage(R.string.refund_dialog_hint)
                                        .setPositiveButton(R.string.ensure) { dialogInterface, i ->
                                            presenter.refundOrder(order_id)
                                            dialogInterface.dismiss()
                                        }
                                        .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.dismiss() }
                                        .create().show()
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

//    override fun onEventReceived(baseEvent: BaseEvent<*>) {
//        if (baseEvent.code == EventCode.EVENT_CANCEL_PAY) {
//            finish()
//        }
//    }


}