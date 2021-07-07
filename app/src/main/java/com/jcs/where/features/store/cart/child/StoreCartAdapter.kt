package com.jcs.where.features.store.cart.child

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
import com.jcs.where.R
import com.jcs.where.api.response.store.cart.StoreCartGroup
import com.jcs.where.api.response.store.cart.StoreCartItem
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.NumberView2

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
            val child = LayoutInflater.from(context).inflate(R.layout.item_store_cart_child, null)
            bindChild(child, it, select_all_tv, item)
            child_container_ll.addView(child)
        }


    }

    private fun bindChild(child: View, it: StoreCartItem, groupSelectAllTv: CheckedTextView, groupData: StoreCartGroup) {

        val good_checked_tv = child.findViewById<CheckedTextView>(R.id.good_checked_tv)
        val image_iv = child.findViewById<ImageView>(R.id.image_iv)
        val good_name = child.findViewById<TextView>(R.id.good_name)
        val now_price_tv = child.findViewById<TextView>(R.id.now_price_tv)
        val old_price_tv = child.findViewById<TextView>(R.id.old_price_tv)
        val number_view = child.findViewById<NumberView2>(R.id.number_view)


        val goodData = it.good_data

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
            MIN_GOOD_NUM = 1
            alwaysEnableCut(true)
            updateNumber(it.good_num)


            valueChangeListener = object : NumberView2.OnValueChangeListener {
                override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
                    it.good_num = goodNum
                    numberChangeListener?.onChildNumberChange(it.cart_id, isAdd)

                }
            }

        }

        // child 选中
        good_checked_tv.apply {
            this.isChecked = it.nativeIsSelect
            setOnClickListener { view ->
                val currentIsSelect = it.nativeIsSelect
                it.nativeIsSelect = !currentIsSelect
                if (it.nativeIsSelect) {
                    VibrateUtils.vibrate(10)
                }
                this.isChecked = it.nativeIsSelect

                // 判断组内是否全选
                val isGroupSelectAll = checkGroupSelectAll(groupData)
                groupData.nativeIsSelect = isGroupSelectAll
                groupSelectAllTv.isChecked = isGroupSelectAll

                onChildSelectClick?.onChildSelected(it.nativeIsSelect)


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
    fun onChildNumberChange(cartId: Int, add: Boolean)
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