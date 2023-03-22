package com.jiechengsheng.city.features.job.collection

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.job.CompanyInfo
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2023/3/21 15:37.
 *
 */
class CompanyAdapter : BaseQuickAdapter<CompanyInfo, BaseViewHolder>(R.layout.item_collection_campany),LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: CompanyInfo) {

        val logoIv = holder.getView<ImageView>(R.id.company_logo_iv)
        val nameTv = holder.getView<TextView>(R.id.company_name_tv)
        val typeTv = holder.getView<TextView>(R.id.type_tv)

        GlideUtil.load(context, item.logo, logoIv)
        nameTv.text = item.company_title
        typeTv.text = item.company_type + " | " + item.company_size


    }
}