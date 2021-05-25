package com.jcs.where.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.utils.GlideUtil;

import org.jetbrains.annotations.NotNull;

public class ModulesCategoryAdapter2 extends BaseQuickAdapter<CategoryResponse, BaseViewHolder> {


    public ModulesCategoryAdapter2() {
        super(R.layout.item_home_modules_2);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, CategoryResponse data) {
        ImageView modules_icon = holder.getView(R.id.modules_icon);
        TextView modules_name = holder.getView(R.id.modules_name);


        if (data.isNativeWebType) {
            modules_icon.setImageResource(R.mipmap.ic_tourism_sector);
            modules_name.setText(StringUtils.getString(R.string.tourism_management));
        } else {
            GlideUtil.load(getContext(), data.getIcon(), modules_icon);
            modules_name.setText(data.getName());
        }

    }
}
