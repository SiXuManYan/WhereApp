package com.jiechengsheng.city.features.store.cart.child

import android.view.LayoutInflater
import android.view.View
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.VibrateUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallCartItem
import com.jiechengsheng.city.api.response.store.cart.StoreCartGroup
import com.jiechengsheng.city.api.response.store.cart.StoreCartItem
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.GlideUtil
import com.jiechengsheng.city.widget.NumberView2

/**
 * Created by Wangsw  2021/7/5 15:41.
 *
 */
class StoreCartAdapter : BaseQuickAdapter<StoreCartGroup, BaseViewHolder>(R.layout.item_store_cart) {


    /**
     * child数量改变监听
     */
    var numberChangeListener: StoreCartValueChangeListener? = null

    /**
     * child选中状态监听
     */
    var onChildSelectClick: OnChildSelectClick? = null

    /**
     * group 选中
     */
    var onGroupSelectClick: OnGroupSelectClick? = null

    override fun convert(holder: BaseViewHolder, item: StoreCartGroup) {

        val store_cart_ll = holder.getView<LinearLayout>(R.id.store_cart_ll)
        val select_all_tv = holder.getView<CheckedTextView>(R.id.select_all_tv)
        val child_container_ll = holder.getView<LinearLayout>(R.id.child_container_ll)


        select_all_tv.apply {
            text = item.shop_name
            isChecked = item.nativeIsSelect
        }

        // group 选中
        select_all_tv.setOnClickListener { _ ->

            val currentIsSelect = item.nativeIsSelect
            item.nativeIsSelect = !currentIsSelect
            if (item.nativeIsSelect) {
                VibrateUtils.vibrate(10)
            }
            select_all_tv.isChecked = item.nativeIsSelect

            item.goods.forEachIndexed { index, storeCartItem ->
                storeCartItem.nativeIsSelect = item.nativeIsSelect
                val good_checked_tv = child_container_ll.getChildAt(index).findViewById<CheckedTextView>(R.id.good_checked_tv)
                good_checked_tv.isChecked = item.nativeIsSelect
                onGroupSelectClick?.onGroupSelected(item.nativeIsSelect)

            }
        }

        child_container_ll.removeAllViews()
        item.goods.forEach {
            val child = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart_child_for_store, null)

            bindChild(child, it, select_all_tv, item)
            child_container_ll.addView(child)
        }


    }

    private fun bindChild(child: View, storeCartItem: StoreCartItem, groupSelectAllTv: CheckedTextView, groupData: StoreCartGroup) {

        val good_checked_tv = child.findViewById<CheckedTextView>(R.id.good_checked_tv)
        val image_iv = child.findViewById<ImageView>(R.id.image_iv)
        val good_name = child.findViewById<TextView>(R.id.good_name_tv)
        val now_price_tv = child.findViewById<TextView>(R.id.now_price_tv)
        val old_price_tv = child.findViewById<TextView>(R.id.old_price_tv)
        val number_view = child.findViewById<NumberView2>(R.id.number_view)


        val goodData = storeCartItem.good_data

        if (goodData.images.isNotEmpty()) {
            GlideUtil.load(context, goodData.images[0], image_iv, 4)
            good_name.text = goodData.title
        }


        val nowPrice: String = StringUtils.getString(R.string.price_unit_format, goodData.price)
        now_price_tv.text = nowPrice
        val oldPrice: String = StringUtils.getString(R.string.price_unit_format, goodData.original_price.toPlainString())
        val oldBuilder = SpanUtils().append(oldPrice).setStrikethrough().create()
        old_price_tv.text = oldBuilder

        number_view.apply {
            alwaysEnableCut = true
            MIN_GOOD_NUM = 1
            MAX_GOOD_NUM = BusinessUtils.getSafeStock(goodData.inventory)
            cut_iv.setImageResource(R.mipmap.ic_cut_black_transparent)
            add_iv.setImageResource(R.mipmap.ic_add_black)
            cutResIdCommon = R.mipmap.ic_cut_black
            cutResIdMin = R.mipmap.ic_cut_black_transparent
            addResIdCommon = R.mipmap.ic_add_black
            addResIdMax = R.mipmap.ic_add_black_transparent
            updateNumberJudgeMin(storeCartItem.good_num)

            valueChangeListener = object : NumberView2.OnValueChangeListener {
                override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
                    storeCartItem.good_num = goodNum
                    numberChangeListener?.onChildNumberChange(storeCartItem.cart_id, isAdd, goodNum)

                }
            }
        }

        // child 选中
        good_checked_tv.apply {
            this.isChecked = storeCartItem.nativeIsSelect
            setOnClickListener { view ->
                val currentIsSelect = storeCartItem.nativeIsSelect
                storeCartItem.nativeIsSelect = !currentIsSelect
                if (storeCartItem.nativeIsSelect) {
                    VibrateUtils.vibrate(10)
                }
                this.isChecked = storeCartItem.nativeIsSelect

                // 判断组内是否全选
                val isGroupSelectAll = checkGroupSelectAll(groupData)
                groupData.nativeIsSelect = isGroupSelectAll
                groupSelectAllTv.isChecked = isGroupSelectAll

                onChildSelectClick?.onChildSelected(storeCartItem.nativeIsSelect)


            }

        }

    }

    /**
     * 是否全部选中
     */
    fun checkGroupSelectAll(groupData: StoreCartGroup): Boolean {
        val result = ArrayList<Boolean>()
        result.clear()

        groupData.goods.forEach { data ->
            result.add(data.nativeIsSelect)
        }
        return !result.contains(false)
    }


}


/**
 * child数量改变监听
 * 1.fragment重新计算价格，
 * 2.fragment调用更改购物车数量接口
 */
interface StoreCartValueChangeListener {

    /**
     * 数量改变,重新总计算价格，调用更改购物车数量接口
     * @param cartId 购物车id
     * @param add 是否是添加
     */


    fun onChildNumberChange(cartId: Int, add: Boolean, number: Int) {

    }
}


/**
 * child选中状态监听
 * 1.adapter内父级group判断是否全选
 * 2.fragment重新计算价格
 * 3.fragment 判断 顶级全选
 */
interface OnChildSelectClick {


    /**
     * child 选中状态改变，重新计算总价格，判断顶级全选
     */
    fun onChildSelected(checked: Boolean)
}

/**
 * group 选中状态监听
 * 2.fragment重新计算价格
 * 3.fragment 判断 顶级全选
 */
interface OnGroupSelectClick {

    /**
     * group 选中状态改变，重新计算总价格，判断顶级全选
     */
    fun onGroupSelected(nativeIsSelect: Boolean)
}


/**
 * 重新选择SKU
 */
interface OnChildReselectSkuClick {

    /**
     * @param childIndex        当前操作的商品 index
     * @param adapterPosition   当前操作的店铺，在列表中的位置
     * @param source             可用于更改的SKU数据源
     *
     */
    fun reselectSkuClick(childIndex: Int, adapterPosition: Int, source: MallCartItem)

}