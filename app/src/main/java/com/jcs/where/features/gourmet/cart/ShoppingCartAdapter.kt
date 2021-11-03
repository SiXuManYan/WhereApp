package com.jcs.where.features.gourmet.cart

import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.VibrateUtils
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

      var holder: BaseViewHolder? = null

    /**
     * 商品数量改变监听
     */
    var numberChangeListener: NumberView.OnValueChangerListener? = null

    /**
     * 选中全部监听
     */
    var onUserSelectListener: OnUserSelectListener? = null

    lateinit var mChildAdapter: ShoppingCartChildAdapter


    lateinit var m_select_all_iv: ImageView


    /**
     * 选中全部、子view选中监听
     */
    interface OnUserSelectListener {
        /**
         * 选中了item的标题
         */
        fun onTitleSelectClick(isSelected: Boolean)

        /**
         * 子选中了item内具体的某一项
         */
        fun onChildSelectClick(isSelected: Boolean)
    }

    override fun convert(holder: BaseViewHolder, data: ShoppingCartResponse) {

        this.holder = holder;
        val title_ll = holder.getView<LinearLayout>(R.id.title_ll)
        val select_all_iv = holder.getView<ImageView>(R.id.select_all_iv)
        m_select_all_iv = select_all_iv

        val name_tv = holder.getView<TextView>(R.id.name_tv)
        val content_rv = holder.getView<RecyclerView>(R.id.content_rv)

        if (holder.adapterPosition == 0) {
            val layoutParams = title_ll.layoutParams as LinearLayout.LayoutParams
            layoutParams.topMargin = SizeUtils.dp2px(10f)
            title_ll.layoutParams = layoutParams
        }


        // 内容
        content_rv.layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        // 标题
        name_tv.text = data.restaurant_name

        if (data.nativeIsSelect) {
            select_all_iv.setImageResource(R.mipmap.ic_checked_blue)
        } else {
            select_all_iv.setImageResource(R.mipmap.ic_un_checked)
        }

        val childAdapter = ShoppingCartChildAdapter()
        mChildAdapter = childAdapter


        childAdapter.setNewInstance(data.products)
        content_rv.adapter = childAdapter
        childAdapter.numberChangeListener = numberChangeListener

        // 选中全部，取消全部
        select_all_iv.setOnClickListener {

            val currentIsSelect = data.nativeIsSelect

            data.nativeIsSelect = !currentIsSelect
            if (data.nativeIsSelect) {
                VibrateUtils.vibrate(10)
                select_all_iv.setImageResource(R.mipmap.ic_checked_blue)
            } else {
                select_all_iv.setImageResource(R.mipmap.ic_un_checked)
            }

            childAdapter.data.forEach {
                it.nativeIsSelect = data.nativeIsSelect
            }
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                childAdapter.notifyDataSetChanged()
            }, 50)



            onUserSelectListener?.onTitleSelectClick(data.nativeIsSelect)

        }

        // 子view 选中监听
        childAdapter.checkedChangeListener = object : ShoppingCartChildAdapter.OnChildContainerClick {
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
                        select_all_iv.setImageResource(R.mipmap.ic_checked_blue)
                    } else {
                        // 部分选中
                        select_all_iv.setImageResource(R.mipmap.ic_un_checked)
                    }

                    data.nativeIsSelect = finals

                } else {
                    // 取消选中事件
                    select_all_iv.setImageResource(R.mipmap.ic_un_checked)
                    data.nativeIsSelect = false
                }

                onUserSelectListener?.onChildSelectClick(data.nativeIsSelect)
            }


        }

    }


}