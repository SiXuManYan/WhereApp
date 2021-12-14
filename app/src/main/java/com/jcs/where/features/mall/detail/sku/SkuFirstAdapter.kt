package com.jcs.where.features.mall.detail.sku

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
class SkuFirstAdapter :BaseQuickAdapter<MallAttribute,BaseViewHolder>(R.layout.item_sku_first) {


    override fun convert(holder: BaseViewHolder, item: MallAttribute) {
        holder.setText(R.id.sku_title_tv,item.key)
        val view = holder.getView<RecyclerView>(R.id.second_rv)

        initChildItem(holder,item,view)
    }

    private fun initChildItem(holder: BaseViewHolder, item: MallAttribute, secondRv: RecyclerView) {


        val contentAdapter = SkuSecondAdapter().apply {
            setNewInstance(item.value)
        }

        secondRv.apply {
            adapter = contentAdapter
            layoutManager = MyLayoutManager()
        }

    }
}