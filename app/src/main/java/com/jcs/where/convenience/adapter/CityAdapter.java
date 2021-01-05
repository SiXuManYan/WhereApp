package com.jcs.where.convenience.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.bean.CityResponse;

import org.jetbrains.annotations.NotNull;

/**
 * create by zyf on 2021/1/5 6:25 下午
 */
public class CityAdapter extends BaseQuickAdapter<CityResponse, BaseViewHolder> {
    private int mSelectedPosition = -1;

    public CityAdapter() {
        super(R.layout.item_city_to_selected);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, CityResponse cityResponse) {
        baseViewHolder.setText(R.id.cityNameTv, cityResponse.getName());

        if (mSelectedPosition != -1 && mSelectedPosition == baseViewHolder.getAdapterPosition()) {
            // 选中状态的item
            baseViewHolder.setTextColor(R.id.cityNameTv, getContext().getColor(R.color.blue_4D9FF2));
            baseViewHolder.setGone(R.id.cityCheckedIcon, false);
        } else {
            baseViewHolder.setTextColor(R.id.cityNameTv, getContext().getColor(R.color.black_333333));
            baseViewHolder.setGone(R.id.cityCheckedIcon, true);
        }
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }
}
