package com.jcs.where.features.store.order

import com.jcs.where.R
import com.jcs.where.api.response.store.StoreOrderCommitList
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2021/6/21 10:26.
 *  商城提交订单
 */
class StoreOrderCommitActivity :BaseMvpActivity<StoreOrderCommitPresenter>(),StoreOrderCommitView{


    override fun getLayoutId() = R.layout.activity_store_order_commit

    override fun initView() {

        val bundle = intent.extras
        bundle?.let {
            /*
            it.getInt(Constant.PARAM_SHOP_ID)
            it.getInt(Constant.PARAM_DELIVERY_TYPE)
            it.getString(Constant.PARAM_SHOP_NAME)
            it.getString(Constant.PARAM_SHOP_IMAGE)

            it.getInt(Constant.PARAM_GOOD_ID)
            it.getInt(Constant.PARAM_GOOD_NUMBER)
            it.getFloat(Constant.PARAM_PRICE)
            it.getString(Constant.PARAM_GOOD_NAME)
            it.getString(Constant.PARAM_GOOD_IMAGE)
            */
            val serializable = it.getSerializable(Constant.PARAM_ORDER_COMMIT_DATA) as StoreOrderCommitList





        }
    }

    override fun initData() {

    }

    override fun bindListener() {

    }
}