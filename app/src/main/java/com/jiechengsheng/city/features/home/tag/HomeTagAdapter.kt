package com.jiechengsheng.city.features.home.tag

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R

/**
 * Created by Wangsw  2022/7/17 10:49.
 * 首页 tag
 */
class HomeTagAdapter : BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_home_tag){

    override fun convert(holder: BaseViewHolder, item: String) {
        val child_tv = holder.getView<TextView>(R.id.home_tag_item)
        child_tv.text = item
    }
}