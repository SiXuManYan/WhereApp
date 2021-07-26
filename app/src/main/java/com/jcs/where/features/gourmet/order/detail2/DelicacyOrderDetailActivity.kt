package com.jcs.where.features.gourmet.order.detail2

import android.graphics.Color
import com.blankj.utilcode.util.BarUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.order.FoodOrderDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.customer.ExtendChatActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_delicacy_order_detail.*

/**
 * Created by Wangsw  2021/7/23 15:59.
 * 美食订单详情
 */
class DelicacyOrderDetailActivity :BaseMvpActivity<DelicacyOrderDetailPresenter>(),DelicacyOrderDetailView{

    private var orderId = "";

    override fun getLayoutId() = R.layout.activity_delicacy_order_detail

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        orderId = bundle.getString(Constant.PARAM_ORDER_ID, "")
    }

    override fun initData() {
        presenter = DelicacyOrderDetailPresenter(this)
        presenter.getDetail(orderId)
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }
        service_iv.setOnClickListener {
            startActivity(ExtendChatActivity::class.java)
        }

    }

    override fun bindDetail(it: FoodOrderDetail) {
        TODO("Not yet implemented")
    }

}