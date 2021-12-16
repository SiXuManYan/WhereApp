package com.jcs.where.features.mall.buy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.api.response.mall.request.MallCommitResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.address.AddressActivity
import com.jcs.where.features.store.pay.StorePayActivity
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_mall_order_commit.*


import java.math.BigDecimal

/**
 * Created by Wangsw  2021/12/15 13:56.
 * 商城下单
 */
class MallOrderCommitActivity : BaseMvpActivity<MallOrderCommitPresenter>(), MallOrderCommitView {


    private var totalPrice: BigDecimal = BigDecimal.ZERO

    private var data: ArrayList<MallCartGroup> = ArrayList()


    /** 收货地址 */
    var mSelectAddressData: AddressResponse? = null

    private lateinit var mAdapter: MallOrderCommitAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_order_commit

    override fun initView() {
        val bundle = intent.extras
        bundle?.let {
            data = it.getSerializable(Constant.PARAM_DATA) as ArrayList<MallCartGroup>
        }
        initContent()
    }

    private fun initContent() {
        mAdapter = MallOrderCommitAdapter()
        content_rv.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.white), SizeUtils.dp2px(10f), 0, 0))
        }
        mAdapter.setNewInstance(data)
    }

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


    override fun initData() {
        presenter = MallOrderCommitPresenter(this)
         totalPrice = presenter.handlePrice(mAdapter)
        total_price_tv.text = getString(R.string.price_unit_format, totalPrice.toPlainString())
    }

    override fun bindListener() {
        address_ll.setOnClickListener {
            searchLauncher.launch(Intent(this, AddressActivity::class.java).putExtra(Constant.PARAM_HANDLE_ADDRESS_SELECT, true))
        }
        submit_tv.setOnClickListener {
            if (mSelectAddressData == null) {
                ToastUtils.showShort(R.string.address_edit_hint)
                return@setOnClickListener
            }
            presenter.orderCommit(data, mSelectAddressData?.id)
        }
    }

    override fun commitSuccess(response: MallCommitResponse) {

        startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
            putDouble(Constant.PARAM_TOTAL_PRICE, response.total_price.toDouble())
            putIntegerArrayList(Constant.PARAM_ORDER_IDS, response.orders)
            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_MALL)
        })
    }

}