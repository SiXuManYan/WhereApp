package com.jcs.where.features.gourmet.refund

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.RefundMethod
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.refund.MallRefundEditActivity
import com.jcs.where.features.refund.method.RefundMethodActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_refund_complex.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/5/10 15:44.
 * 美食外卖申请退款
 */
class ComplexRefundActivity : BaseMvpActivity<ComplexRefundPresenter>(), ComplexRefundView {


    /** 商品订单id */
    private var orderId = 0

    /** 打款id */
    private var remitId = 0


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refund_complex

    companion object {

        fun navigation(context: Context, orderId: Int, refundPrice: String, totalPrice: String) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ORDER_ID, orderId)
                putString(Constant.PARAM_REFUND_PRICE, refundPrice)
                putString(Constant.PARAM_TOTAL_PRICE, totalPrice)
            }
            val intent = Intent(context, MallRefundEditActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val bundle = it.data?.extras ?: return@registerForActivityResult
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                val refundMethod = bundle.getParcelable<RefundMethod>(Constant.PARAM_REFUND_METHOD)!!
                loadRefundMethod(refundMethod)
            }
        }

    }


    override fun initView() {
        initExtra()
    }

    private fun initExtra() {
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
            val refundPrice = it.getString(Constant.PARAM_REFUND_PRICE, "")
            val totalPrice = it.getString(Constant.PARAM_TOTAL_PRICE, "")

            refund_price_tv.text = StringUtils.getString(R.string.price_unit_format , refundPrice)
            total_price_tv.text = StringUtils.getString(R.string.price_unit_format , totalPrice)

        }
    }

    override fun initData() {
       presenter = ComplexRefundPresenter(this)
    }

    override fun bindListener() {

        refund_method_tv.setOnClickListener {
            launcher.launch(Intent(this, RefundMethodActivity::class.java)
                .putExtra(Constant.PARAM_HANDLE_SELECT, true))
        }
        confirm_tv.setOnClickListener {
            if (remitId == 0) {
                ToastUtils.showShort(R.string.please_add_refund_methods)
                return@setOnClickListener
            }
            presenter.refundOrder(orderId,remitId)
        }
    }


    private fun loadRefundMethod(refundMethod: RefundMethod) {
        remitId = refundMethod.id

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

    override fun refundSuccess() {
        ToastUtils.showShort(R.string.application_success)
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
        finish()
    }
}