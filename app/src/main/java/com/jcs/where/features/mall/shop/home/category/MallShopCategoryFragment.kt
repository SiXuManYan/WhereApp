package com.jcs.where.features.mall.shop.home.category

import android.view.View
import com.jcs.where.base.mvp.BaseMvpFragment

/**
 * Created by Wangsw  2022/1/24 10:31.
 * 店铺详情分类
 */
class MallShopCategoryFragment  :BaseMvpFragment<MallShopCategoryPresenter>(),MallShopCategoryView{
    override fun getLayoutId(): Int {
        TODO("Not yet implemented")
    }

    override fun initView(view: View?) {

    }

    override fun initData() {
     presenter = MallShopCategoryPresenter(this)
    }

    override fun bindListener() {

    }
}