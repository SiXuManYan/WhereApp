package com.jcs.where.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.widget.LabelView;
import com.jcs.where.widget.StarView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TravelStayHotelAdapter extends BaseQuickAdapter<HotelResponse, BaseViewHolder> {
    public TravelStayHotelAdapter(int layoutResId, @Nullable List<HotelResponse> data) {
        super(layoutResId, data);
    }

    public TravelStayHotelAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelResponse hotelResponse) {

        TextView scoreTv = baseViewHolder.findView(R.id.scoreTv);
        scoreTv.setText(hotelResponse.getGrade() + "");

        Context context = baseViewHolder.itemView.getContext();
        List<String> images = hotelResponse.getImages();
        if (images != null && images.size() > 0) {
            GlideUtil.load(getContext(), images.get(0), baseViewHolder.getView(R.id.hotelIcon));
        }
        baseViewHolder.setText(R.id.hotelName, hotelResponse.getName());
        baseViewHolder.setText(R.id.distanceTv, String.valueOf(hotelResponse.getDistance()));
        baseViewHolder.setText(R.id.locationTv, hotelResponse.getAddress());
        baseViewHolder.setText(R.id.priceTv, String.format(getContext().getString(R.string.price_above_number), hotelResponse.getPrice()));
        String commentNumberText = String.format(getContext().getString(R.string.comment_num_prompt), hotelResponse.getComment_counts());
        baseViewHolder.setText(R.id.commitTv, commentNumberText);

        StarView startView = baseViewHolder.findView(R.id.starView);
        startView.setStartNum(hotelResponse.getGrade());
        LabelView labelView = baseViewHolder.findView(R.id.labelView);
        List<LabelView.LabelNameInterface> labels = new ArrayList<>();
        List<HotelResponse.TagsBean> tags = hotelResponse.getTags();
        for (int i = 0; i < tags.size(); i++) {
            labels.add(tags.get(i));
        }
        labelView.setLabels(labels);
    }
}
