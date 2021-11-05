package com.jcs.where.features.gourmet.takeaway.submit

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.jcs.where.R
import com.jcs.where.api.response.address.AddressResponse
import com.jcs.where.api.response.gourmet.dish.DishResponse
import com.jcs.where.api.response.gourmet.order.TakeawayOrderSubmitData
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.bean.OrderSubmitChildRequest
import com.jcs.where.bean.OrderSubmitTakeawayRequest
import com.jcs.where.features.address.AddressActivity
import com.jcs.where.features.store.pay.StorePayActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.time.TimeUtil
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_order_submit_takeaway.*

/**
 * Created by Wangsw  2021/4/26 17:54.
 * 外卖订单提交页
 */
class OrderSubmitTakeawayActivity : BaseMvpActivity<OrderSubmitTakeawayPresenter>(), OrderSubmitTakeawayView {


    /** 餐厅id */
    private var mRestaurantId: String = "0"

    /** 包装费 */
    private var packing_charges: String = "0"

    /** 配送费 */
    private var delivery_cost: String = "0"

    /** 总金额 */
    private var total_price: String = "0"

    /** 商家名称 */
    private var restaurant_name: String = ""

    /** 菜品列表 */
    private lateinit var dish_list: ArrayList<DishResponse>

    var addressDialog: BottomSheetDialog? = null
    var timeDialog: BottomSheetDialog? = null

    /** 收货地址 */
    var mSelectAddressData: AddressResponse? = null

    /** 配送时间类型（1：立即配送，2：选定时间） */
    private var mDeliveryTimeType = 0

    /** 配送时间 */
    private var mDeliveryTime = ""


    private lateinit var mAdapter: OrderSubmitTakeawayAdapter

    private lateinit var mTimeAdapter: TimeAdapter

    override fun getLayoutId() = R.layout.activity_order_submit_takeaway

    override fun isStatusDark() = true

    override fun initView() {

        BarUtils.setStatusBarColor(this, Color.WHITE)
        // extra
        val bundle = intent.extras ?: return
        mRestaurantId = bundle.getString(Constant.PARAM_ID, "0")
        packing_charges = bundle.getString(Constant.PARAM_PACKING_CHARGES, "0")
        delivery_cost = bundle.getString(Constant.PARAM_DELIVERY_COST, "0")
        total_price = bundle.getString(Constant.PARAM_TOTAL_PRICE, "0")
        restaurant_name = bundle.getString(Constant.PARAM_NAME, "0")
        dish_list = bundle.getSerializable(Constant.PARAM_DATA) as ArrayList<DishResponse>

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
            addItemDecoration(DividerDecoration(
                Color.WHITE, SizeUtils.dp2px(10f),
                0, 0
            ).apply { setDrawHeaderFooter(true) })
            adapter = mAdapter
        }


        // 送达时间
        mTimeAdapter = TimeAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val time = mTimeAdapter.data[position]

                mDeliveryTimeType = if (position == 0) {
                    1
                } else {
                    2
                }
                mDeliveryTime = time
                delivery_time_tv.text = getString(R.string.delivery_time_format2, TimeUtil.getFormatTimeHM(time))
                timeDialog?.dismiss()
            }

        }

    }

    override fun initData() {
        mAdapter.setNewInstance(dish_list)
        presenter = OrderSubmitTakeawayPresenter(this)
        presenter.getTimeList(mRestaurantId)

    }

    override fun bindListener() {
        select_address_rl.setOnClickListener {
            showAddress()
        }
        time_tv.setOnClickListener {
            showTime()
        }

        buy_after_tv.setOnClickListener {
            if (mSelectAddressData == null) {
                ToastUtils.showShort(R.string.choose_shipping_address)
                return@setOnClickListener
            }
            if (mDeliveryTime.isEmpty()) {
                ToastUtils.showShort(getString(R.string.please_select_delivery_time))
                return@setOnClickListener
            }

            val goodIds = ArrayList<OrderSubmitChildRequest>()

            mAdapter.data.forEach {
                val apply = OrderSubmitChildRequest().apply {
                    good_id = it.id
                    good_num = it.nativeSelectCount
                }
                goodIds.add(apply)
            }

            val apply = OrderSubmitTakeawayRequest().apply {
                goods = Gson().toJson(goodIds)
                address_id = mSelectAddressData!!.id
                restaurant_id = mRestaurantId
                delivery_time_type = mDeliveryTimeType
                delivery_time = mDeliveryTime
                remark = remarks_et.text.toString().trim()
            }

            presenter.submitOrder(apply)


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


    private fun showAddress() {
        searchLauncher.launch(Intent(this, AddressActivity::class.java).putExtra(Constant.PARAM_HANDLE_ADDRESS_SELECT, true))
    }

    override fun bindTime(otherTimes: java.util.ArrayList<String>) {
        mTimeAdapter.setNewInstance(otherTimes)
    }


    private fun showTime() {
        val timeDialog = BottomSheetDialog(this)
        this.timeDialog = timeDialog
        val view = LayoutInflater.from(this).inflate(R.layout.layout_select_time, null)
        timeDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val recycler_view = view.findViewById<RecyclerView>(R.id.recycler_view)
        recycler_view.adapter = mTimeAdapter

        view.findViewById<ImageView>(R.id.close_iv).setOnClickListener {
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    override fun submitSuccess(response: TakeawayOrderSubmitData) {

        val orderIds = ArrayList<Int>()
        orderIds.add(response.order.id)

        startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
            putDouble(Constant.PARAM_TOTAL_PRICE, total_price.toDouble())
            putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_TAKEAWAY)
        })

        finish()
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_CANCEL_PAY -> finish()
        }
    }


}