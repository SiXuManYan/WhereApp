package com.jcs.where.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModulesAdapter extends BaseQuickAdapter<ModulesResponse, BaseViewHolder> {

    public ModulesAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ModulesResponse modulesResponse) {
        Context context = baseViewHolder.itemView.getContext();
        Glide.with(context).load(modulesResponse.getIcon()).into((ImageView) baseViewHolder.getView(R.id.modules_icon));
        baseViewHolder.setText(R.id.modules_name, modulesResponse.getName());

        if (modulesResponse.getId() == 2 && CacheUtil.needUpdateBySpKey(SPKey.K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID).equals("")) {
            // 存储企业黄页的一级分类id
            CacheUtil.cacheWithCurrentTime(SPKey.K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID, modulesResponse.getCategories());
        }

    }
}
