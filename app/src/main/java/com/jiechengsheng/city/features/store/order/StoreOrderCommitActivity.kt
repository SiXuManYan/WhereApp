package com.jiechengsheng.city.features.store.order

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.address.AddressResponse
import com.jiechengsheng.city.api.response.store.StoreOrderCommitData
import com.jiechengsheng.city.api.response.store.StoreOrderInfoResponse
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.address.AddressActivity
import com.jiechengsheng.city.features.payment.WebPayActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_store_order_commit.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/21 10:26.
 *  商城提交订单
 */
class StoreOrderCommitActivity : BaseMvpActivity<StoreOrderCommitPresenter>(), StoreOrderCommitView {


    private var totalPrice: BigDecimal = BigDecimal.ZERO


    private var data: ArrayList<StoreOrderCommitData> = ArrayList()

    /** 收货地址 */
    var mSelectAddressData: AddressResponse? = null

    private lateinit var mAdapter: StoreOrderCommitAdapter


    /** 处理选择地址 */
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bundle = it.data?.extras ?: return@registerForActivityResult

            val selectData = bundle.getSerializable(Constant.PARAM_DATA) as AddressResponse
            mSelectAddressData = selectData
            select_address_tv.text = selectData.address
            address_value_ll.visibility = ViewGroup.VISIBLE
            val sex = if (selectData.sex == 1) {
                getString(R.string.sir)
            } else {
                getString(R.string.lady)
            }
            address_name_tv.text = getString(R.string.recipient_format, selectData.contact_name, sex, selectData.contact_number)
            phone_tv.text = selectData.contact_number
        }
    }


    override fun getLayoutId() = R.layout.activity_store_order_commit

    override fun initView() {

        val bundle = intent.extras
        bundle?.let {
            data = it.getSerializable(Constant.PARAM_ORDER_COMMIT_DATA) as ArrayList<StoreOrderCommitData>
        }

        initContent()

    }

    private fun initContent() {
        mAdapter = StoreOrderCommitAdapter()
        content_rv.apply {
            adapter = mAdapter
            addItemDecoration(
                DividerDecoration(
                    ColorUtils.getColor(R.color.white),
                    SizeUtils.dp2px(10f), 0, 0
                )
            )
        }
        if (data.isNotEmpty()) {
            mAdapter.setNewInstance(data)
            if (data[0].delivery_type == 1) {
                delivery_value_tv.text = getString(R.string.self_extraction)
                address_ll.visibility = View.GONE
                phone_rl.visibility = View.VISIBLE
            } else {
                delivery_value_tv.text = getString(R.string.express)
                address_ll.visibility = View.VISIBLE
                phone_rl.visibility = View.GONE
            }
        }


    }


    override fun isStatusDark() = true

    override fun initData() {
        presenter = StoreOrderCommitPresenter(this)
        totalPrice = presenter.handlePrice(mAdapter)
        total_price_tv.text = getString(R.string.price_unit_format, totalPrice.toPlainString())
    }


    override fun bindListener() {
        address_ll.setOnClickListener {
            searchLauncher.launch(Intent(this, AddressActivity::class.java).putExtra(Constant.PARAM_HANDLE_SELECT, true))
        }

        submit_tv.setOnClickListener {

            if (data.isNotEmpty()) {
                val phone = phone_aet.text.toString().trim()

                if (data[0].delivery_type == 1 && phone.isEmpty()) {
                    ToastUtils.showShort(getString(R.string.input_phone_empty_hint))
                    return@setOnClickListener
                }

                if (data[0].delivery_type == 2 && mSelectAddressData == null) {
                    ToastUtils.showShort(R.string.address_edit_hint)
                    return@setOnClickListener
                }
                presenter.orderCommit(data, mSelectAddressData?.id, phone)

            }


        }
    }

    override fun commitSuccess(response: StoreOrderInfoResponse) {

        val orderIds = ArrayList<Int>()
        response.orders.forEach {
            orderIds.add(it.id)
        }

//        startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
//            putDouble(Constant.PARAM_TOTAL_PRICE, response.total_price.toDouble())
//            putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
//            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_ESTORE)
//        })

        WebPayActivity.navigation(this, Constant.PAY_INFO_ESTORE, orderIds)
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        val code = baseEvent.code
        when (code) {
            EventCode.EVENT_CANCEL_PAY -> finish()
            else -> {

            }
        }
    }


}