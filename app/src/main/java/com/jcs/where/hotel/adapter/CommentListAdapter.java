package com.jcs.where.hotel.adapter;

import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.CommentResponse;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.widget.StarView;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends BaseQuickAdapter<CommentResponse, BaseViewHolder> {
    private final List<Integer> mCommentImgResIds;

    public CommentListAdapter() {
        super(R.layout.item_comment_list);
        mCommentImgResIds = new ArrayList<>();
        mCommentImgResIds.add(R.id.commentIcon01);
        mCommentImgResIds.add(R.id.commentIcon02);
        mCommentImgResIds.add(R.id.commentIcon03);
        mCommentImgResIds.add(R.id.commentIcon04);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, CommentResponse dataBean) {
        baseViewHolder.setText(R.id.username, dataBean.getUsername());

        ImageView circleIcon = (ImageView) baseViewHolder.findView(R.id.circleIcon);
        if (circleIcon != null) {
            GlideUtil.load(getContext(), dataBean.getAvatar(), circleIcon);
        }

        StarView starView = baseViewHolder.findView(R.id.starView);
        Integer starLevel = dataBean.getStarLevel();
        if (starView != null && starLevel != null) {
            starView.setStartNum(starLevel);
        }

        baseViewHolder.setText(R.id.dateTv, dataBean.getCreatedAt());
        TextView commentContentTv = baseViewHolder.findView(R.id.commentContent);
        commentContentTv.setText(dataBean.getContent());
        if (dataBean.needFullText == null) {
            commentContentTv.post(() -> {
                int ellipsisCount = commentContentTv.getLayout().getEllipsisCount(2);
                if (ellipsisCount > 0) {
                    baseViewHolder.setGone(R.id.fullText, false);
                    dataBean.needFullText = true;
                    if (dataBean.is_select) {
                        baseViewHolder.setText(R.id.fullText, getContext().getString(R.string.put_up));
                        commentContentTv.setMaxLines(Integer.MAX_VALUE);
                        commentContentTv.setEllipsize(null);
                    } else {
                        baseViewHolder.setText(R.id.fullText, getContext().getString(R.string.full_text));
                        commentContentTv.setMaxLines(3);
                        commentContentTv.setEllipsize(TextUtils.TruncateAt.END);
                    }
                } else {
                    baseViewHolder.setGone(R.id.fullText, true);
                    dataBean.needFullText = false;
                }
            });
        } else {
            baseViewHolder.setGone(R.id.fullText, !dataBean.needFullText);
        }
        List<String> images = dataBean.getImages();

        deployCommentIcon(baseViewHolder, images);
    }

    private void deployCommentIcon(@NotNull BaseViewHolder baseViewHolder, List<String> images) {
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
