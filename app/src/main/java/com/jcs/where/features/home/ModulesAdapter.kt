package com.jcs.where.features.home

import android.widget.LinearLayout
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.ModulesResponse
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.SPKey

class ModulesAdapter : BaseQuickAdapter<ModulesResponse, BaseViewHolder>(R.layout.item_home_modules) {

    override fun convert(holder: BaseViewHolder, item: ModulesResponse) {
        val context = holder.itemView.context
        val view = holder.getView<LinearLayout>(R.id.moduleLayout)

        if (!DeviceUtils.isTablet()) {
            val params = view.layoutParams
            params.width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30f)) / 5
            params.height = SizeUtils.dp2px(70f)
            view.layoutParams = params
        }


        GlideUtil.load(context, item.icon, holder.getView(R.id.modules_icon))
        holder.setText(R.id.modules_name, item.name)

        if (item.id == 2 && CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID) == "") {
            // 存储企业黄页的一级分类id
            CacheUtil.cacheWithCurrentTimeByLanguage(SPKey.K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID, item.categories)
        }
    }
}