package com.jcs.where.features.refund.add.channel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.footprint.Footprint
import com.jcs.where.api.response.mall.RefundChannel
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.refund.add.form.third.ThirdChannelFormActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_refund_channel_selected.*

/**
 * Created by Wangsw  2022/4/25 19:03.
 * 退款渠道
 */
class RefundChannelActivity : BaseMvpActivity<RefundChannelPresenter>(), RefundChannelView, OnItemClickListener {


    private var channelCode = ""
    private var channelCategory = ""
    private var channelName = ""


    private lateinit var mAdapter: RefundChannelAdapter
    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refund_channel_selected

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        mAdapter = RefundChannelAdapter().apply {

            setOnItemClickListener(this@RefundChannelActivity)
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
            if (channelCode.isBlank()) {
                ToastUtils.showShort(R.string.please_selected_channel)
                return@setOnClickListener
            }
            startActivity(ThirdChannelFormActivity::class.java, Bundle().apply {
                putString(Constant.PARAM_CHANNEL_NAME, channelName)
                putString(Constant.PARAM_REFUND_CHANNEL_CODE, channelCode)
                putString(Constant.PARAM_REFUND_CHANNEL_CATEGORY, channelCategory)
            })
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {


        val itemViewType = adapter.getItemViewType(position)
        if (itemViewType == Footprint.TYPE_TITLE) {
            return
        }

        val refundChannel = mAdapter.data[position]

        channelCode = refundChannel.channel_code
        channelCategory = refundChannel.channel_category
        channelName = refundChannel.name

        mAdapter.data.forEachIndexed { index, item ->
            item.isSelected = index == position
            mAdapter.notifyDataSetChanged()
        }


    }
}