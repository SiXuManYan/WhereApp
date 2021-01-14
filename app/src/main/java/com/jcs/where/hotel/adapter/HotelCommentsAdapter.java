package com.jcs.where.hotel.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.HotelCommentsResponse;
import com.jcs.where.utils.GlideUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HotelCommentsAdapter extends BaseQuickAdapter<HotelCommentsResponse.DataBean, BaseViewHolder> {
    private final List<Integer> mCommentImgResIds;

    public HotelCommentsAdapter(int layoutResId) {
        super(layoutResId);
        mCommentImgResIds = new ArrayList<>();
        mCommentImgResIds.add(R.id.commentIcon01);
        mCommentImgResIds.add(R.id.commentIcon02);
        mCommentImgResIds.add(R.id.commentIcon03);
        mCommentImgResIds.add(R.id.commentIcon04);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelCommentsResponse.DataBean dataBean) {
        baseViewHolder.setText(R.id.username, dataBean.getUsername());

        ImageView circleIcon = (ImageView) baseViewHolder.findView(R.id.circleIcon);
        if (circleIcon != null) {
            GlideUtil.load(getContext(), dataBean.getAvatar(), circleIcon);
        }

        baseViewHolder.setText(R.id.dateTv, dataBean.getCreated_at());
        TextView commentContentTv = baseViewHolder.findView(R.id.commentContent);
        commentContentTv.setText(dataBean.getContent());
        if (dataBean.is_select) {
            baseViewHolder.setText(R.id.fullText, getContext().getString(R.string.put_up));

            commentContentTv.setMaxLines(Integer.MAX_VALUE);
            commentContentTv.setEllipsize(null);
        } else {
            baseViewHolder.setText(R.id.fullText, getContext().getString(R.string.full_text));

            commentContentTv.setMaxLines(3);
            commentContentTv.setEllipsize(TextUtils.TruncateAt.END);
        }
        List<String> images = dataBean.getImages();

        int imgSize = 0;
        if (images != null && images.size() > 0) {
            imgSize = images.size();
        }

        for (int i = 0; i < 4; i++) {
            RoundedImageView commentIv = (RoundedImageView) baseViewHolder.getView(mCommentImgResIds.get(i));
            if (i < imgSize) {
                String img = images.get(i);
                commentIv.setVisibility(View.VISIBLE);
                GlideUtil.load(getContext(), img, commentIv);
            } else {
                if (imgSize == 0) {
                    commentIv.setVisibility(View.GONE);
                } else {
                    commentIv.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
