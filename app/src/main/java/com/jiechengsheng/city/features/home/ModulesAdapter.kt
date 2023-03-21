package com.jiechengsheng.city.features.home

import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.ModulesResponse
import com.jiechengsheng.city.utils.CacheUtil
import com.jiechengsheng.city.utils.GlideUtil
import com.jiechengsheng.city.utils.SPKey

class ModulesAdapter : BaseQuickAdapter<ModulesResponse, BaseViewHolder>(R.layout.item_home_modules) {

    override fun convert(holder: BaseViewHolder, item: ModulesResponse) {
        val context = holder.itemView.context
        val parent = holder.getView<LinearLayout>(R.id.moduleLayout)
        val modules_name = holder.getView<TextView>(R.id.modules_name)

        if (!DeviceUtils.isTablet()) {
            val params = parent.layoutParams
            params.width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30f)) / 5
            params.height = SizeUtils.dp2px(70f)
            parent.layoutParams = params
        }


        GlideUtil.load(context, item.icon, holder.getView(R.id.modules_icon))
        modules_name.text = item.name
        modules_name.contentDescription = item.name

        if (item.id == 2 && CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID) == "") {
            // 存储企业黄页的一级分类id
            CacheUtil.cacheWithCurrentTimeByLanguage(SPKey.K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID, item.categories)
        }

    }
}