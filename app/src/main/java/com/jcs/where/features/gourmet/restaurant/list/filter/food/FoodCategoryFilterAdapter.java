package com.jcs.where.features.gourmet.restaurant.list.filter.food;

import androidx.appcompat.widget.AppCompatCheckedTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
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
        AppCompatCheckedTextView contentTv = holder.getView(R.id.content_tv);

        contentTv.setText(data.name);
        boolean nativeIsSelected = data.nativeIsSelected;
        contentTv.setChecked(nativeIsSelected);
        contentTv.getPaint().setFakeBoldText(nativeIsSelected);
    }
}
