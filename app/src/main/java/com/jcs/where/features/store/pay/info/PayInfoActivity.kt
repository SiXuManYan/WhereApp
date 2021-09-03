package com.jcs.where.features.store.pay.info

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.response.store.PayChannel
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.pay.result.StorePayResultActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_store_pay_info.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/6/23 17:11.
 *  支付信息
 *  商城支付。水电支付
 */
class PayInfoActivity : BaseMvpActivity<PayInfoPresenter>(), PayInfoView {

    private var totalPrice: Double = 0.00
    private var selectedChannel: PayChannel? = null
    private var orderIds = java.util.ArrayList<Int>()
    private var addressDialog: BottomSheetDialog? = null

    /**
     * 0 商城订单
     * 1 水电订单
     * 2 美食
     * 3 外卖
     * 4 酒店
     */
    private var useType = 0


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_store_pay_info

    override fun initView() {

        intent.extras?.let {
            totalPrice = it.getDouble(Constant.PARAM_TOTAL_PRICE, 0.0)
            selectedChannel = it.getSerializable(Constant.PARAM_DATA) as PayChannel

            val ids = it.getIntegerArrayList(Constant.PARAM_ORDER_IDS)
            if (!ids.isNullOrEmpty()) {
                orderIds.addAll(ids)
            }
            useType = it.getInt(Constant.PARAM_TYPE)
        }

        amount_tv.text = getString(R.string.price_unit_format, totalPrice.toString())
        selectedChannel?.let {
            payment_platform_tv.text = it.title
            payment_name_tv.text = it.card_account
            payment_account_number_tv.text = it.card_number
        }

        // 提交订单成功，刷新列表
        EventBus.getDefault().post(BaseEvent<Boolean>(EventCode.EVENT_ORDER_COMMIT_SUCCESS))
    }

    override fun initData() {
        presenter = PayInfoPresenter(this)

    }


    override fun bindListener() {

        paid_tv.setOnClickListener {
            showVerifyDialog()
        }
        copy_iv.setOnClickListener {
            ClipboardUtils.copyText(payment_name_tv.text.toString().trim())
            ToastUtils.showShort(getString(R.string.copy_successfully))
        }
        copy2_iv.setOnClickListener {
            ClipboardUtils.copyText(payment_account_number_tv.text.toString().trim())
            ToastUtils.showShort(getString(R.string.copy_successfully))
        }
    }


    private fun showVerifyDialog() {
        val addressDialog = BottomSheetDialog(this, R.style.bottom_sheet_edit)
        this.addressDialog = addressDialog
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_verify_paid, null)
        addressDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
        }
        view.findViewById<ImageView>(R.id.close_iv).setOnClickListener {
            addressDialog.dismiss()
        }

        val name_aet = view.findViewById<AppCompatEditText>(R.id.name_aet)
        val account_aet = view.findViewById<AppCompatEditText>(R.id.account_aet)

        val clear_name_iv = view.findViewById<ImageView>(R.id.clear_name_iv)
        val clear_account_iv = view.findViewById<ImageView>(R.id.clear_account_iv)

        name_aet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable) {
                val result = s.toString().trim()

                if (result.isEmpty()) {
                    clear_name_iv.visibility = View.VISIBLE
                }
            }
        })
        account_aet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable) {
                val result = s.toString().trim()

                if (result.isEmpty()) {
                    clear_account_iv.visibility = View.VISIBLE
                }
            }
        })

        clear_name_iv.setOnClickListener {
            name_aet.setText("")
        }

        clear_account_iv.setOnClickListener {
            name_aet.setText("")
        }


        view.findViewById<TextView>(R.id.add_new_address_tv).setOnClickListener {

            val accountName = name_aet.text.toString().trim()
            val accountNumber = account_aet.text.toString().trim()

            if (accountName.isEmpty() || accountNumber.isEmpty()) {
                return@setOnClickListener
            }


            when (useType) {
                Constant.PAY_INFO_ESTORE -> {
                    presenter.upLoadPayAccountInfo(orderIds, accountName, accountNumber, selectedChannel!!.id)
                }
                Constant.PAY_INFO_ESTORE_BILLS -> {
                    presenter.upLoadBillsPayAccountInfo(orderIds, accountName, accountNumber, selectedChannel!!.id)
                }
                Constant.PAY_INFO_FOOD -> {
                    presenter.upLoadFoodPayAccountInfo(orderIds, accountName, accountNumber, selectedChannel!!.id)
                }
                Constant.PAY_INFO_TAKEAWAY -> {
                    presenter.upLoadTakeawayPayAccountInfo(orderIds, accountName, accountNumber, selectedChannel!!.id)
                }
                Constant.PAY_INFO_HOTEL -> {
                    presenter.upLoadHotelPayAccountInfo(orderIds, accountName, accountNumber, selectedChannel!!.id)
                }
                else -> {
                }
            }


        }
        addressDialog.show()

    }


    override fun paySuccess() {
        startActivityAfterLogin(StorePayResultActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_TYPE, useType)
        })
        // 支付成功
        EventBus.getDefault().post(BaseEvent<Boolean>(EventCode.EVENT_REFRESH_ORDER_LIST))
        finish()
    }


}