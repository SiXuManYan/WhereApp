package com.jcs.where.features.mall.cart

import android.view.LayoutInflater
import android.view.View
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.VibrateUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.api.response.mall.MallCartItem
import com.jcs.where.features.store.cart.child.OnChildSelectClick
import com.jcs.where.features.store.cart.child.OnGroupSelectClick
import com.jcs.where.features.store.cart.child.StoreCartValueChangeListener
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.NumberView2

/**
 * Created by Wangsw  2021/12/14 18:50.
 * 商城购物车
 */
class MallCartAdapter : BaseQuickAdapter<MallCartGroup, BaseViewHolder>(R.layout.item_mall_cart),LoadMoreModule {

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


    override fun convert(holder: BaseViewHolder, item: MallCartGroup) {

        val store_cart_ll = holder.getView<LinearLayout>(R.id.store_cart_ll)
        val select_all_tv = holder.getView<CheckedTextView>(R.id.select_all_tv)
        val child_container_ll = holder.getView<LinearLayout>(R.id.child_container_ll)


        select_all_tv.apply {
            text = item.title
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

            // 子项全选
            item.gwc.forEachIndexed { index, storeCartItem ->
                storeCartItem.nativeIsSelect = item.nativeIsSelect
                val good_checked_tv = child_container_ll.getChildAt(index).findViewById<CheckedTextView>(R.id.good_checked_tv)
                good_checked_tv.isChecked = item.nativeIsSelect
                onGroupSelectClick?.onGroupSelected(item.nativeIsSelect)

            }
        }

        child_container_ll.removeAllViews()
        item.gwc.forEach {
            val child = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart_child_for_store_mall, null)

            bindChild(child, it, select_all_tv, item)
            child_container_ll.addView(child)
        }

    }

    private fun bindChild(child: View, mallCartItem: MallCartItem, groupSelectAllTv: CheckedTextView, groupData: MallCartGroup) {
        val good_checked_tv = child.findViewById<CheckedTextView>(R.id.good_checked_tv)
        val image_iv = child.findViewById<ImageView>(R.id.image_iv)
        val good_name = child.findViewById<TextView>(R.id.good_name_tv)
        val now_price_tv = child.findViewById<TextView>(R.id.now_price_tv)
        val good_attr_tv = child.findViewById<TextView>(R.id.good_attr_tv)
        val number_view = child.findViewById<NumberView2>(R.id.number_view)

        mallCartItem.goods_info?.let {
            GlideUtil.load(context, it.photo, image_iv, 4)
            good_name.text = it.title
        }

        mallCartItem.specs_info?.let {
            val nowPrice: String = StringUtils.getString(R.string.price_unit_format, it.price.toPlainString())
            now_price_tv.text = nowPrice

            // 数量
            number_view.apply {
                alwaysEnableCut = true
                MIN_GOOD_NUM = 1
                MAX_GOOD_NUM = it.stock
                cut_iv.setImageResource(R.mipmap.ic_cut_black_transparent)
                add_iv.setImageResource(R.mipmap.ic_add_black)
                cutResIdCommon = R.mipmap.ic_cut_black
                cutResIdMin = R.mipmap.ic_cut_black_transparent
                addResIdCommon = R.mipmap.ic_add_black
                addResIdMax = R.mipmap.ic_add_black_transparent
                updateNumberJudgeMin(mallCartItem.good_num)

                valueChangeListener = object : NumberView2.OnValueChangeListener {
                    override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
                        mallCartItem.good_num = goodNum
                        numberChangeListener?.onChildNumberChange(mallCartItem.cart_id, isAdd)
                        numberChangeListener?.onChildNumberChange(mallCartItem.cart_id, isAdd,goodNum)


                    }
                }
            }


            val attr = StringBuffer()

            // 商品属性
            val specs = it.specs
            if (specs.isNullOrEmpty()) {
                good_attr_tv.visibility = View.GONE
            }else{
                good_attr_tv.visibility = View.VISIBLE
                specs.forEach { entry ->
                    attr.append(entry.value + " ")
                }
                good_attr_tv.text = attr
            }
        }


        // child 选中
        good_checked_tv.apply {
            this.isChecked = mallCartItem.nativeIsSelect
            setOnClickListener { view ->
                val currentIsSelect = mallCartItem.nativeIsSelect
                mallCartItem.nativeIsSelect = !currentIsSelect
                if (mallCartItem.nativeIsSelect) {
                    VibrateUtils.vibrate(10)
                }
                this.isChecked = mallCartItem.nativeIsSelect

                // 判断组内是否全选
                val isGroupSelectAll = checkGroupSelectAll(groupData)
                groupData.nativeIsSelect = isGroupSelectAll
                groupSelectAllTv.isChecked = isGroupSelectAll

                onChildSelectClick?.onChildSelected(mallCartItem.nativeIsSelect)

            }
        }
    }

    /**
     * 是否全部选中
     */
    fun checkGroupSelectAll(groupData: MallCartGroup): Boolean {
        val result = ArrayList<Boolean>()
        result.clear()

        groupData.gwc.forEach { data ->
            result.add(data.nativeIsSelect)
        }
        return !result.contains(false)
    }

}