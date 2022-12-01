package com.jcs.where.features.job.home.tag

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R

/**
 * Created by Wangsw  2022/11/29 14:31.
 *
 */
class JobTagAdapter : BaseQuickAdapter<String , BaseViewHolder>(R.layout.item_job_tag) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tag_tv , item)
    }

}