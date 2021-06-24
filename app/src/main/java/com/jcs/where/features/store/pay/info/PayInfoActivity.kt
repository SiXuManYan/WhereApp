package com.jcs.where.features.store.pay.info

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.response.store.PayChannel
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_store_pay_info.*

/**
 * Created by Wangsw  2021/6/23 17:11.
 *  支付信息
 */
class PayInfoActivity : BaseMvpActivity<PayInfoPresenter>(), PayInfoView {

    private var totalPrice: Double = 0.00
    private var selectedChannel: PayChannel? = null
    private var orderIds = java.util.ArrayList<Int>()
    private var addressDialog: BottomSheetDialog? = null

    override fun getLayoutId() = R.layout.activity_store_pay_info

    override fun initView() {

        intent.extras?.let {
            totalPrice = it.getDouble(Constant.PARAM_TOTAL_PRICE, 0.0)
            selectedChannel = it.getSerializable(Constant.PARAM_DATA) as PayChannel
            val ids = it.getIntegerArrayList(Constant.PARAM_ORDER_IDS)
            if (!ids.isNullOrEmpty()) {
                orderIds.addAll(ids)
            }
        }
    }

    override fun initData() {

        presenter = PayInfoPresenter(this)
        amount_tv.text = getString(R.string.price_unit_format, totalPrice.toString())
        selectedChannel?.let {
            payment_platform_tv.text = it.title
            payment_name_tv.text = it.card_account
            payment_account_number_tv.text = it.card_account
        }
    }

    override fun bindListener() {
        paid_tv.setOnClickListener {


        }
    }


    private fun showVerifyDialog() {
        val addressDialog = BottomSheetDialog(this)
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
        view.findViewById<TextView>(R.id.add_new_address_tv).setOnClickListener {

            val accountName = name_aet.text.toString().trim()
            val accountNumber = account_aet.text.toString().trim()

            if (accountName.isEmpty() || accountNumber.isEmpty()) {
                return@setOnClickListener
            }

            presenter.upLoadPayAccountInfo(orderIds, accountName, accountNumber, selectedChannel!!.id)

        }
        addressDialog.show()

    }


    override fun paySuccess() {


    }


}