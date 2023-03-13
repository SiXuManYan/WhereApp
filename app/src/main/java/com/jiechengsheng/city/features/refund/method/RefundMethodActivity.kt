package com.jiechengsheng.city.features.refund.method

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.RefundMethod
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.refund.add.channel.RefundChannelActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_refund_method_list.*

/**
 * Created by Wangsw  2022/4/25 16:11.
 * 退款方式列表
 */
class RefundMethodActivity : BaseMvpActivity<RefundMethodPresenter>(), RefundMethodView, OnItemChildClickListener,
    OnItemClickListener {

    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: RefundMethodAdapter
    private var handleSelected = false

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refund_method_list

    override fun initView() {

        handleSelected = intent.getBooleanExtra(Constant.PARAM_HANDLE_SELECT, false)

        emptyView = EmptyView(this).apply {
            setEmptyImage(R.mipmap.ic_empty_refund)
            setEmptyMessage(R.string.refund_method_empty)
            setEmptyHint(R.string.refund_method_empty_hint)
            addEmptyList(this)
        }

        mAdapter = RefundMethodAdapter().apply {
            setEmptyView(emptyView)
            addChildClickViewIds(R.id.unbind_iv)
            setOnItemChildClickListener(this@RefundMethodActivity)
            setOnItemClickListener(this@RefundMethodActivity)
        }

        recycler_view.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), SizeUtils.dp2px(1f), 0, 0))
        }


    }

    override fun initData() {
        presenter = RefundMethodPresenter(this)
        presenter.getRefundMethod()
    }

    override fun bindListener() {
        add_tv.setOnClickListener {
            startActivity(RefundChannelActivity::class.java)
        }
    }


    override fun bindData(toMutableList: MutableList<RefundMethod>) {
        if (toMutableList.isNullOrEmpty()) {
            emptyView.showEmptyContainer()
        }
        mAdapter.setNewInstance(toMutableList)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val refundMethod = mAdapter.data[position]

        when (view.id) {
            R.id.unbind_iv -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.hint)
                    .setMessage(R.string.unbind_refund_method_hint)
                    .setCancelable(false)
                    .setPositiveButton(R.string.confirm) { dialogInterface, _ ->
                        // 解绑
                        presenter.unBind(refundMethod.id)
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .create().show()


            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (!handleSelected) {
            return
        }
        // 选中
        val refundMethod = mAdapter.data[position]
        setResult(RESULT_OK, Intent().putExtra(Constant.PARAM_REFUND_METHOD, refundMethod))
        finish()
    }

    override fun unBindSuccess() {
        presenter.getRefundMethod()
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_REFUND_METHOD_ADD_SUCCESS -> {
                ToastUtils.showShort(R.string.refund_method_add_success)
                presenter.getRefundMethod()
            }
        }
    }

}