package com.jcs.where.features.store.good

import com.jcs.where.R
import com.jcs.where.api.response.store.StoreGoodDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2021/6/18 14:23.
 * 商城商品详情
 */
class StoreGoodDetailActivity :BaseMvpActivity<StoreGoodDetailPresenter>(),StoreGoodDetailView {

    var good_id: Int = 0


    override fun getLayoutId() = R.layout.activity_store_good_detail

    override fun initView() {

        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
        good_id = bundle.getInt(Constant.PARAM_ID, 0)

    }

    override fun initData() {
        presenter = StoreGoodDetailPresenter(this)
        presenter.getData(good_id)

    }

    override fun bindListener() {

    }


    override fun bingData(response: StoreGoodDetail) {

    }
}