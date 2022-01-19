package com.jcs.where.features.mall.detail.sku

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallAttribute
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
                    targetGoodItemClickCallBack?.onAttrItemClick()
                }
                1 -> {
                    allData.forEachIndexed { index, child ->
                        if (child.nativeIsSelected != 2) {
                            child.nativeIsSelected = 0
                        }
                    }
                    secondAdapter.notifyDataSetChanged()

                    targetGoodItemClickCallBack?.onAttrItemClick()
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
     * 获取用户选中的组坐标
     */
    fun getFirstUserSelectedIndex(): Int {
        var resultIndex = -1

        data.forEachIndexed { index, group ->
            // 每一组
            group.value.forEach { item ->

                if (item.nativeIsSelected == 1) {
                    resultIndex = index
                    return@forEachIndexed
                }
            }
        }

        return resultIndex
    }

}

interface TargetGoodItemClickCallBack {
    fun onAttrItemClick()

}