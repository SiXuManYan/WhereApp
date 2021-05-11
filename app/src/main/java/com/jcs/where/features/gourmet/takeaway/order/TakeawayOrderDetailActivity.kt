package com.jcs.where.features.gourmet.takeaway.order

import android.view.View
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.order.TakeawayOrderDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_order_detail_takeaway.*

/**
 * Created by Wangsw  2021/5/11 14:53.
 * 外卖订单详情
 */
class TakeawayOrderDetailActivity : BaseMvpActivity<TakeawayOrderDetailPresenter>(), TakeawayOrderDetailView {

    private var orderId = "";

    override fun getLayoutId() = R.layout.activity_order_detail_takeaway

    override fun initView() {
        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        orderId = bundle.getString(Constant.PARAM_ORDER_ID, "")
    }

    override fun initData() {
        presenter = TakeawayOrderDetailPresenter(this)
        presenter.getDetail(orderId)
    }

    override fun bindListener() {

    }

    override fun bindDetail(it: TakeawayOrderDetail) {
        val goodData = it.good_data
        val restaurantDate = it.restaurant_date
        val orderData = it.order_data

        chat_iv.visibility = if (restaurantDate.im_status == 1) {
            View.VISIBLE
        } else {
            View.GONE
        }

    }


}