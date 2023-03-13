package com.jiechengsheng.city.features.mall.detail.sku

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallAttribute
import com.jiechengsheng.city.api.response.mall.MallAttributeValue
import com.jiechengsheng.city.view.MyLayoutManager

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
            if (secondDataItem.nativeEnable) {
                allData.forEach {
                    if (it == secondDataItem) {
                        it.nativeSelected = !it.nativeSelected
                    } else {
                        it.nativeSelected = false
                    }
                }
                secondAdapter.notifyDataSetChanged()
                targetGoodItemClickCallBack?.onAttrItemClick2(secondDataItem)
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
    fun onAttrItemClick2(secondDataItem: MallAttributeValue?)

}