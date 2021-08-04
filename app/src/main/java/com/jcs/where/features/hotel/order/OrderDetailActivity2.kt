package com.jcs.where.features.hotel.order

import android.view.View
import com.blankj.utilcode.util.ResourceUtils
import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelOrderDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_order_detail.*


/**
 * Created by Wangsw  2021/8/3 14:45.
 *
 */
class OrderDetailActivity2 : BaseMvpActivity<OrderDetailPresenter>(), OrderDetailView {

    var order_id = 0


    override fun getLayoutId() = R.layout.activity_order_detail

    override fun initView() {
        findViewById<View>(android.R.id.content).background = ResourceUtils.getDrawable(R.drawable.shape_gradient_hotel_detail)


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
        desc_tv.apply {

        }

        bottom_ll.apply {

        }
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
        room_type_tv.text = getString(R.string.room_type_format, roomData.name, roomData.room_num)
        ("·" + roomData.room_type).also { content_a_tv.text = it }

        val breakfast = if (roomData.breakfast_type == 1) {
            "含早"
        } else {
            "不含早"
        }
        val wifi = if (roomData.wifi_type == 1) {
            "免费WIFI"
        } else {
            "收费WIFI"
        }

        ("·$breakfast | $wifi").also { content_b_tv.text = it }




    }

}