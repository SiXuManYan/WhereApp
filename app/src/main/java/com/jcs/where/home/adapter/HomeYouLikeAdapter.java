package com.jcs.where.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.HotelResponse;
import com.jcs.where.utils.GlideUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by zyf on 2021/1/11 12:06 下午
 */
public class HomeYouLikeAdapter extends BaseQuickAdapter<HotelResponse, BaseViewHolder> {
    private String mCommentNumPrompt;

    public HomeYouLikeAdapter() {
        super(R.layout.item_guess_you_like_hotel);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelResponse hotelResponse) {
        if (mCommentNumPrompt == null) {
            mCommentNumPrompt = getContext().getString(R.string.comment_num_prompt);
        }
        baseViewHolder.setText(R.id.youLikeNameTv, hotelResponse.getName());
        baseViewHolder.setText(R.id.youLikeAddressTv, hotelResponse.getAddress());
        baseViewHolder.setText(R.id.youLikePriceTv, String.valueOf(hotelResponse.getPrice()));
        baseViewHolder.setText(R.id.youLikeCommentTv, String.format(mCommentNumPrompt, hotelResponse.getComment_counts()));
        baseViewHolder.setText(R.id.youLikeScoreTv, String.valueOf(hotelResponse.getGrade()));
        RoundedImageView icon = baseViewHolder.findView(R.id.youLikeIcon);
        List<String> images = hotelResponse.getImages();
        if (icon != null && images.size() > 0) {
            GlideUtil.load(getContext(), images.get(0), icon);
        }

    }
}
