package com.jcs.where.features.bills.channel

import android.view.View
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.bills.BillsChannel
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.form.BillsFormActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_no_refresh_list.*

/**
 * Created by Wangsw  2022/6/8 15:28.
 * 缴费渠道列表
 */
class BillsChannelActivity : BaseMvpActivity<BillsChannelPresenter>(), BillsChannelView, OnItemClickListener {


    /**
     * 账单类型（1-话费，2-水费，3-电费，4-网费）
     */
    private var type = 0

    private lateinit var mAdapter: BillsChannelAdapter

    override fun getLayoutId() = R.layout.activity_no_refresh_list


    override fun initView() {
        type = intent.getIntExtra(Constant.PARAM_TYPE, 0)

        val title = when (type) {
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

        val emptyView = EmptyView(this).apply {
            showEmptyDefault()
        }
        mAdapter = BillsChannelAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@BillsChannelActivity)
        }
        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f),
                SizeUtils.dp2px(15f),
                0))
        }

    }

    override fun initData() {
        presenter = BillsChannelPresenter(this)
        presenter.getData(type)
    }

    override fun bindListener() {

    }

    override fun bindData(response: ArrayList<BillsChannel>) {
        mAdapter.setNewInstance(response)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        if (!data.Status) {
            ToastUtils.showShort("Channel not available")
            return
        }
        BillsFormActivity.navigation(this, data.BillerTag,data.ServiceCharge.toDouble(), data.FieldDetails)
    }


}