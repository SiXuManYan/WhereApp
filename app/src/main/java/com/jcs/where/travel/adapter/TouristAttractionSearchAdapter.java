package com.jcs.where.travel.adapter;

import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.TouristAttractionResponse;
import com.jcs.where.utils.MapMarkerUtil;

import org.jetbrains.annotations.NotNull;

/**
 * create by zyf on 2021/1/7 3:39 下午
 */
public class TouristAttractionSearchAdapter extends BaseQuickAdapter<MapMarkerUtil.IMapData, BaseViewHolder> {
    public TouristAttractionSearchAdapter() {
        super(R.layout.item_search);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MapMarkerUtil.IMapData mapData) {
        Log.e("TouristSearchAdapter", "convert: " + mapData);
        baseViewHolder.setText(R.id.searchTitleTv, mapData.getName());
        baseViewHolder.setGone(R.id.line, baseViewHolder.getAdapterPosition() == getItemCount() - 1);
    }

}
