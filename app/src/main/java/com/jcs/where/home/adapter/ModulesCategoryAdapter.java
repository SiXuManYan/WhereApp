package com.jcs.where.home.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.CategoryResponse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModulesCategoryAdapter extends BaseQuickAdapter<CategoryResponse, BaseViewHolder> {
    public ModulesCategoryAdapter(int layoutResId, @Nullable List<CategoryResponse> data) {
        super(layoutResId, data);
    }

    public ModulesCategoryAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, CategoryResponse modulesResponse) {
        Context context = baseViewHolder.itemView.getContext();
        Glide.with(context).load(modulesResponse.getIcon()).into((ImageView) baseViewHolder.getView(R.id.modules_icon));
        baseViewHolder.setText(R.id.modules_name, modulesResponse.getName());
    }
}
