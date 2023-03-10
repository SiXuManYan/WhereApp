package com.jcs.where.features.payment.tokenized

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.response.pay.PayCounterChannel
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_refresh_list.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2023/3/9 15:25.
 * 已绑定渠道列表
 */
class TokenizedActivity : BaseMvpActivity<TokenizedPresenter>(), TokenizedView, OnItemChildClickListener {

    private var index = -1

    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: TokenizedAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refresh_list

    override fun initView() {
        mJcsTitle.setMiddleTitle(R.string.set_up_tokenized_pay)
        container_ll.setBackgroundColor(Color.WHITE)
        recycler.setBackgroundColor(ColorUtils.getColor(R.color.grey_F5F5F5))

        initContent()
    }

    private fun initContent() {

        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            addEmptyList(this)
        }

        mAdapter = TokenizedAdapter().apply {
            addChildClickViewIds(R.id.unbind_tv)
            setOnItemChildClickListener(this@TokenizedActivity)
            setEmptyView(emptyView)
        }

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.apply {
            adapter = mAdapter
            layoutManager = manager
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, 1, SizeUtils.dp2px(16f), 0))
        }

    }

    override fun initData() {
        presenter = TokenizedPresenter(this)
        presenter.getBoundList()
    }

    override fun bindListener() {
        swipe_layout.setOnRefreshListener {
            presenter.getBoundList()
        }
    }

    override fun bindBoundList(data: MutableList<PayCounterChannel>?) {
        swipe_layout.isRefreshing = false
        if (data.isNullOrEmpty()) emptyView.showEmptyContainer()
        mAdapter.setNewInstance(data)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        index = position
        val payCounter = mAdapter.data[position]
        when (view.id) {
            R.id.unbind_tv -> {
                unBind(payCounter.title, payCounter.channel_code)
            }
            else -> {}
        }

    }


    private fun unBind(channelTitle: String, channelCode: String) {

        val timeDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_pay_unbind_token, null)
        timeDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val title_tv = view.findViewById<TextView>(R.id.title_tv)
        val pay_channel_tv = view.findViewById<TextView>(R.id.pay_channel_tv)

        val cancel_bt = view.findViewById<Button>(R.id.cancel_bt)
        val confirm_bt = view.findViewById<Button>(R.id.confirm_bt)


        title_tv.text = getString(R.string.unbind_token_title_format, channelTitle)
        pay_channel_tv.text = getString(R.string.unbind_token_hint_format, channelTitle)


        cancel_bt.setOnClickListener {
            timeDialog.dismiss()

        }
        confirm_bt.setOnClickListener {
            timeDialog.dismiss()
            presenter.unbindToken(channelCode)
        }

        timeDialog.show()

    }

    override fun unBindSuccess() {
        if (index > 0) {
            mAdapter.removeAt(index)
        }
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_UNBIND_PAY_TOKEN_SUCCESS))
    }


}