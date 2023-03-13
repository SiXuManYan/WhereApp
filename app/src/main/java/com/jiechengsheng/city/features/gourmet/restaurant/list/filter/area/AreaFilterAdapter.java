package com.jiechengsheng.city.features.gourmet.restaurant.list.filter.area;

import androidx.appcompat.widget.AppCompatCheckedTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.response.area.AreaResponse;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Wangsw  2021/3/29 16:01.
 * 餐厅商业区筛选
 */
public class AreaFilterAdapter extends BaseQuickAdapter<AreaResponse, BaseViewHolder> {

    public AreaFilterAdapter() {
        super(R.layout.item_restaurant_fiter);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, AreaResponse data) {
        AppCompatCheckedTextView contentTv = holder.getView(R.id.content_tv);

        contentTv.setText(data.name);

        boolean nativeIsSelected = data.nativeIsSelected;
        contentTv.setChecked(nativeIsSelected);
        contentTv.getPaint().setFakeBoldText(nativeIsSelected);
    }
}
