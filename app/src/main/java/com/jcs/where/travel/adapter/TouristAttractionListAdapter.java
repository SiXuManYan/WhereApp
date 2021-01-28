package com.jcs.where.travel.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.MechanismResponse;
import com.jcs.where.api.response.TouristAttractionResponse;
import com.jcs.where.utils.GlideUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by zyf on 2020/12/28 9:13 PM
 */
public class TouristAttractionListAdapter extends BaseQuickAdapter<TouristAttractionResponse, BaseViewHolder> {
    public TouristAttractionListAdapter() {
        super(R.layout.item_mechainsm_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, TouristAttractionResponse touristAttractionResponse) {
        baseViewHolder.setText(R.id.mechanismTitleTv, touristAttractionResponse.getName());
        baseViewHolder.setText(R.id.mechanismAddressTv, touristAttractionResponse.getAddress());
        double distance = touristAttractionResponse.getDistance();;
        if (distance == 0) {
            baseViewHolder.setVisible(R.id.mechanismDistanceTv, false);
        } else {
            baseViewHolder.setVisible(R.id.mechanismDistanceTv, true);
            baseViewHolder.setText(R.id.mechanismDistanceTv, distance + "km");
        }
        ImageView mechanismIconIv = baseViewHolder.findView(R.id.mechanismIconIv);
        List<String> images = touristAttractionResponse.getImage();
        if (images != null && images.size() > 0 && mechanismIconIv != null) {
            GlideUtil.load(getContext(), images.get(0), mechanismIconIv);
        }
    }
}
