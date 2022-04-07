package com.jcs.where.features.mall.shop.home.category

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallShopCategory
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list_no_refresh.*
import java.util.*

/**
 * Created by Wangsw  2022/1/24 10:31.
 * 店铺详情分类
 */
class MallShopCategoryFragment : BaseMvpFragment<MallShopCategoryPresenter>(), MallShopCategoryView {

    private var mShopId = 0

    private lateinit var mAdapter: MallShopCategoryAdapter


    companion object {

        fun newInstance(shopId: Int): MallShopCategoryFragment {
            val fragment = MallShopCategoryFragment()

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_SHOP_ID, shopId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_refresh_list_no_refresh

    override fun initView(view: View) {

        arguments?.let {
            mShopId = it.getInt(Constant.PARAM_SHOP_ID)
        }

        mAdapter = MallShopCategoryAdapter()
        recycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.color_F0F0F0), 1, 0, 0))
        }
    }

    override fun initData() {
        presenter = MallShopCategoryPresenter(this)

    }

    override fun loadOnVisible() {
        presenter.getCategory(mShopId)
    }

    override fun bindListener() {

    }

    override fun bindCategory(response: ArrayList<MallShopCategory>) {
        mAdapter.setNewInstance(response)
    }
}