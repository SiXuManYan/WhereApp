package com.jiechengsheng.city.features.order.parent

import com.jiechengsheng.city.R
import com.jiechengsheng.city.base.BaseActivity

/**
 * Created by Wangsw  2021/8/7 15:51.
 * 订单列表过度页，用户取消支付后，跳转到该页面
 */
class OrderActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_order_list

    override fun initView() {
        val transaction = supportFragmentManager.beginTransaction()

        val orderFragment = OrderFragment().apply {
            needBack = true
        }
        if (orderFragment.isAdded) {
            transaction.show(orderFragment)
        } else {
            transaction.add(R.id.fragment_container, orderFragment)
        }
        transaction.commit()
    }

    override fun initData() {

    }

    override fun bindListener() {

    }
}