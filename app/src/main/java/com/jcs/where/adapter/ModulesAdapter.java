package com.jcs.where.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.ModulesResponse;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.SPKey;
import com.jcs.where.utils.SPUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ModulesAdapter extends BaseQuickAdapter<ModulesResponse, BaseViewHolder> {

    private int mItemWidth = 0;
    private int mItemHeight = 0;

    public ModulesAdapter(int itemWidth, int height) {
        super(R.layout.item_home_modules);
        this.mItemWidth = itemWidth;
        this.mItemHeight = height;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ModulesResponse modulesResponse) {
        Context context = baseViewHolder.itemView.getContext();
        View view = baseViewHolder.getView(R.id.moduleLayout);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(mItemWidth, mItemHeight);
        view.setLayoutParams(lp);
        GlideUtil.load(context, modulesResponse.getIcon(), baseViewHolder.getView(R.id.modules_icon));
        baseViewHolder.setText(R.id.modules_name, modulesResponse.getName());

        if (modulesResponse.getId() == 2 && CacheUtil.needUpdateBySpKeyByLanguage(SPKey.K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID).equals("")) {
            // 存储企业黄页的一级分类id
            CacheUtil.cacheWithCurrentTimeByLanguage(SPKey.K_YELLOW_PAGE_FIRST_LEVEL_CATEGORY_ID, modulesResponse.getCategories());
        }

    }
}
