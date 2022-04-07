package com.jcs.where.features.store.history

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R

/**
 * Created by Wangsw  2021/6/7 14:31.
 *
 */
class SearchHistoryAdapter:BaseQuickAdapter<String , BaseViewHolder>(R.layout.item_search_history) {

    override fun convert(holder: BaseViewHolder, item: String) {
        val content_tv = holder.getView<TextView>(R.id.content_tv)
        content_tv.text = item
    }
}