package com.jcs.where.features.integral.order

import android.graphics.Color
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.jcs.where.R
import com.jcs.where.api.response.integral.IntegralOrderDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_integral_order_detail.*

/**
 * Created by Wangsw  2022/9/26 17:10.
 * 积分订单详情
 */
class IntegralOrderDetailActivity : BaseMvpActivity<IntegralOrderDetailPresenter>(), IntegralOrderDetailView {

    var orderId = 0

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_integral_order_detail

    override fun initView() {
        BarUtils.setStatusBarColor(this,Color.WHITE)
        orderId = intent.getIntExtra(Constant.PARAM_ID , 0)
    }

    override fun initData() {
        presenter = IntegralOrderDetailPresenter(this)
        presenter.getData(orderId)
    }

    override fun bindListener() {

    }

    override fun bindData(response: IntegralOrderDetail) {


        when (response.order_status) {
            1 ->{
                status_tv.text = getString(R.string.store_status_3)
                recipient_info_ll.visibility = View.VISIBLE

                response.address?.let {
                    address_name_tv.text = it.address
                    recipient_tv.text = getString(R.string.star_text_format, it.contact_name, it.contact_number)
                }

            }
            2 ->{
                status_tv.text = getString(R.string.store_status_5)
                recipient_info_ll.visibility = View.GONE
            }
        }
        price_tv.text = getString(R.string.points_format, response.price)
        order_number_tv.text = response.trade_no
        created_date_tv.text = response.created_at

        GlideUtil.load(this,response.image,image_iv)
        title_tv.text = response.title
        points_tv.text = getString(R.string.points_format , response.price)

    }


}