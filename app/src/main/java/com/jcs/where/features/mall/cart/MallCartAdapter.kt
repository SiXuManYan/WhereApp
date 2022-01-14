package com.jcs.where.features.mall.cart

import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.VibrateUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.api.response.mall.MallCartItem
import com.jcs.where.features.store.cart.child.OnChildReselectSkuClick
import com.jcs.where.features.store.cart.child.OnChildSelectClick
import com.jcs.where.features.store.cart.child.OnGroupSelectClick
import com.jcs.where.features.store.cart.child.StoreCartValueChangeListener
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.NumberView2

/**
 * Created by Wangsw  2021/12/14 18:50.
 * 商城购物车
 */
class MallCartAdapter : BaseQuickAdapter<MallCartGroup, BaseViewHolder>(R.layout.item_mall_cart), LoadMoreModule {

    /** child数量改变监听 */
    var numberChangeListener: StoreCartValueChangeListener? = null

    /** child选中状态监听 */
    var onChildSelectClick: OnChildSelectClick? = null

    /** group 选中 */
    var onGroupSelectClick: OnGroupSelectClick? = null

    /** 重新选择SKU */
    var onChildReselectSkuClick: OnChildReselectSkuClick? = null

    /** 是否为编辑模式 */
    var isEditMode = false


    override fun convert(holder: BaseViewHolder, item: MallCartGroup) {

        val store_cart_ll = holder.getView<LinearLayout>(R.id.store_cart_ll)
        val select_all_tv = holder.getView<CheckedTextView>(R.id.select_all_tv)
        val child_container_ll = holder.getView<LinearLayout>(R.id.child_container_ll)


        // group 选中
        select_all_tv.setOnClickListener { _ ->

            val currentIsSelect = if (isEditMode) {
                item.nativeIsSelectEdit
            } else {
                item.nativeIsSelect
            }

            if (isEditMode) {
                item.nativeIsSelectEdit = !currentIsSelect
            } else {
                item.nativeIsSelect = !currentIsSelect
            }
            select_all_tv.isChecked = !currentIsSelect

            if (item.nativeIsSelect || item.nativeIsSelectEdit) {
                VibrateUtils.vibrate(10)
            }


            // 子项全选
            item.gwc.forEachIndexed { index, storeCartItem ->

                val isSelected = if (isEditMode) {
                    item.nativeIsSelectEdit
                } else {
                    item.nativeIsSelect
                }

                if (isEditMode) {
                    storeCartItem.nativeIsSelectEdit = isSelected
                } else {
                    storeCartItem.nativeIsSelect = isSelected
                }

                val good_checked_tv = child_container_ll.getChildAt(index).findViewById<CheckedTextView>(R.id.good_checked_tv)
                good_checked_tv.isChecked = isSelected
            }
            onGroupSelectClick?.onGroupSelected(select_all_tv.isChecked)
        }

        child_container_ll.removeAllViews()
        item.gwc.forEachIndexed { childIndex, mallCartItem ->
            val child = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart_child_for_store_mall, null)
            bindChild(child, mallCartItem, select_all_tv, item, childIndex, holder.adapterPosition)
            child_container_ll.addView(child)
        }


        select_all_tv.apply {
            text = item.title

            // 按钮是否可用
            if (isEditMode) {
                this.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.selector_store_cart_select_all, 0, 0, 0)
                item.nativeEnable = true
                this.isEnabled = true
                this.isChecked = item.nativeIsSelectEdit
            } else {

                // 正常模式
                item.nativeEnable = checkGroupAllEnable(item)
                this.isEnabled = item.nativeEnable

                if (item.nativeEnable) {
                    isChecked = item.nativeIsSelect
                } else {
                    this.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.ic_checked_unavailable, 0, 0, 0)
                }


            }

        }


    }

    private fun bindChild(
        child: View,
        mallCartItem: MallCartItem,
        groupSelectAllTv: CheckedTextView,
        groupData: MallCartGroup,
        childIndex: Int,
        adapterPosition: Int,
    ) {
        val good_checked_tv = child.findViewById<CheckedTextView>(R.id.good_checked_tv)
        val image_iv = child.findViewById<ImageView>(R.id.image_iv)
        val good_name = child.findViewById<TextView>(R.id.good_name_tv)
        val now_price_tv = child.findViewById<TextView>(R.id.now_price_tv)
        val good_attr_tv = child.findViewById<TextView>(R.id.good_attr_tv)
        val number_view = child.findViewById<NumberView2>(R.id.number_view)


        // SKU已被删除
        val number_sku_sw = child.findViewById<ViewSwitcher>(R.id.number_sku_sw)
        val reselect_sku_tv = child.findViewById<TextView>(R.id.reselect_sku_tv)
        val sku_sw = child.findViewById<ViewSwitcher>(R.id.sku_sw)
        // 商品库存不足

        val stock_invalid_tv = child.findViewById<TextView>(R.id.stock_invalid_tv)

        // 是否库存不足
        val inventoryShortage = mallCartItem.good_num > mallCartItem.specs_info!!.stock

        // sku 是否被删除
        val skuDelete = mallCartItem.specs_info!!.delete_status

        mallCartItem.goods_info?.let {
            GlideUtil.load(context, it.photo, image_iv, 4)
            good_name.text = it.title
            stock_invalid_tv.visibility = if (inventoryShortage) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        mallCartItem.specs_info?.let {

            if (it.delete_status == 1) {
                number_sku_sw.displayedChild = 1
                sku_sw.displayedChild = 1
                good_name.setTextColor(ColorUtils.getColor(R.color.grey_999999))

                return@let
            }

            number_sku_sw.displayedChild = 0
            sku_sw.displayedChild = 0
            good_name.setTextColor(ColorUtils.getColor(R.color.black_333333))


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
                        numberChangeListener?.onChildNumberChange(mallCartItem.cart_id, isAdd, goodNum)
                    }
                }
            }


            val attr = StringBuffer()

            // 商品属性
            val specs = it.specs
            if (specs.isNullOrEmpty()) {
                good_attr_tv.visibility = View.GONE
            } else {
                good_attr_tv.visibility = View.VISIBLE
                specs.forEach { entry ->
                    attr.append(entry.value + " ")
                }
                good_attr_tv.text = attr
            }
        }


        good_attr_tv.setOnClickListener {
            onChildReselectSkuClick?.reselectSkuClick(childIndex, adapterPosition, mallCartItem)
        }
        reselect_sku_tv.setOnClickListener {
            good_attr_tv.performClick()
        }


        // 判断组选中
        good_checked_tv.apply {


            if (isEditMode) {
                mallCartItem.nativeEnable = true
                this.isEnabled = true
                this.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.selector_store_cart_select_all, 0, 0, 0)
            } else {
                // 没有库存 SKU被删除
                if (inventoryShortage || skuDelete == 1) {
                    mallCartItem.nativeEnable = false
                    this.isEnabled = false
                    this.setCompoundDrawablesRelativeWithIntrinsicBounds(R.mipmap.ic_checked_unavailable, 0, 0, 0)
                    return@apply
                }
            }


//            this.isChecked = checkGroupSelectAll(groupData)
            this.isChecked = if (isEditMode) {
                mallCartItem.nativeIsSelectEdit
            } else {
                mallCartItem.nativeIsSelect
            }

            setOnClickListener { view ->

                val currentIsSelect: Boolean

                if (isEditMode) {
                    currentIsSelect = mallCartItem.nativeIsSelectEdit
                    mallCartItem.nativeIsSelectEdit = !currentIsSelect
                    this.isChecked = mallCartItem.nativeIsSelectEdit
                } else {
                    currentIsSelect = mallCartItem.nativeIsSelect
                    mallCartItem.nativeIsSelect = !currentIsSelect
                    this.isChecked = mallCartItem.nativeIsSelect
                }


                if (mallCartItem.nativeIsSelect || mallCartItem.nativeIsSelectEdit) {
                    VibrateUtils.vibrate(10)
                }

                // 判断组内是否全选
                val isGroupSelectAll = checkGroupSelectAll(groupData)
                groupData.nativeIsSelect = isGroupSelectAll
                groupSelectAllTv.isChecked = isGroupSelectAll

                onChildSelectClick?.onChildSelected(this.isChecked)

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
            if (data.nativeEnable) {
                if (isEditMode) {
                    result.add(data.nativeIsSelectEdit)
                } else {
                    result.add(data.nativeIsSelect)
                }
            }

        }
        return !result.contains(false)
    }


    /**
     * 是否有可用子项
     */
    fun checkGroupAllEnable(groupData: MallCartGroup): Boolean {
        val result = ArrayList<Boolean>()
        result.clear()

        groupData.gwc.forEach { data ->
            if (isEditMode) {
                result.add(true)
            } else {
                result.add(data.nativeEnable)
            }
        }
        return result.contains(true)
    }

}