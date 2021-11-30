package com.jcs.where.features.mall.home.child

import android.view.View
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.mvp.BaseMvpFragment

/**
 * Created by Wangsw  2021/11/30 17:01.
 * 商城首页商品
 */
class MallHomeChildFragment :BaseMvpFragment<MallHomeChildPresenter>(),MallHomeChildView{

    /** 当前页对应的一级分类 */
    lateinit var targetFirstCategory :Category

    override fun getLayoutId() = R.layout.fragment_mall_home_child

    override fun initView(view: View) {

    }

    override fun initData() {
        presenter = MallHomeChildPresenter(this)
    }

    override fun bindListener() {

    }

}