package com.jcs.where.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.utils.FeaturesUtil;
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
    protected void convert(@NotNull BaseViewHolder holder, HotelResponse hotelResponse) {

        TextView scoreTv = holder.findView(R.id.scoreTv);
        scoreTv.setText(hotelResponse.getGrade() + "");

        Context context = holder.itemView.getContext();
        List<String> images = hotelResponse.getImages();
        if (images != null && images.size() > 0) {
            GlideUtil.load(getContext(), images.get(0), holder.getView(R.id.hotelIcon));
        }
        holder.setText(R.id.hotelName, hotelResponse.getName());
        holder.setText(R.id.distanceTv, String.valueOf(hotelResponse.getDistance()));
        holder.setText(R.id.locationTv, hotelResponse.getAddress());
        holder.setText(R.id.priceTv, String.format(getContext().getString(R.string.price_above_number), hotelResponse.getPrice()));
        String commentNumberText = String.format(getContext().getString(R.string.comment_num_prompt), hotelResponse.getComment_counts());
        holder.setText(R.id.commitTv, commentNumberText);

        StarView startView = holder.findView(R.id.starView);
        startView.setStartNum(hotelResponse.getGrade());
        LabelView labelView = holder.findView(R.id.labelView);
        List<LabelView.LabelNameInterface> labels = new ArrayList<>();
        List<HotelResponse.TagsBean> tags = hotelResponse.getTags();
        for (int i = 0; i < tags.size(); i++) {
            labels.add(tags.get(i));
        }
        labelView.setLabels(labels);

        TextView evaluationTv = holder.getView(R.id.evaluationTv);
        evaluationTv.setText(FeaturesUtil.getGradeRetouchString((float) hotelResponse.getGrade()));

    }
}
