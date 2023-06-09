package com.jiechengsheng.city.features.integral.place

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
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.address.AddressResponse
import com.jiechengsheng.city.api.response.integral.IntegralPlaceOrderResponse
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.address.AddressActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_integral_place_order.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by Wangsw  2022/9/23 14:21.
 * 优惠券下单
 */
class IntegralOrderActivity : BaseMvpActivity<IntegralOrderPresenter>(), IntegralOrderView {


    var goodId = 0
    var title = ""
    var image = ""
    var price = ""

    /**   1：商品 其他：优惠券  */
    var couponType = 0

    /** 收货地址 */
    var mSelectAddressData: AddressResponse? = null

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_integral_place_order


    companion object {

        /**
         *       1：商品 其他：优惠券
         */
        fun navigation(
            context: Context, goodId: Int, title: String,
            image: String, price: String, couponType: Int,
        ) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_ID, goodId)
                putString(Constant.PARAM_TITLE, title)
                putString(Constant.PARAM_IMAGE, image)
                putString(Constant.PARAM_PRICE, price)
                putInt(Constant.PARAM_TYPE, couponType)
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
        BarUtils.setStatusBarColor(this, Color.WHITE)
        initExtra()
    }

    private fun initExtra() {
        intent.extras?.let {
            goodId = it.getInt(Constant.PARAM_ID)
            couponType = it.getInt(Constant.PARAM_TYPE)
            title = it.getString(Constant.PARAM_TITLE, "")
            image = it.getString(Constant.PARAM_IMAGE, "")
            price = it.getString(Constant.PARAM_PRICE, "")
        }

        if (couponType == 1) {
            address_ll.visibility = View.VISIBLE
        } else {
            address_ll.visibility = View.GONE
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

            if (couponType == 1 && mSelectAddressData == null) {
                ToastUtils.showShort(R.string.choose_shipping_address)
                return@setOnClickListener
            }
            showDialog()
        }
    }

    override fun submitSuccess(response: IntegralPlaceOrderResponse) {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_INTEGRAL))
        finish()
    }


    private fun showDialog() {

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

            // 更改默认宽度
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(window.attributes)
            lp.width = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(80f)
            window.attributes = lp
        }
        cancelTv.setOnClickListener {
            alertDialog.dismiss()
        }

        confirmTv.setOnClickListener {

            var addressId: String? = null
            mSelectAddressData?.let {
                addressId = it.id
            }

            presenter.makeOrder(goodId, addressId)
            alertDialog.dismiss()
        }

    }

}