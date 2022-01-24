package com.jcs.where.features.mall.shop.home.second

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.mall.shop.home.good.MallShopGoodFragment
import com.jcs.where.utils.Constant

/**
 * Created by Wangsw  2022/1/24 15:26.
 * 商城店铺商品列表
 */
class MallShopGoodActivity : BaseActivity() {


    /** 店铺分类Id */
    private var mCategoryId = 0
    private var mShopId = 0
    private var mCategoryName = ""

    companion object {

        fun navigation(context: Context, shopId: Int, categoryName: String, shopCategoryId: Int) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_SHOP_ID, shopId)
                putString(Constant.PARAM_CATEGORY_NAME, categoryName)
                putInt(Constant.PARAM_CATEGORY_ID, shopCategoryId)
            }
            val intent = Intent(context, MallShopGoodActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_shop_good

    override fun initView() {
        intent.extras?.let {
            mShopId = it.getInt(Constant.PARAM_SHOP_ID, 0)
            mCategoryName = it.getString(Constant.PARAM_CATEGORY_NAME, "")
            mCategoryId = it.getInt(Constant.PARAM_CATEGORY_ID, 0)
        }

    }

    override fun initData() {
        mJcsTitle.setMiddleTitle(mCategoryName)
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = MallShopGoodFragment.newInstance(shopId = mShopId, shopCategoryId = mCategoryId)
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.fragment_container, fragment)
        }
        transaction.commit()

    }

    override fun bindListener() = Unit
}