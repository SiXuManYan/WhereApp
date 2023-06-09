package com.jiechengsheng.city.features.mall.shop.home.category

import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallShopCategory
import com.jiechengsheng.city.features.mall.shop.home.filter.MallShopFilterGoodActivity
import com.jiechengsheng.city.widget.list.DividerDecoration

/**
 * Created by Wangsw  2022/1/24 14:34.
 * 商城店铺首级分类
 */
class MallShopCategoryAdapter : BaseQuickAdapter<MallShopCategory, BaseViewHolder>(R.layout.item_mall_shop_category) {


    override fun convert(holder: BaseViewHolder, item: MallShopCategory) {
        holder.setText(R.id.category_title_tv, item.name + " ( " + item.num + " ) ")


        holder.getView<RelativeLayout>(R.id.title_rl).setOnClickListener {
            // 店铺详情列表
            MallShopFilterGoodActivity.navigation(context, shopId = item.shop_id, categoryName = item.name, shopCategoryId = item.id)
        }

        val childRv = holder.getView<RecyclerView>(R.id.child_category_rv)
        bindChild(childRv, item)
    }

    private fun bindChild(childRv: RecyclerView, item: MallShopCategory) {
        val childAdapter = MallShopCategoryChildAdapter()
        childRv.apply {
            adapter = childAdapter
            isNestedScrollingEnabled = true
            layoutManager = object : GridLayoutManager(context, 2, VERTICAL, false) {
                override fun canScrollVertically() = false
            }
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.white), SizeUtils.dp2px(16f), 0, 0))
        }
        childAdapter.setNewInstance(item.level)
    }
}

/**
 * Created by Wangsw  2022/1/24 14:34.
 * 商城店铺子分类
 */
class MallShopCategoryChildAdapter : BaseQuickAdapter<MallShopCategory, BaseViewHolder>(R.layout.item_mall_shop_category_child) {
    override fun convert(holder: BaseViewHolder, item: MallShopCategory) {
        val nameTv = holder.getView<TextView>(R.id.name_tv)
        nameTv.text = item.name
        nameTv.setOnClickListener {
            MallShopFilterGoodActivity.navigation(context, shopId = item.shop_id, categoryName = item.name, shopCategoryId = item.id)
        }
    }

}