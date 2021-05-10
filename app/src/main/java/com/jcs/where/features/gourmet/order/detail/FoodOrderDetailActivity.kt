package com.jcs.where.features.gourmet.order.detail

import android.view.View
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_order_detail_food.*

/**
 * Created by Wangsw  2021/5/10 9:47.
 *  美食订单详情
 */
class FoodOrderDetailActivity : BaseMvpActivity<FoodOrderDetailPresenter>(), FoodOrderDetailView {

    private var orderId = "";


    override fun getLayoutId() = R.layout.activity_order_detail_food

    override fun initView() {
        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        orderId = bundle.getString(Constant.PARAM_ORDER_ID, "")
    }

    override fun initData() {
        presenter = FoodOrderDetailPresenter(this);
        presenter.getDetail(orderId)
    }

    override fun bindListener() = Unit

    override fun bindDetail(it: FoodOrderDetail) {

        val goodData = it.good_data
        val restaurantDate = it.restaurant_date
        val orderData = it.order_data

        GlideUtil.load(this, goodData.good_image, image_iv)
        restaurant_name_tv.text = restaurantDate.mer_name
        good_name_tv.text = restaurantDate.name
        state_tv.text = orderData.status.toString()
        count_tv.text = getString(R.string.quantity_format, goodData.good_num)
        total_price_tv.text = getString(R.string.price_unit_format, goodData.price.toPlainString())

        // 券码
        if (orderData.status == 3) {
            coupon_ll.visibility = View.VISIBLE
            coupon_expire_tv.text = getString(R.string.coupon_expire_format, orderData.coupon_expire)
            coupon_no_tv.text = orderData.coupon_no
            GlideUtil.load(this, orderData.coupon_qr_code, coupon_qr_code_iv)
            coupon_ll.visibility = View.VISIBLE
        } else {
            coupon_ll.visibility = View.GONE
        }


        // 订单信息
        trade_no_tv.text = getString(R.string.trade_no_format, orderData.trade_no)
        phone_tv.text = getString(R.string.phone_format, orderData.phone)
        valid_period_tv.text = getString(R.string.valid_period_format, orderData.start_date, orderData.end_date)
        created_at_tv.text = getString(R.string.pay_time_format, orderData.created_at)

        // 底部
        if (orderData.status == 1) {
            bottom_ll.visibility = View.VISIBLE
            // 倒计时
            presenter.countdown(countdown_tv, "")
        } else {
            bottom_ll.visibility = View.GONE
        }


    }


}