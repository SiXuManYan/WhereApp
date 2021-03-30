package com.jcs.where.features.gourmet.restaurant.list.filter.food;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.area.AreaResponse;
import com.jcs.where.api.response.category.Category;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Wangsw  2021/3/29 16:01.
 * 餐厅美食分类筛选
 */
public class FoodCategoryFilterAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    public FoodCategoryFilterAdapter() {
        super(R.layout.item_restaurant_fiter);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Category data) {
        TextView contentTv = holder.getView(R.id.content_tv);
        ImageView selectedIv = holder.getView(R.id.selected_iv);

        contentTv.setText(data.name);
        if (data.nativeIsSelected) {
            contentTv.setTextColor(ColorUtils.getColor(R.color.blue_4C9EF2));
            selectedIv.setVisibility(View.VISIBLE);
        } else {
            contentTv.setTextColor(ColorUtils.getColor(R.color.black_333333));
            selectedIv.setVisibility(View.GONE);
        }
    }
}
