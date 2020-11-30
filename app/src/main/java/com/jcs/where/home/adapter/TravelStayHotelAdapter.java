package com.jcs.where.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.HotelResponse;
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

        Context context = baseViewHolder.itemView.getContext();
        List<String> images = hotelResponse.getImages();
        if (images != null && images.size() > 0) {
            Glide.with(context).load(images.get(0)).into((ImageView) baseViewHolder.getView(R.id.hotelIcon));
        }
        baseViewHolder.setText(R.id.hotelName, hotelResponse.getName());
        baseViewHolder.setText(R.id.distanceTv, String.valueOf(hotelResponse.getDistance()));
        baseViewHolder.setText(R.id.locationTv, hotelResponse.getAddress());
        baseViewHolder.setText(R.id.priceTv, hotelResponse.getPrice() + "起");
        baseViewHolder.setText(R.id.commentTv, hotelResponse.getComment_counts() + "条评论");

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
