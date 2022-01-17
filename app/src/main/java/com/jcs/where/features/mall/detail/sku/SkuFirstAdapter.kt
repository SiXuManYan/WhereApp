package com.jcs.where.features.mall.detail.sku

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallAttribute
import com.jcs.where.api.response.mall.MallAttributeValue
import com.jcs.where.view.MyLayoutManager

/**
 * Created by Wangsw  2021/12/13 16:49.
 *
 */
class SkuFirstAdapter : BaseQuickAdapter<MallAttribute, BaseViewHolder>(R.layout.item_sku_first) {

    var targetGoodItemClickCallBack: TargetGoodItemClickCallBack? = null

    override fun convert(holder: BaseViewHolder, item: MallAttribute) {
        holder.setText(R.id.sku_title_tv, item.key)
        val view = holder.getView<RecyclerView>(R.id.second_rv)

        initChildItem(holder, item, view)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initChildItem(holder: BaseViewHolder, item: MallAttribute, secondRv: RecyclerView) {

        val contentAdapter = SkuSecondAdapter().apply {
            setNewInstance(item.value)

        }

        contentAdapter.setOnItemClickListener { _, _, position ->

            val allData = contentAdapter.data
            val mallAttributeValue = allData[position]

            when (mallAttributeValue.nativeIsSelected) {
                0 -> {
                    allData.forEachIndexed { index, child ->
                        if (child.nativeIsSelected != 2) {
                            if (index == position) {
                                child.nativeIsSelected = 1
                            } else {
                                child.nativeIsSelected = 0
                            }
                        }
                    }
                    contentAdapter.notifyDataSetChanged()
                    targetGoodItemClickCallBack?.onItemClick2(mallAttributeValue)
                }
                1 -> {
                    allData.forEachIndexed { index, child ->
                        if (child.nativeIsSelected != 2) {
                            if (index == position) {
                                child.nativeIsSelected = 0
                            } else {
                                child.nativeIsSelected = 1
                            }
                        }
                    }
                    contentAdapter.notifyDataSetChanged()
                    targetGoodItemClickCallBack?.onItemClick2(mallAttributeValue)
                }
                else -> {
                }
            }
        }

        secondRv.apply {
            adapter = contentAdapter
            layoutManager = MyLayoutManager()
        }
    }
}

interface TargetGoodItemClickCallBack {
    fun onItemClick2(userSelect: MallAttributeValue)

}