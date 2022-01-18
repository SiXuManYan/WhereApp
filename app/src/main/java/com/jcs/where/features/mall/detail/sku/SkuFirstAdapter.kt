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

        val secondAdapter = SkuSecondAdapter().apply {
            setNewInstance(item.value)

        }

        secondAdapter.setOnItemClickListener { _, _, position ->

            val allData = secondAdapter.data
            val secondDataItem = allData[position]

            when (secondDataItem.nativeIsSelected) {
                0 -> {
                    allData.forEachIndexed { index, child ->
                        if (child.nativeIsSelected != 2) {
                            if (child == secondDataItem) {
                                child.nativeIsSelected = 1
                            } else {
                                child.nativeIsSelected = 0
                            }
                        }
                    }
                    secondAdapter.notifyDataSetChanged()
                    targetGoodItemClickCallBack?.onItemClick2(secondDataItem)

                }
                1 -> {
                    allData.forEachIndexed { index, child ->
                        if (child.nativeIsSelected != 2) {
                            /*if (child == secondDataItem) {
                                child.nativeIsSelected = 0
                            }*/
//                            else {
//                                child.nativeIsSelected = 1
//                            }
                            child.nativeIsSelected = 0
                        }
                    }
                    secondAdapter.notifyDataSetChanged()

                    targetGoodItemClickCallBack?.onItemClick2(secondDataItem)
                }
                else -> {
                }
            }
        }

        secondRv.apply {
            adapter = secondAdapter
            layoutManager = MyLayoutManager()
        }
    }


    /**
     * 获取所有用户选中的数量
     */
    fun getAllUserSelectedSize(mAdapter: SkuFirstAdapter): Int {
        val value = ArrayList<MallAttributeValue>()
        mAdapter.data.forEachIndexed { index, group ->
            // 每一组
            group.value.forEach { item ->

                if (item.nativeIsSelected == 1) {
                    value.add(item)
                }
            }
        }

        return value.size
    }


    /**
     * 获取所有用户选中的index
     */
    fun getUserSelectedIndex(): Int {
        var resultIndex = -1

        val value = ArrayList<MallAttributeValue>()
        data.forEachIndexed { index, group ->
            // 每一组
            group.value.forEach { item ->

                if (item.nativeIsSelected == 1) {
                    resultIndex = index
                }
            }
        }

        return resultIndex
    }

}

interface TargetGoodItemClickCallBack {
    fun onItemClick2(userSelect: MallAttributeValue)

}