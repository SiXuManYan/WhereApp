package com.jcs.where.features.mall.refund.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.order.store.RefundDetailMall
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.order.MallOrderDetailAdapter
import com.jcs.where.features.mall.refund.apply.MallRefundActivity
import com.jcs.where.features.store.refund.StoreRefundAdapter
import com.jcs.where.features.store.refund.detail.MallRefundDetailPresenter
import com.jcs.where.features.store.refund.detail.MallRefundDetailView
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_store_refund_detail.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/12/17 19:38.
 * 新版商城售后详情
 */

class MallRefundDetailActivity : BaseMvpActivity<MallRefundDetailPresenter>(), MallRefundDetailView {

    private var orderId = 0
    private var totalPrice = 0.0
    private var cancel_reason = ""
    private lateinit var mAdapter: MallOrderDetailAdapter
    private lateinit var mImageAdapter: StoreRefundAdapter

    override fun getLayoutId() = R.layout.activity_store_refund_detail

    override fun isStatusDark() = true

    override fun initView() {

        BarUtils.setStatusBarColor(this, Color.WHITE)
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID)
        }

        // 商品信息
        mAdapter = MallOrderDetailAdapter()
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
        presenter = MallRefundDetailPresenter(this)
        presenter.getDetail(orderId)

    }

    override fun bindListener() {

        change_tv.setOnClickListener {

            startActivity(MallRefundActivity::class.java, Bundle().apply {
                putSerializable(Constant.PARAM_DATA, ArrayList(mAdapter.data))
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


    override fun bindDetail(data: RefundDetailMall) {

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
            bottom_v.visibility = View.VISIBLE
        }

        mAdapter.setNewInstance(data.goods)
        val refundPrice = data.refund_price
        totalPrice = refundPrice.toDouble()
        amount_tv.text = getString(R.string.price_unit_format, refundPrice.toPlainString())

        cancel_time_tv.text = data.cancel_time
        trade_no_tv.text = data.trade_no

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
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                // 修改售后申请成功
                finish()
            }
            else -> {

            }
        }
    }


}