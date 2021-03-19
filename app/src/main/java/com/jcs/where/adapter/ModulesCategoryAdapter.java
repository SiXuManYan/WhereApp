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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModulesCategoryAdapter extends BaseQuickAdapter<CategoryResponse, BaseViewHolder> {


    public ModulesCategoryAdapter() {
        super(R.layout.item_home_modules);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, CategoryResponse data) {
        ImageView modules_icon = holder.getView(R.id.modules_icon);
        TextView modules_name = holder.getView(R.id.modules_name);

        LinearLayout view = holder.getView(R.id.moduleLayout);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = ScreenUtils.getScreenWidth() / 5 ;
        params.height = SizeUtils.dp2px(70);
        view.setLayoutParams(params);


        if (data.isNativeWebType) {
            modules_icon.setImageResource(R.mipmap.ic_tourism_sector);
            modules_name.setText(StringUtils.getString(R.string.tourism_management));
        } else {
            GlideUtil.load(getContext(), data.getIcon(), modules_icon);
            modules_name.setText(data.getName());
        }

    }
}
