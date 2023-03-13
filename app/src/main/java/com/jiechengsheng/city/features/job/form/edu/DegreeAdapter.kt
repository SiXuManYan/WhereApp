package com.jiechengsheng.city.features.job.form.edu

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.job.Degree

/**
 * Created by Wangsw  2022/10/20 17:34.
 * 学历列表
 */
class DegreeAdapter :BaseQuickAdapter<Degree, BaseViewHolder> (R.layout.item_degree){

    override fun convert(holder: BaseViewHolder, item: Degree) {
        val degree_tv = holder.getView<CheckedTextView>(R.id.degree_tv)

        degree_tv.text = item.educational_level
        degree_tv.isChecked = item.nativeSelected
    }


}