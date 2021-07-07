package com.jcs.where.features.store.refund.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.order.store.RefundDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.EventCode.EVENT_REFRESH_ORDER_LIST
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.order.detail.StoreOrderDetailAdapter
import com.jcs.where.features.store.refund.StoreRefundActivity
import com.jcs.where.features.store.refund.StoreRefundAdapter
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_store_refund_detail.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/7/1 17:53.
 * 售后详情
 */
class StoreRefundDetailActivity : BaseMvpActivity<StoreRefundDetailPresenter>(), StoreRefundDetailView {

    private var orderId = 0
    private var totalPrice = 0.0
    private var cancel_reason = ""
    private lateinit var mAdapter: StoreOrderDetailAdapter
    private lateinit var mImageAdapter: StoreRefundAdapter

    override fun getLayoutId() = R.layout.activity_store_refund_detail

    override fun isStatusDark() = true

    override fun initView() {

        BarUtils.setStatusBarColor(this, Color.WHITE)
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID)
        }

        // 商品信息
        mAdapter = StoreOrderDetailAdapter()
        good_rv.apply {
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically() = false
            }
            adapter = mAdapter
        }

        // 相册
        mImageAdapter = StoreRefundAdapter().apply {
            hideDelete = true
        }
        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

    }

    override fun initData() {
        presenter = StoreRefundDetailPresenter(this)
        presenter.getDetail(orderId)

    }

    override fun bindListener() {

        change_tv.setOnClickListener {

            startActivity(StoreRefundActivity::class.java, Bundle().apply {
                putParcelableArrayList(Constant.PARAM_DATA, ArrayList(mAdapter.data))
                putInt(Constant.PARAM_ORDER_ID, orderId)
                putDouble(Constant.PARAM_TOTAL_PRICE, totalPrice)
                putBoolean(Constant.PARAM_BOOLEAN, true)
                putString(Constant.PARAM_DESCRIPTION, cancel_reason)
                putStringArrayList(Constant.PARAM_SELECT_IMAGE, ArrayList(mImageAdapter.data))
            })

        }

        cancel_tv.setOnClickListener {
            presenter.cancelApplication(orderId)
        }
    }


    override fun bindDetail(data: RefundDetail) {

        if (data.status == 11) {
            data.shop?.let {
                address_ll.visibility = View.VISIBLE
                contact_name_tv.text = getString(R.string.contact_name_format, it.contact_name)
                contact_number_tv.text = getString(R.string.contact_number_format, it.contact_cell_phone)
            }
        } else {
            address_ll.visibility = View.GONE
        }

        if (data.status == 12) {
            bottom_rl.visibility = View.VISIBLE
        }

        mAdapter.setNewInstance(data.goods)
        val refundPrice = data.refund_price
        totalPrice = refundPrice.toDouble()
        refund_price_tv.text = getString(R.string.price_unit_format, refundPrice.toPlainString())

        cancel_time_tv.text = data.cancel_time
        trade_no_tv.text = data.trade_no.toString()

        val cancelReason = data.cancel_reason
        desc_tv.text = cancelReason
        cancel_reason = cancelReason
        mImageAdapter.setNewInstance(data.cancel_images)

    }

    override fun cancelSuccess() {

        // 取消成功,刷新列表和商城订单详情
        ToastUtils.showShort(getString(R.string.refund_cancel_request_success))
        EventBus.getDefault().post(EventCode.EVENT_REFRESH_ORDER_LIST)

        finish()

    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        when (baseEvent.code) {
            EVENT_REFRESH_ORDER_LIST -> {
                // 修改售后申请成功
                finish()
            }
            else -> {

            }
        }
    }


}