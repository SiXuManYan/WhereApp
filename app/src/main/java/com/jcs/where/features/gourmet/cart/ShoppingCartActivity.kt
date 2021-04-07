package com.jcs.where.features.gourmet.cart

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_shopping_cart.*

/**
 * Created by Wangsw  2021/4/7 14:49.
 * 购物车
 */
class ShoppingCartActivity : BaseMvpActivity<ShoppingCartPresenter>(), ShoppingCartView {

    override fun getLayoutId() = R.layout.activity_shopping_cart

    override fun initView() {

    }

    override fun initData() {
        presenter = ShoppingCartPresenter(this);
    }

    override fun bindListener() {

        edit_tv.setOnClickListener {
            right_vs.displayedChild = 1
            bottom_vs.displayedChild = 1
        }

        cancel_tv.setOnClickListener {
            right_vs.displayedChild = 0
            bottom_vs.displayedChild = 0
        }
    }


}