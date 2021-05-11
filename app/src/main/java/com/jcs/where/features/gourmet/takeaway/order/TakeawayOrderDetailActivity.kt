package com.jcs.where.features.gourmet.takeaway.order

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity

/**
 * Created by Wangsw  2021/5/11 14:53.
 * 外卖订单详情
 */
class TakeawayOrderDetailActivity : BaseMvpActivity<TakeawayOrderDetailPresenter>(), TakeawayOrderDetailView {

    override fun getLayoutId() = R.layout.activity_order_detail_takeaway

    override fun initView() {

    }

    override fun initData() {
        presenter = TakeawayOrderDetailPresenter(this)
    }

    override fun bindListener() {

    }


}