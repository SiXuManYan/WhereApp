package com.jcs.where.features.refund.selected

import android.annotation.SuppressLint
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.RefundChannel
import com.jcs.where.base.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_refund_channel_selected.*

/**
 * Created by Wangsw  2022/4/25 19:03.
 *
 */
class RefundChannelActivity : BaseMvpActivity<RefundChannelPresenter>(), RefundChannelView {

    private lateinit var mAdapter: RefundChannelAdapter

    private var isBankChannel = false
    private var selectedChannel = ""

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
        }
    }
}