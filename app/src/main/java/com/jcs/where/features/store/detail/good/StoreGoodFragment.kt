package com.jcs.where.features.store.detail.good

import android.os.Bundle
import android.view.View
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2021/6/16 15:27.
 * 商城 商品列表
 */
class StoreGoodFragment : BaseMvpFragment<StoreGoodPresenter>(), StoreGoodView {

    companion object {

        /**
         * 美食评论
         * @param shop_id 商家ID
         */
        fun newInstance(shop_id: String): StoreGoodFragment {
            val fragment = StoreGoodFragment()
            val bundle = Bundle().apply {
                putString(Constant.PARAM_SHOP_ID, shop_id)
            }
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutId() = R.layout.fragment_refresh_list_no_refresh

    override fun initView(view: View) {

    }

    override fun initData() {
        presenter = StoreGoodPresenter(this)
    }

    override fun bindListener() {

    }

}