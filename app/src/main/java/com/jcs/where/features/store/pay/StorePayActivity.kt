package com.jcs.where.features.store.pay

import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.store.PayChannel
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_store_pay.*

/**
 * Created by Wangsw  2021/6/23 15:10.
 * 商城支付
 */
class StorePayActivity : BaseMvpActivity<StorePayPresenter>(), StorePayView {

    private lateinit var mAdapter: StorePayAdapter

    override fun getLayoutId() = R.layout.activity_store_pay

    override fun initView() {

        mAdapter = StorePayAdapter()

        pay_channel_rv.apply {
            adapter = mAdapter
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerDecoration(getColor(R.color.colorPrimary), SizeUtils.dp2px(20f),
                    0, 0).apply { setDrawHeaderFooter(false) })
        }
    }

    override fun initData() {
        presenter = StorePayPresenter(this)
        presenter.getPayChannel()

    }

    override fun bindListener() {

    }

    override fun bindData(response: ArrayList<PayChannel>) {
        mAdapter.setNewInstance(response)
    }

}