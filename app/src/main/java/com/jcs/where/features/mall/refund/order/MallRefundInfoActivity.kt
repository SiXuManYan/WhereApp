package com.jcs.where.features.mall.refund.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.mall.MallRefundInfo
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.refund.MallRefundEditActivity
import com.jcs.where.features.mall.refund.complaint.ComplaintActivity
import com.jcs.where.features.store.refund.image.RefundImage
import com.jcs.where.features.store.refund.image.StoreRefundAdapter2
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_mall_refund_order_detail.*
import kotlinx.android.synthetic.main.item_dishes_for_order_submit_mall.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/3/25 14:26.
 * 商品售后详情
 */
class MallRefundInfoActivity : BaseMvpActivity<MallRefundInfoPresenter>(), MallRefundInfoIVniew {

    /** 商品订单id */
    private var goodOrderId = 0

    /** 售后ID */
    private var refundId = 0

    override fun isStatusDark() = true

    private lateinit var mImageAdapter: StoreRefundAdapter2

    companion object {

        fun navigation(context: Context, goodOrderId: Int, refundId: Int) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ORDER_ID, goodOrderId)
                putInt(Constant.PARAM_REFUND_ID, refundId)
            }
            val intent = Intent(context, MallRefundInfoActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var alreadyComplaint = false


    /** 处理申诉 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            alreadyComplaint = true
            ToastUtils.showShort(R.string.complained_success)

        }
    }


    override fun getLayoutId() = R.layout.activity_mall_refund_order_detail

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        initExtra()
        initContent()
    }


    private fun initExtra() {
        intent.extras?.let {
            goodOrderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
            refundId = it.getInt(Constant.PARAM_REFUND_ID, 0)
        }
    }


    private fun initContent() {
        // 相册
        mImageAdapter = StoreRefundAdapter2()
        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        }

    }

    override fun initData() {
        presenter = MallRefundInfoPresenter(this)
        presenter.getData(goodOrderId, refundId)
    }

    override fun bindListener() {
        // 取消申请
        left_tv.setOnClickListener {

            AlertDialog.Builder(this@MallRefundInfoActivity)
                .setTitle(R.string.prompt)
                .setMessage(R.string.confirm_cancel_application)
                .setCancelable(false)
                .setPositiveButton(R.string.ensure) { dialogInterface, i ->
                    presenter.cancelRefund(refundId)
                    dialogInterface.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.dismiss() }
                .create().show()

        }

        // 修改申请
        right_tv.setOnClickListener {
            MallRefundEditActivity.navigation(this, goodOrderId, refundId, true)
        }

        // 申诉
        complaint_tv.setOnClickListener {
            if (alreadyComplaint) {
                ToastUtils.showShort(R.string.complained_success)
            }else {
                searchLauncher.launch(Intent(this, ComplaintActivity::class.java).putExtra(Constant.PARAM_ORDER_ID, goodOrderId))
            }
        }

    }


    override fun bindDetail(response: MallRefundInfo) {
        val goods = response.goods
        val goodStatus = goods.status
        status_tv.text = BusinessUtils.getMallGoodRefundStatusText(goodStatus)
        status_desc_tv.text = BusinessUtils.getMallGoodRefundStatusDescText(goodStatus)

        val moneyInfo = response.money_info
        price_tv.text = getString(R.string.price_unit_format, moneyInfo.refund_money)

        // 物流
        val addressInfo = response.address_info
        val name = addressInfo.name
        val tel = addressInfo.tel
        val address = addressInfo.address

        if (name.isNotBlank() || tel.isNotBlank() || address.isNotBlank()) {
            logistics_container_ll.visibility = View.VISIBLE

            logistics_info_tv.text = StringBuilder()
                .append(getString(R.string.contact_name_format, name)).append("\r\n")
                .append(getString(R.string.contact_number_format, tel)).append("\r\n")
                .append(getString(R.string.contact_address_format, address))
        } else {
            logistics_container_ll.visibility = View.GONE
        }

        // 售后商品
        GlideUtil.load(this, goods.good_image, order_image_iv, 4)
        good_name_tv.text = goods.good_title
        count_tv.text = getString(R.string.count_format, goods.good_num)
        now_price_tv.text = getString(R.string.price_unit_format, goods.good_price)

        // 售后信息
        response.money_info.let {
            amount_tv.text = getString(R.string.price_unit_format, it.refund_money)
            good_price_tv.text = getString(R.string.price_unit_format, it.goods_total)
            shop_offers_tv.text = getString(R.string.price_unit_format, it.shop_coupon_money)
            platform_offers_tv.text = getString(R.string.price_unit_format, it.shop_coupon_money)
            refund_time_tv.text = it.refund_time
            refund_number_tv.text = it.serial_number
        }

        // 售后原因
        desc_tv.text = response.cancel_reason
        val photos = ArrayList<RefundImage>()
        response.cancel_images.forEach {
            val apply = RefundImage().apply {
                type = RefundImage.TYPE_SHOW
                imageSource = it
            }
            photos.add(apply)

        }
        mImageAdapter.setNewInstance(photos)

        // 处理底部按钮
        when (goodStatus) {
            2 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.visibility = View.VISIBLE
                right_tv.visibility = View.GONE
            }
            4 -> {
                bottom_container_rl.visibility = View.VISIBLE
                left_tv.visibility = View.VISIBLE
                right_tv.visibility = View.VISIBLE
            }
            else -> {
                bottom_container_rl.visibility = View.GONE
            }
        }

        bottom_v.visibility = bottom_container_rl.visibility

        // 投诉
        if (goodStatus == 4 && response.complaint == 0) {
            complaint_tv.visibility = View.VISIBLE
        } else {
            complaint_tv.visibility = View.GONE
        }

        // 退款方式
        val refundMethod = response.remit_info
        refundMethod?.let {
            val bankChannel = BusinessUtils.isBankChannel(refundMethod.channel_name)
            if (bankChannel) {
                refund_name_tv.text = refundMethod.bank_all_name
            } else {
                refund_name_tv.text = refundMethod.channel_name
            }
            refund_user_name_tv.text = refundMethod.user_name
            refund_account_tv.text = refundMethod.account
            refund_method_ll.visibility = View.VISIBLE
        }

        // 退款失败
        if (goodStatus == 8) {
            fail_reason_rl.visibility = View.VISIBLE
            reason_split_v.visibility = View.VISIBLE
            fail_reason_tv.text =response.error_reason
        }else {
            logistics_container_ll.visibility = View.GONE
            reason_split_v.visibility = View.GONE
        }




    }

    override fun cancelSuccess() {
        // 刷新订单详情以及订单列表
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
        finish()
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }

        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                // 完成修改、提交售后，刷新页面
                presenter.getData(goodOrderId, refundId)
            }
            else -> {

            }
        }
    }


}


interface MallRefundInfoIVniew : BaseMvpView {
    fun bindDetail(response: MallRefundInfo)
    fun cancelSuccess()

}


class MallRefundInfoPresenter(private var view: MallRefundInfoIVniew) : BaseMvpPresenter(view) {


    fun getData(orderId: Int, refundId: Int) {
        requestApi(mRetrofit.mallRefundOrderDetail(orderId, refundId), object : BaseMvpObserver<MallRefundInfo>(view) {
            override fun onSuccess(response: MallRefundInfo) {
                view.bindDetail(response)
            }

        })
    }


    fun cancelRefund(refundId: Int) {
        requestApi(mRetrofit.cancelRefund(refundId), object : BaseMvpObserver<JsonElement>(view) {
            override fun onSuccess(response: JsonElement?) {
                view.cancelSuccess()
            }

        })

    }

}