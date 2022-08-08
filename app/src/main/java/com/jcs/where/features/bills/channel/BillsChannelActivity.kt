package com.jcs.where.features.bills.channel

import android.view.View
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.bills.BillsChannel
import com.jcs.where.api.response.bills.CallChargeChannel
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.charges.CallChargesActivity
import com.jcs.where.features.bills.form.BillsFormActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_no_refresh_list.*

/**
 * Created by Wangsw  2022/6/8 15:28.
 * 水电网费缴费渠道列表
 */
class BillsChannelActivity : BaseMvpActivity<BillsChannelPresenter>(), BillsChannelView, OnItemClickListener {


    /**
     * 账单类型（1-话费，2-水费，3-电费，4-网费）
     */
    private var billsType = 0


    /** 水电网费 */
    private lateinit var mCommonAdapter: BillsChannelAdapter

    /** 话费 */
    private lateinit var mCallAdapter: CallChargesChannelAdapter

    private lateinit var emptyView:EmptyView

    override fun getLayoutId() = R.layout.activity_no_refresh_list

    override fun isStatusDark() = true

    override fun initView() {
        billsType = intent.getIntExtra(Constant.PARAM_TYPE, 0)

        val title = when (billsType) {
            1 -> getString(R.string.prepaid_reload)
            2 -> getString(R.string.water_utilities)
            3 -> getString(R.string.electric_utilities)
            4 -> getString(R.string.internet_billing)
            else -> ""
        }
        mJcsTitle.setMiddleTitle(title)
        initContent()
    }

    private fun initContent() {

        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            addEmptyList(this)
        }

        mCommonAdapter = BillsChannelAdapter().apply {
            setOnItemClickListener(this@BillsChannelActivity)
        }

        mCallAdapter = CallChargesChannelAdapter().apply {
            setOnItemClickListener(this@BillsChannelActivity)
        }

        recycler.apply {

            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f),
                SizeUtils.dp2px(15f),
                0))
        }

        if (billsType == 1) {
            recycler.adapter = mCallAdapter
            mCallAdapter.setEmptyView(emptyView)

        } else {
            recycler. adapter = mCommonAdapter
            mCommonAdapter.setEmptyView(emptyView)
        }
    }

    override fun initData() {
        presenter = BillsChannelPresenter(this)
        presenter.getData(billsType)
    }

    override fun bindListener() {

    }

    override fun bindCommonData(response: ArrayList<BillsChannel>) {
        mCommonAdapter.setNewInstance(response)
        if (response.isNullOrEmpty()) {
            emptyView.showEmptyContainer()
        }
    }

    override fun bindCallData(response: ArrayList<CallChargeChannel>) {
        mCallAdapter.setNewInstance(response)
        if (response.isNullOrEmpty()) {
            emptyView.showEmptyContainer()
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        if (billsType == 1) {

            val data = mCallAdapter.data[position]
            CallChargesActivity.navigation(this, data)
        } else {

            val data = mCommonAdapter.data[position]
            if (!data.Status) {
                ToastUtils.showShort("Channel not available")
                return
            }
            BillsFormActivity.navigation(this,
                data.BillerTag, data.Description,
                data.ServiceCharge.toDouble(), data.FieldDetails,
                billsType)
        }


    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                finish()
            }
        }

    }


}