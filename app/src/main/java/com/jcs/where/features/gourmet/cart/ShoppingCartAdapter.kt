package com.jcs.where.features.gourmet.cart

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.VibrateUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.widget.NumberView
import com.jcs.where.widget.list.DividerDecoration

/**
 * Created by Wangsw  2021/4/7 16:34.
 * 美食购物车、美食提交订单
 */
class ShoppingCartAdapter : BaseMultiItemQuickAdapter<ShoppingCartResponse, BaseViewHolder>(), LoadMoreModule {


    init {
        addItemType(ShoppingCartResponse.CONTENT_TYPE_COMMON, R.layout.item_shopping_cart)
        addItemType(ShoppingCartResponse.CONTENT_TYPE_COMMIT, R.layout.item_shopping_cart_for_commit)
    }

    /** 商品数量改变监听 */
    var numberChangeListener: NumberView.OnValueChangerListener? = null

    /** 选中全部监听 */
    var onUserSelectListener: OnUserSelectListener? = null


    /** 选中全部、子view选中监听 */
    interface OnUserSelectListener {

        /** 选中了item的标题 */
        fun onTitleSelectClick(isSelected: Boolean)

        /** 子选中了item内具体的某一项 */
        fun onChildSelectClick(isSelected: Boolean)
    }

    override fun convert(holder: BaseViewHolder, item: ShoppingCartResponse) {

        when (holder.itemViewType) {
            ShoppingCartResponse.CONTENT_TYPE_COMMON -> {
                bindTitleItem(holder, item)
                bindProductItemAndHandleSelect(holder, item)
            }

            ShoppingCartResponse.CONTENT_TYPE_COMMIT -> {
                bindTitleItem(holder, item)
                bindProductItem(holder, item)

                bindItemTotalPrice(holder, item)

            }
        }
    }


    /**
     * 标题
     */
    private fun bindTitleItem(holder: BaseViewHolder, data: ShoppingCartResponse) {
        val title = holder.getView<LinearLayout>(R.id.title_ll)
        if (holder.adapterPosition == 0 && holder.itemViewType == ShoppingCartResponse.CONTENT_TYPE_COMMON) {
            val layoutParams = title.layoutParams as LinearLayout.LayoutParams
            layoutParams.topMargin = SizeUtils.dp2px(10f)
            title.layoutParams = layoutParams
        }

        holder.setText(R.id.name_tv, data.restaurant_name)
    }


    /**
     * 菜品
     */
    private fun bindProductItem(holder: BaseViewHolder, data: ShoppingCartResponse): ShoppingCartChildAdapter {
        val contentRv = holder.getView<RecyclerView>(R.id.content_rv)
        val childAdapter = ShoppingCartChildAdapter().apply {
            setNewInstance(data.products)
            numberChangeListener = this@ShoppingCartAdapter.numberChangeListener
        }

        contentRv.apply {
            adapter = childAdapter
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(16f), 0, 0))
        }
        return childAdapter
    }


    /**
     * 处理选择菜品
     */
    private fun bindProductItemAndHandleSelect(holder: BaseViewHolder, data: ShoppingCartResponse) {

        // 菜品内容
        val childAdapter = bindProductItem(holder, data)

        // 全选
        val selectAllIv = holder.getView<ImageView>(R.id.select_all_iv)
        if (data.nativeIsSelect) {
            selectAllIv.setImageResource(R.mipmap.ic_checked_blue)
        } else {
            selectAllIv.setImageResource(R.mipmap.ic_un_checked)
        }


        // 子view 选中监听
        childAdapter.checkedChangeListener = object
            : ShoppingCartChildAdapter.OnChildContainerClick {
            override fun onClick(isChecked: Boolean) {

                val result = ArrayList<Boolean>()

                childAdapter.data.forEach {
                    result.add(it.nativeIsSelect)
                }

                if (isChecked) {
                    // 选中事件
                    val finals = !result.contains(false)
                    if (finals) {
                        // 全部选中
                        selectAllIv.setImageResource(R.mipmap.ic_checked_blue)
                    } else {
                        // 部分选中
                        selectAllIv.setImageResource(R.mipmap.ic_un_checked)
                    }

                    data.nativeIsSelect = finals

                } else {
                    // 取消选中事件
                    selectAllIv.setImageResource(R.mipmap.ic_un_checked)
                    data.nativeIsSelect = false
                }

                onUserSelectListener?.onChildSelectClick(data.nativeIsSelect)
            }


        }

        // 选中全部，取消全部
        selectAllIv.setOnClickListener {

            val currentIsSelect = data.nativeIsSelect

            data.nativeIsSelect = !currentIsSelect
            if (data.nativeIsSelect) {
                VibrateUtils.vibrate(10)
                selectAllIv.setImageResource(R.mipmap.ic_checked_blue)
            } else {
                selectAllIv.setImageResource(R.mipmap.ic_un_checked)
            }

            childAdapter.data.forEach {
                it.nativeIsSelect = data.nativeIsSelect
            }
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                childAdapter.notifyDataSetChanged()
            }, 50)

            onUserSelectListener?.onTitleSelectClick(data.nativeIsSelect)

        }
    }


    /**
     * 处理总价格
     */
    private fun bindItemTotalPrice(holder: BaseViewHolder, item: ShoppingCartResponse) {
        val totalPriceLl = holder.getView<LinearLayout>(R.id.item_total_price_ll)
        val totalPriceTv = holder.getView<TextView>(R.id.item_total_price_tv)

        val nativeTotalPrice = item.nativeTotalPrice
        if (nativeTotalPrice != null) {
            totalPriceLl.visibility = View.VISIBLE
            totalPriceTv.text =
                StringUtils.getString(R.string.price_unit_format, nativeTotalPrice.toPlainString())
        } else {
            totalPriceLl.visibility = View.GONE
        }
    }


}