package com.jcs.where.features.integral.place

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.integral.IntegralPlaceOrderResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.address.AddressActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_integral_place_order.*


/**
 * Created by Wangsw  2022/9/23 14:21.
 * 优惠券下单
 */
class IntegralOrderActivity : BaseMvpActivity<IntegralOrderPresenter>(), IntegralOrderView {


    var goodId = 0
    var title = ""
    var image = ""
    var price = ""

    /** 收货地址 */
    var mSelectAddressData: AddressResponse? = null

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_integral_place_order


    companion object {
        fun navigation(
            context: Context, goodId: Int, title: String,
            image: String, price: String,
        ) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, goodId)
                putString(Constant.PARAM_TITLE, title)
                putString(Constant.PARAM_IMAGE, image)
                putString(Constant.PARAM_PRICE, price)
            }
            val intent = Intent(context, IntegralOrderActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    /** 处理选择地址 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bundle = it.data?.extras ?: return@registerForActivityResult

            val selectData = bundle.getSerializable(Constant.PARAM_DATA) as AddressResponse
            mSelectAddressData = selectData
            select_address_tv.text = selectData.address
            address_rl.visibility = ViewGroup.VISIBLE
            val sex = if (selectData.sex == 1) {
                getString(R.string.sir)
            } else {
                getString(R.string.lady)
            }
            address_name_tv.text = getString(R.string.recipient_format, selectData.contact_name, sex, selectData.contact_number)
            phone_tv.text = selectData.contact_number
        }
    }


    override fun initView() {
        initExtra()

    }

    private fun initExtra() {
        intent.extras?.let {
            goodId = it.getInt(Constant.PARAM_ID)
            title = it.getString(Constant.PARAM_TITLE, "")
            image = it.getString(Constant.PARAM_IMAGE, "")
            price = it.getString(Constant.PARAM_PRICE, "")
        }
    }

    override fun initData() {
        presenter = IntegralOrderPresenter(this)

        GlideUtil.load(this, image, good_iv)
        good_name_tv.text = title
        payable_tv.text = getString(R.string.points_format, price)

    }

    override fun bindListener() {
        select_address_rl.setOnClickListener {
            searchLauncher.launch(Intent(this, AddressActivity::class.java).putExtra(Constant.PARAM_HANDLE_SELECT, true))
        }

        confirm_tv.setOnClickListener {
            if (mSelectAddressData == null) {
                ToastUtils.showShort(R.string.choose_shipping_address)
                return@setOnClickListener
            }
            showDialog()
        }
    }

    override fun submitSuccess(response: IntegralPlaceOrderResponse) {
        ToastUtils.showShort("pay success ")
        finish()
    }


    private fun showDialog(){

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.dialog_integral_place_order, null)

        val cancelTv = view.findViewById<TextView>(R.id.cancel_tv)
        val confirmTv = view.findViewById<TextView>(R.id.confirm_tv)

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        val window: Window? = alertDialog.window
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setContentView(view)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        }
        cancelTv.setOnClickListener {
            alertDialog.dismiss()
        }

        confirmTv.setOnClickListener {
            presenter.makeOrder(goodId, mSelectAddressData!!.id)
            alertDialog.dismiss()
        }





    }

}