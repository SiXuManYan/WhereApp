package com.jcs.where.features.mall.second

import androidx.recyclerview.widget.LinearLayoutManager
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_mall_second.*

/**
 * Created by Wangsw  2021/12/3 15:30.
 * 新版商城二级列表
 */
class MallSecondActivity : BaseMvpActivity<MallSecondPresenter>(), MallSecondView {


    /** 当前页面对应的一级分类 */
    lateinit var targetFirstCategory: Category

    lateinit var mThirdCategoryAdapter:ThirdCategoryFilterAdapter

    override fun getLayoutId() = R.layout.activity_mall_second

    override fun initView() {

        initCategoryFilter()
    }

    private fun initCategoryFilter() {

        mThirdCategoryAdapter = ThirdCategoryFilterAdapter()
        category_rv.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = mThirdCategoryAdapter


        }
    }

    override fun initData() {
        presenter = MallSecondPresenter(this)
    }

    override fun bindListener() {
        TODO("Not yet implemented")
    }
}