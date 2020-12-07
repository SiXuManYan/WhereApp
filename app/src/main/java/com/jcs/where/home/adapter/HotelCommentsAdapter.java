package com.jcs.where.home.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.HotelCommentsResponse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HotelCommentsAdapter extends BaseQuickAdapter<HotelCommentsResponse.DataBean, BaseViewHolder> {

    public HotelCommentsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelCommentsResponse.DataBean dataBean) {
        baseViewHolder.setText(R.id.username, dataBean.getUsername());
        ImageView circleIcon = (ImageView) baseViewHolder.findView(R.id.circleIcon);
        if (circleIcon != null) {
            Glide.with(getContext()).load(dataBean.getAvatar()).into(circleIcon);
        }

        baseViewHolder.setText(R.id.dateTv, dataBean.getCreated_at());
        baseViewHolder.setText(R.id.commentContent, dataBean.getContent());
    }
}
