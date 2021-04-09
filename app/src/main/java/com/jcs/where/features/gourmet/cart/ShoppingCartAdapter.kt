package com.jcs.where.features.gourmet.cart

import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.widget.NumberView

/**
 * Created by Wangsw  2021/4/7 16:34.
 */
class ShoppingCartAdapter : BaseQuickAdapter<ShoppingCartResponse, BaseViewHolder>(R.layout.item_shopping_cart), LoadMoreModule {


    /**
     * 数量改变监听
     */
    var numberChangeListener: NumberView.OnValueChangerListener? = null


    override fun convert(holder: BaseViewHolder, data: ShoppingCartResponse) {

        val name_iv = holder.getView<ImageView>(R.id.name_iv)
        val name_tv = holder.getView<TextView>(R.id.name_tv)

        val content_rv = holder.getView<RecyclerView>(R.id.content_rv)


        // 内容
        content_rv.layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        if (content_rv.adapter != null) {
            return
        }


        val childAdapter = ShoppingCartChildAdapter()
        childAdapter.setNewInstance(data.products)
        content_rv.adapter = childAdapter
        childAdapter.numberChangeListener = numberChangeListener

        // 标题
        name_tv.text = data.restaurant_name

        if (data.nativeIsSelect) {
            name_iv.setImageResource(R.mipmap.ic_checked_orange)
        } else {
            name_iv.setImageResource(R.mipmap.ic_un_checked)
        }



        name_iv.setOnClickListener {

            val currentIsSelect = data.nativeIsSelect

            data.nativeIsSelect = !currentIsSelect
            if (data.nativeIsSelect) {
                name_iv.setImageResource(R.mipmap.ic_checked_orange)
            } else {
                name_iv.setImageResource(R.mipmap.ic_un_checked)
            }


            childAdapter.data.forEach {
                it.nativeIsSelect = data.nativeIsSelect
            }
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                childAdapter.notifyDataSetChanged()
            }, 50)


        }

        childAdapter.checkedChangeListener = object : ShoppingCartChildAdapter.OnChildContainerClick {
            override fun onClick(isChecked: Boolean) {

                val result = ArrayList<Boolean>()

                childAdapter.data.forEach {
                    result.add(it.nativeIsSelect)
                }

                if (isChecked) {
                    // 选中
                    val finals = !result.contains(false)
                    if (finals) {
                        name_iv.setImageResource(R.mipmap.ic_checked_orange)
                    } else {
                        name_iv.setImageResource(R.mipmap.ic_un_checked)
                    }

                    data.nativeIsSelect = finals

                } else {
                    val finals = !result.contains(true)
                    if (finals) {
                        name_iv.setImageResource(R.mipmap.ic_checked_orange)
                    } else {
                        name_iv.setImageResource(R.mipmap.ic_un_checked)
                    }
                    data.nativeIsSelect = finals
                }

            }

        }


    }


}