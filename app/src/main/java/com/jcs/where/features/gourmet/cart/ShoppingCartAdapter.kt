package com.jcs.where.features.gourmet.cart

import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.checkbox.MaterialCheckBox
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.widget.NumberView

/**
 * Created by Wangsw  2021/4/7 16:34.
 */
class ShoppingCartAdapter : BaseQuickAdapter<ShoppingCartResponse, BaseViewHolder>(R.layout.item_shopping_cart), LoadMoreModule {


    private lateinit var restaurant_cb: MaterialCheckBox
    private lateinit var content_rv: RecyclerView


    private lateinit var childAdapter: ShoppingCartChildAdapter

    /**
     * 数量改变监听
     */
    var numberChangeListener: NumberView.OnValueChangerListener? = null


    override fun convert(holder: BaseViewHolder, data: ShoppingCartResponse) {

        restaurant_cb = holder.getView(R.id.restaurant_cb)
        content_rv = holder.getView(R.id.content_rv)


        // 内容
        content_rv.layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        if (content_rv.adapter == null) {
            childAdapter = ShoppingCartChildAdapter()
            childAdapter.setNewInstance(data.products)
            content_rv.adapter = childAdapter
            childAdapter.numberChangeListener = numberChangeListener
        }

        // 标题
        restaurant_cb.text = data.restaurant_name
        restaurant_cb.isChecked = data.nativeIsSelect
        restaurant_cb.setOnCheckedChangeListener { buttonView, isChecked ->
            childAdapter.data.forEach {
                it.nativeIsSelect = isChecked
            }
            childAdapter.notifyDataSetChanged()
        }
        childAdapter.checkedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->


            val result = ArrayList<Boolean>()

            childAdapter.data.forEach {
                result.add(it.nativeIsSelect)
            }

            if (isChecked) {
                // 选中
                restaurant_cb.isChecked = !result.contains(false)
            } else {
                restaurant_cb.isChecked = !result.contains(true)
            }


        }


    }


}