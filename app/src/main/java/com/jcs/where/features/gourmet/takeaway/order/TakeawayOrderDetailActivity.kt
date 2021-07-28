package com.jcs.where.features.gourmet.takeaway.order

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.order.TakeawayOrderDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.widget.list.DividerDecoration
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.activity_order_detail_takeaway.*

/**
 * Created by Wangsw  2021/5/11 14:53.
 * 外卖订单详情
 */
class TakeawayOrderDetailActivity : BaseMvpActivity<TakeawayOrderDetailPresenter>(), TakeawayOrderDetailView {

    private var orderId = "";
    private var contactNumber = "";
    private var merUuid = "";
    private var restaurantName = "";
    private lateinit var mAdapter: TakeawayGoodDataAdapter

    override fun getLayoutId() = R.layout.activity_order_detail_takeaway

    override fun isStatusDark() = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        orderId = bundle.getString(Constant.PARAM_ORDER_ID, "")

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
        tel_iv.setOnClickListener {
            if (contactNumber.isBlank()) {
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$contactNumber")
            }
            startActivity(intent)
        }

        chat_iv.setOnClickListener {
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
        merUuid = restaurantData.mer_uuid
        restaurantName = restaurantData.name

        contactNumber = orderData.address.contact_number

        chat_iv.visibility = if (restaurantData.im_status == 1) {
            View.VISIBLE
        } else {
            View.GONE
        }

        FeaturesUtil.bindTakeawayOrderStatus(orderData.status, status_tv)
        restaurant_name_tv.text = restaurantData.name
        packing_charges_tv.text = getString(R.string.price_unit_format, orderData.packing_charges.toPlainString())
        delivery_cost_tv.text = getString(R.string.price_unit_format, orderData.delivery_cost.toPlainString())
        total_price_tv.text = getString(R.string.price_unit_format, orderData.price.toPlainString())
        delivery_time_tv.text = if (orderData.delivery_time_type == 1) {
            getString(R.string.delivery_now)
        } else {
            orderData.delivery_time
        }
        address_tv.text = orderData.address.address
        order_number_tv.text = orderData.trade_no
        pay_time_tv.text = orderData.created_at
        mAdapter.setNewInstance(goodData)
    }


}