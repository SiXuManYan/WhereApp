package com.jcs.where.features.refund.method

import android.view.View
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.mall.RefundMethod
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_refund_method_list.*

/**
 * Created by Wangsw  2022/4/25 16:11.
 * 退款方式列表
 */
class RefundMethodActivity : BaseMvpActivity<RefundMethodPresenter>(), RefundMethodView, OnItemChildClickListener, OnItemClickListener {

    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: RefundMethodAdapter

    override fun getLayoutId() = R.layout.activity_refund_method_list

    override fun initView() {

        emptyView = EmptyView(this).apply {
            setEmptyImage(R.mipmap.ic_empty_card_coupon)
            setEmptyMessage(R.string.refund_method_empty)
            setEmptyMessage(R.string.refund_method_empty_hint)
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

        }
    }


    override fun bindData(toMutableList: MutableList<RefundMethod>) {
        mAdapter.setNewInstance(toMutableList)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val refundMethod = mAdapter.data[position]

        when (view.id) {
            R.id.unbind_iv -> {
                // 解绑
                presenter.unBind(refundMethod.id)
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        // 选中
    }

    override fun unBindSuccess() {
        presenter.getRefundMethod()
    }
}