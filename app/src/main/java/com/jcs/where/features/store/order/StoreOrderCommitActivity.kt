package com.jcs.where.features.store.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.store.StoreOrderCommitData
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.address.AddressAdapter
import com.jcs.where.features.address.edit.AddressEditActivity
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_store_order_commit.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/6/21 10:26.
 *  商城提交订单
 */
class StoreOrderCommitActivity : BaseMvpActivity<StoreOrderCommitPresenter>(), StoreOrderCommitView {


    private var totalPrice: BigDecimal = BigDecimal.ZERO

    private var addressDialog: BottomSheetDialog? = null

    private var data: StoreOrderCommitData? = null

    /** 收货地址 */
    var mSelectAddressData: AddressResponse? = null

    private lateinit var mAdapter: StoreOrderCommitAdapter

    private lateinit var mAddressAdapter: AddressAdapter


    override fun getLayoutId() = R.layout.activity_store_order_commit

    override fun initView() {

        val bundle = intent.extras
        bundle?.let {
            data = it.getSerializable(Constant.PARAM_ORDER_COMMIT_DATA) as StoreOrderCommitData
        }

        initAddress()



        mAdapter = StoreOrderCommitAdapter()
        content_rv.apply {
            adapter = mAdapter
            addItemDecoration(
                    DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                            SizeUtils.dp2px(10f), 0, 0).apply { setDrawHeaderFooter(false) }
            )
        }

        data?.let {
            mAdapter.addData(it)
        }
    }

    private fun initAddress() {
        mAddressAdapter = AddressAdapter()

        // 收货地址
        mAddressAdapter = AddressAdapter().apply {
            addChildClickViewIds(R.id.edit_iv)
            setOnItemClickListener { adapter, view, position ->
                val selectData = mAddressAdapter.data[position]
                mSelectAddressData = selectData

                val sex = if (selectData.sex == 1) {
                    getString(R.string.sir)
                } else {
                    getString(R.string.lady)
                }
                address_tv.text = getString(R.string.recipient_format, selectData.contact_name, sex, selectData.contact_number)
                addressDialog?.dismiss()
            }
            setOnItemChildClickListener { _, view, position ->
                val data: AddressResponse = mAddressAdapter.data[position]
                if (view.id == R.id.edit_iv) {
                    val bundle = Bundle()
                    bundle.putString(Constant.PARAM_ADDRESS_ID, data.id)
                    bundle.putInt(Constant.PARAM_SEX, data.sex)
                    bundle.putString(Constant.PARAM_ADDRESS, data.address)
                    bundle.putString(Constant.PARAM_RECIPIENT, data.contact_name)
                    bundle.putString(Constant.PARAM_PHONE, data.contact_number)
                    startActivity(AddressEditActivity::class.java, bundle)
                    addressDialog?.dismiss()
                }
            }
        }
    }

    override fun isStatusDark() = true

    override fun initData() {
        presenter = StoreOrderCommitPresenter(this)
        presenter.getAddress()
        totalPrice = presenter.handlePrice(mAdapter)
        total_price_tv.text = getString(R.string.price_unit_format, totalPrice.toPlainString())
    }


    override fun bindListener() {
        address_rl.setOnClickListener {
            showAddress()
        }
    }


    private fun showAddress() {
        val addressDialog = BottomSheetDialog(this)
        this.addressDialog = addressDialog
        val view = LayoutInflater.from(this).inflate(R.layout.layout_select_address, null)
        addressDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val recycler_view = view.findViewById<RecyclerView>(R.id.recycler_view)
        recycler_view.adapter = mAddressAdapter

        view.findViewById<ImageView>(R.id.close_iv).setOnClickListener {
            addressDialog.dismiss()
        }

        val add_new_address_tv = view.findViewById<TextView>(R.id.add_new_address_tv)
        add_new_address_tv.setOnClickListener {
            startActivity(AddressEditActivity::class.java)
            addressDialog.dismiss()
        }
        addressDialog.show()
    }


    override fun bindAddress(toMutableList: MutableList<AddressResponse>) {
        mAddressAdapter.setNewInstance(toMutableList)
    }

}