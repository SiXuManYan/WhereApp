package com.jcs.where.features.store.detail

import com.jcs.where.R
import com.jcs.where.api.response.store.StoreDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2021/6/15 10:33.
 * 商城店铺详情
 */
class StoreDetailActivity : BaseMvpActivity<StoreDetailPresenter>() ,StoreDetailView{

     var id : Int  = 0

    override fun getLayoutId() = R.layout.activity_store_detail

    override fun initView() {

        val bundle = intent.extras
        if (bundle == null) {
            finish()
            return
        }
         id = bundle.getInt(Constant.PARAM_ID, 0 )
    }

    override fun initData() {
        presenter = StoreDetailPresenter(this)
        presenter.getDetail(id)
    }

    override fun bindListener() {

    }

    override fun bindDetail(response: StoreDetail) {

    }
}