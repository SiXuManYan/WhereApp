package com.jcs.where.features.bills.hydropower.pay

import android.os.Bundle
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.request.bills.BillsOrderCommit
import com.jcs.where.api.response.bills.BillsOrderInfo
import com.jcs.where.api.response.store.PayChannel
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.currency.WebViewActivity
import com.jcs.where.features.store.pay.StorePayAdapter
import com.jcs.where.features.store.pay.info.PayInfoActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_store_pay_bills.*

/**
 * Created by Wangsw  2021/6/29 16:40.
 *  水电支付
 *
 */
class BillsPayActivity : BaseMvpActivity<BillsPayPresenter>(), BillsPayView, OnItemClickListener {


    private lateinit var mAdapter: StorePayAdapter
    private var billsOrderCommit: BillsOrderCommit? = null
    private var selectedChannel: PayChannel? = null
    private var totalPrice: Double = 0.0

    override fun getLayoutId() = R.layout.activity_store_pay_bills

    override fun isStatusDark() = true

    override fun initView() {

        intent.extras?.let {
            billsOrderCommit = it.getSerializable(Constant.PARAM_DATA) as BillsOrderCommit
        }
        initPayChannel()
    }


    override fun onBackPressed() = onBackClick()


    private fun initPayChannel() {
        mAdapter = StorePayAdapter()
        mAdapter.setOnItemClickListener(this@BillsPayActivity)

        pay_channel_rv.apply {
            adapter = mAdapter
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerDecoration(getColor(R.color.colorPrimary), SizeUtils.dp2px(20f),
                    0, 0).apply { setDrawHeaderFooter(false) })
        }

        val string0 = getString(R.string.conditions_privacy_0)
        val string1 = getString(R.string.conditions_privacy_1)
        val string2 = getString(R.string.conditions_privacy_2)
        val string3 = getString(R.string.conditions_privacy_3)

        SpanUtils.with(rule_tv).append(string0)
                .append(string1)
                .setClickSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) =
                            WebViewActivity.goTo(this@BillsPayActivity, FeaturesUtil.getConditionAgreement())
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = getColor(R.color.blue_69AEF5);
                        ds.isUnderlineText = true
                    }

                })
                .append(string2)
                .append(string3)
                .setClickSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) =
                            WebViewActivity.goTo(this@BillsPayActivity, FeaturesUtil.getPrivacyPolicy())

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = getColor(R.color.blue_69AEF5);
                        ds.isUnderlineText = true
                    }

                }).create()
    }

    override fun initData() {
        presenter = BillsPayPresenter(this)
        presenter.getPayChannel()
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mAdapter.data.forEachIndexed { index, payChannel ->
            if (index == position) {
                payChannel.nativeSelected = true
                selectedChannel = payChannel
            } else {
                payChannel.nativeSelected = false
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun bindChannelData(response: ArrayList<PayChannel>) {
        mAdapter.setNewInstance(response)
    }


    override fun bindListener() {
        pay_tv.setOnClickListener {

            if (selectedChannel == null) {
                ToastUtils.showShort(R.string.select_pay_way)
                return@setOnClickListener
            }
            val totalAmount = amount_et.text.toString().trim()

            if (totalAmount.isEmpty()) {
                ToastUtils.showShort(R.string.payment_amount_empty)
                return@setOnClickListener
            }

            totalPrice = try {
                totalAmount.toDouble()
            } catch (e: Exception) {
                0.0
            }

            billsOrderCommit?.let {
                it.total_amount = totalAmount
                presenter.commitOrder(it)
            }

        }

        mJcsTitle.setBackIvClickListener {
            onBackClick()
        }
    }

    override fun commitOrderSuccess(response: BillsOrderInfo) {

        val ids = ArrayList<Int>().apply {
            add(response.order_id)
        }

        startActivityAfterLogin(PayInfoActivity::class.java, Bundle().apply {
            putDouble(Constant.PARAM_TOTAL_PRICE, totalPrice)
            putSerializable(Constant.PARAM_DATA, selectedChannel)
            putIntegerArrayList(Constant.PARAM_ORDER_IDS, ids)
            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_ESTORE_BILLS)
        })
        finish()
    }


    private fun onBackClick() {
        AlertDialog.Builder(this)
                .setTitle(R.string.prompt)
                .setMessage(R.string.give_up_payment_hint)
                .setCancelable(false)
                .setPositiveButton(R.string.continue_to_pay) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .setNegativeButton(R.string.give_up) { dialogInterface, i ->
                    dialogInterface.dismiss()
                    finish()
                }
                .create().show()
    }


}