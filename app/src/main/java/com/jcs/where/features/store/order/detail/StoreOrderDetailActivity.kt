package com.jcs.where.features.store.order.detail

import com.jcs.where.R
import com.jcs.where.api.response.order.store.StoreOrderDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2021/6/26 11:15.
 *  商城订单详情
 */
class StoreOrderDetailActivity : BaseMvpActivity<StoreOrderDetailPresenter>(), StoreOrderDetailView {


    private var orderId = 0

    override fun getLayoutId() = R.layout.activity_store_order_detail

    override fun initView() {

        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)

        }
    }

    override fun initData() {
        presenter = StoreOrderDetailPresenter(this)
        presenter.getOrderDetail(orderId)

    }

    override fun bindData(response: StoreOrderDetail) {

    }

    override fun bindListener() {

        mJcsTitle.setFirstRightIvClickListener {
            // todo IM 聊天
        }


    }


}