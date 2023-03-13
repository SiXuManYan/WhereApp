package com.jiechengsheng.city.features.hotel.order

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.hotel.ComplaintRequest
import com.jiechengsheng.city.api.response.hotel.HotelOrderDetail
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.comment.CommentPostActivity
import com.jiechengsheng.city.features.gourmet.refund.ComplexRefundActivity
import com.jiechengsheng.city.features.gourmet.refund.ComplexRefundPresenter
import com.jiechengsheng.city.features.gourmet.refund.detail.FoodRefundInfoActivity
import com.jiechengsheng.city.features.gourmet.refund.detail.FoodRefundInfoPresenter
import com.jiechengsheng.city.features.mall.refund.complaint.ComplaintActivity
import com.jiechengsheng.city.features.payment.WebPayActivity
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.FeaturesUtil
import com.jiechengsheng.city.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal


/**
 * Created by Wangsw  2021/8/3 14:45.
 * 酒店订单详情
 */
class HotelOrderDetailActivity : BaseMvpActivity<HotelOrderDetailPresenter>(), HotelOrderDetailView {

    var order_id = 0
    var lat = 0f
    var lng = 0f
    var contactNumber = ""
    private var merUuid = "";
    private var merName = "";

    private var alreadyComplaint = false

    /** 处理申诉 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            alreadyComplaint = true
            ToastUtils.showShort(R.string.complained_success)
        }
    }

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

        presenter = HotelOrderDetailPresenter(this)
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
            BusinessUtils.startRongCloudConversationActivity(this, merUuid, merName)
        }

        complaint_tv.setOnClickListener {
            // 投诉
            if (alreadyComplaint) {
                ToastUtils.showShort(R.string.complained_success)
            } else {
                val intent = Intent(this, ComplaintActivity::class.java)
                    .putExtra(Constant.PARAM_ORDER_ID, order_id)
                    .putExtra(Constant.PARAM_TYPE, ComplaintRequest.TYPE_MALL)
                searchLauncher.launch(intent)
            }
        }
    }

    override fun bindDetail(response: HotelOrderDetail) {
        val order_data = response.order_data!!
        val paymentData = response.payment_data!!
        val hotelData = response.hotel_data!!
        val roomData = response.room_data!!

        /**
         * 订单状态（1-待付款，2-支付审核中，3-商家待确认，4-待使用，5-交易成功，6-订单取消（未支付时取消），7-交易关闭，8-退款中，9-退款成功, 10-商家审核中, 11-商家拒绝售后, 12-退款失败）
         */
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
        val price = order_data.price
        amount_tv.text = getString(R.string.price_unit_format, price.toPlainString())

        if (status != 1) {
            pay_way_ll.visibility = View.VISIBLE
            payment_method_tv.text = paymentData.payment_channel
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

        desc_tv.text = BusinessUtils.getHotelStatusDescText(status)

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
                        /*                    startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
                                                putDouble(Constant.PARAM_TOTAL_PRICE, order_data.price.toDouble())
                                                putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                                                putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_HOTEL)
                                            })*/
                        WebPayActivity.navigation(this@HotelOrderDetailActivity, Constant.PAY_INFO_HOTEL, orderIds)
                    }
                }

                right_tv.apply {
                    text = getString(R.string.to_cancel_order)
                    visibility = View.VISIBLE
                    setOnClickListener {

                        AlertDialog.Builder(this@HotelOrderDetailActivity)
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
                            doRefund(price)
                        }
                    } else {
                        visibility = View.GONE
                    }
                }
            }
            5 -> {
                left_tv.apply {
                    if (order_data.comment_status == 1) {
                        text = getString(R.string.to_review)
                        visibility = View.VISIBLE
                        setOnClickListener {
                            CommentPostActivity.navigation(this@HotelOrderDetailActivity, 0, hotelData.id, order_id)
                        }
                    } else {
                        visibility = View.GONE
                    }
                }
                right_tv.visibility = View.GONE
            }
            8, 9, 10, 11 -> {
                bottom_ll.visibility = View.VISIBLE
                left_tv.visibility = View.GONE
                right_tv.apply {
                    text = getString(R.string.refund_information)
                    visibility = View.VISIBLE
                    setOnClickListener {
                        viewRefundInfo()
                    }
                }
            }
            12 -> {
                bottom_ll.visibility = View.VISIBLE
                left_tv.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.apply_again)
                    setOnClickListener {
                        doRefund(price)
                    }
                }
                right_tv.apply {
                    text = getString(R.string.refund_information)
                    visibility = View.VISIBLE
                    setOnClickListener {
                        viewRefundInfo()
                    }
                }
            }
            else -> {
                bottom_ll.visibility = View.GONE
            }
        }


        // 退款失败原因
        if (status == 12) {
            fail_reason_rl.visibility = View.VISIBLE
            reason_split_v.visibility = View.VISIBLE
            fail_reason_tv.text = order_data.error_reason
        } else {
            fail_reason_rl.visibility = View.GONE
            reason_split_v.visibility = View.GONE
        }

        // 投诉
        if (status == 11) {
            complaint_tv.visibility = View.VISIBLE
        } else {
            complaint_tv.visibility = View.GONE
        }


    }

    override fun cancelSuccess() {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
        presenter.getDetail(order_id)
    }


    private fun doRefund(price: BigDecimal) {
        ComplexRefundActivity.navigation(this,
            order_id,
            price.toPlainString(),
            price.toPlainString(),
            ComplexRefundPresenter.TYPE_HOTEL)
    }

    private fun viewRefundInfo() = FoodRefundInfoActivity.navigation(this, order_id, FoodRefundInfoPresenter.TYPE_HOTEL)



    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }

        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                presenter.getDetail(order_id)
            }
            else -> {

            }
        }
    }


}