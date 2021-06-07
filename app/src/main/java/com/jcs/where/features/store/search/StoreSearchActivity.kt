package com.jcs.where.features.store.search

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_search_store.*

/**
 * Created by Wangsw  2021/6/4 15:58.
 * 商城搜索
 */
class StoreSearchActivity : BaseMvpActivity<StoreSearchPresenter>(), StoreSearchView {

    override fun getLayoutId() = R.layout.activity_search_store

    override fun isStatusDark() = true

    override fun initView() {

        val title = intent.getStringExtra(Constant.PARAM_TITLE)
        search_aet.text = title


    }

    override fun initData() {


    }

    override fun bindListener() {
        back_iv.setOnClickListener { finish() }
        search_aet.setOnClickListener { finish() }
    }


}