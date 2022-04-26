package com.jcs.where.features.refund.add.channel

import android.annotation.SuppressLint
import android.content.Intent
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.RefundChannel
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.refund.add.form.bank.BankChannelFormActivity
import com.jcs.where.features.refund.add.form.third.ThirdChannelFormActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_refund_channel_selected.*

/**
 * Created by Wangsw  2022/4/25 19:03.
 * 退款渠道
 */
class RefundChannelActivity : BaseMvpActivity<RefundChannelPresenter>(), RefundChannelView {

    private var isBankChannel = false

    private var selectedChannel = ""
    private lateinit var mAdapter: RefundChannelAdapter
    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refund_channel_selected

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        mAdapter = RefundChannelAdapter().apply {

            setOnItemClickListener { _, _, position ->
                selectedChannel = mAdapter.data[position].name
                isBankChannel = position == mAdapter.data.size - 1 || selectedChannel == "BANK"

                mAdapter.data.forEachIndexed { index, refundChannel ->
                    refundChannel.isSelected = index == position
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
        channel_rv.adapter = mAdapter
    }

    override fun initData() {
        presenter = RefundChannelPresenter(this)
        presenter.getChannel()
    }


    override fun bindChanel(data: ArrayList<RefundChannel>) {
        mAdapter.setNewInstance(data)
    }

    override fun bindListener() {
        next_tv.setOnClickListener {
            if (selectedChannel.isBlank()) {
                ToastUtils.showShort(R.string.please_selected_channel)
                return@setOnClickListener
            }

            val intent = Intent().putExtra(Constant.PARAM_REFUND_CHANNEL_NAME, selectedChannel)
            if (isBankChannel) {
                intent.setClass(this, BankChannelFormActivity::class.java)
            } else {
                intent.setClass(this, ThirdChannelFormActivity::class.java)
            }
            startActivity(intent)
        }
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_REFUND_METHOD_ADD_SUCCESS -> {
                finish()
            }
        }

    }
}