package com.jcs.where.features.merchant

import android.content.Intent
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity

/**
 * Created by Wangsw  2021/11/19 14:15.
 * 商家入住
 */
class MerchantSettledActivity : BaseMvpActivity<MerchantSettledPresenter>(), MerchantSettledView {

    override fun getLayoutId() = R.layout.activity_merchant_settled_home


    override fun initView() {

    }

    override fun initData() {
        presenter = MerchantSettledPresenter(this)
    }

    override fun bindListener() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
        super.onActivityResult(requestCode, resultCode, data)

}