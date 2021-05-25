package com.jcs.where.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.SPKey;

import org.jetbrains.annotations.NotNull;

public class ModulesAdapter extends BaseQuickAdapter<ModulesResponse, BaseViewHolder> {



    public ModulesAdapter() {
        super(R.layout.item_home_modules);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ModulesResponse modulesResponse) {
        Context context = holder.itemView.getContext();


        LinearLayout view = holder.getView(R.id.moduleLayout);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(20)) / 5;
        params.height = SizeUtils.dp2px(70);
        view.setLayoutParams(params);


        GlideUtil.load(context, modulesResponse.getIcon(), holder.getView(R.id.modules_icon));
        holder.setText(R.id.modules_name, modulesResponse.getName());

        if (modulesResponse.getId() == 2 && CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID).equals("")) {
            // 存储企业黄页的一级分类id
            CacheUtil.cacheWithCurrentTimeByLanguage(SPKey.K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID, modulesResponse.getCategories());
        }

    }
}
