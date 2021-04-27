package com.jcs.where.features.gourmet.takeaway.submit

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.gourmet.dish.DishTakeawayResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.address.AddressAdapter
import com.jcs.where.features.address.edit.AddressEditActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_order_submit_takeaway.*

/**
 * Created by Wangsw  2021/4/26 17:54.
 * 外卖订单提交页
 */
class OrderSubmitTakeawayActivity : BaseMvpActivity<OrderSubmitTakeawayPresenter>(), OrderSubmitTakeawayView {


    /** 餐厅id */
    private var restaurant_id: String = "0"

    /** 包装费 */
    private var packing_charges: String = "0"

    /** 配送费 */
    private var delivery_cost: String = "0"

    /** 总金额 */
    private var total_price: String = "0"

    /** 商家名称 */
    private var restaurant_name: String = ""

    /** 菜品列表 */
    private lateinit var dish_list: ArrayList<DishTakeawayResponse>

    var addressDialog: BottomSheetDialog? = null
    var mSelectAddressData: AddressResponse? = null

    private lateinit var mAdapter: OrderSubmitTakeawayAdapter

    private lateinit var mAddressAdapter: AddressAdapter

    override fun getLayoutId() = R.layout.activity_order_submit_takeaway

    override fun isStatusDark() = true

    override fun initView() {

        BarUtils.setStatusBarColor(this, Color.WHITE)
        // extra
        val bundle = intent.extras ?: return
        restaurant_id = bundle.getString(Constant.PARAM_ID, "0")
        packing_charges = bundle.getString(Constant.PARAM_PACKING_CHARGES, "0")
        delivery_cost = bundle.getString(Constant.PARAM_DELIVERY_COST, "0")
        total_price = bundle.getString(Constant.PARAM_TOTAL_PRICE, "0")
        restaurant_name = bundle.getString(Constant.PARAM_NAME, "0")
        dish_list = bundle.getSerializable(Constant.PARAM_DATA) as ArrayList<DishTakeawayResponse>

        // list
        initRecyclerView()
        business_name_tv.text = restaurant_name
        packing_charges_tv.text = getString(R.string.price_unit_format, packing_charges)
        delivery_cost_tv.text = getString(R.string.price_unit_format, delivery_cost)
        val total = getString(R.string.price_unit_format, total_price)
        actual_payment_tv.text = total
        total_price_tv.text = total

    }

    private fun initRecyclerView() {
        mAdapter = OrderSubmitTakeawayAdapter().apply {
            setEmptyView(EmptyView(this@OrderSubmitTakeawayActivity).apply {
                showEmptyDefault()
            })
        }

        dish_rv.apply {
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(this@OrderSubmitTakeawayActivity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(10f),
                    0, 0).apply { setDrawHeaderFooter(true) })
            adapter = mAdapter
        }

        mAddressAdapter = AddressAdapter().apply {
            addChildClickViewIds(R.id.edit_iv)


            setOnItemClickListener { adapter, view, position ->

                val selectData = mAddressAdapter.data[position]
                mSelectAddressData = selectData
                select_address_tv.visibility = ViewGroup.GONE
                address_rl.visibility = ViewGroup.VISIBLE
                address_tv.text = selectData.address

                val sex = if (selectData.sex == 1) {
                    getString(R.string.sir)
                } else {
                    getString(R.string.lady)
                }

                name_tv.text = getString(R.string.recipient_format, selectData.contact_name, sex, selectData.contact_number)
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

    override fun initData() {
        mAdapter.setNewInstance(dish_list)
        presenter = OrderSubmitTakeawayPresenter(this)
        presenter.getAddress()

    }

    override fun bindListener() {
        select_address_tv.setOnClickListener {
            showAddress()

        }

    }


    override fun bindAddress(toMutableList: MutableList<AddressResponse>) {
        mAddressAdapter.setNewInstance(toMutableList)
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

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        if (baseEvent.code == EventCode.EVENT_ADDRESS) {
            presenter.getAddress()
        }
    }

}