package com.jcs.where.mine.adapter


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.mine.adapter.LanguageAdapter.LanguageBean

/**
 * create by zyf on 2021/1/10 8:07 下午
 */
class LanguageAdapter : BaseQuickAdapter<LanguageBean, BaseViewHolder>(R.layout.item_language) {
    override fun convert(holder: BaseViewHolder, languageBean: LanguageBean) {
        holder.setText(R.id.languageTitleTv, languageBean.title)
        if (languageBean.isSelected) {
            holder.setGone(R.id.languageCheckedIcon, false)
        } else {
            holder.setGone(R.id.languageCheckedIcon, true)
        }
        if (holder.adapterPosition == itemCount - 1) {
            holder.setGone(R.id.bottomLine, true)
        }
    }

    class LanguageBean {
        var title: String? = null

        @JvmField
        var local: String? = null

        @JvmField
        var isSelected = false

        constructor() {}
        constructor(title: String?, local: String?, isSelected: Boolean) {
            this.title = title
            this.local = local
            this.isSelected = isSelected
        }
    }
}