package com.jcs.where.features.hotel;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.hotel.HotelListResponse;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.image.GlideRoundedCornersTransform;
import com.jcs.where.widget.ratingstar.RatingStarView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Wangsw  2021/3/16 14:58.
 */
public class HotelListAdapter extends BaseQuickAdapter<HotelListResponse, BaseViewHolder> implements LoadMoreModule {

    public HotelListAdapter() {
        super(R.layout.item_home_recommend_hotel_for_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, HotelListResponse hotelResponse) {
        bindHotelView(holder , hotelResponse);
    }

    /**
     * 酒店推荐
     */
    private void bindHotelView(BaseViewHolder holder, HotelListResponse data) {

        // 图片
        ImageView image_iv = holder.getView(R.id.image_iv);
        loadImage(data, image_iv);

        // 标题
        holder.setText(R.id.title_tv, data.name);

        // 星级
        TextView score_tv = holder.getView(R.id.score_tv);
        TextView score_retouch_tv = holder.getView(R.id.score_retouch_tv);
        RatingStarView star_view = holder.getView(R.id.star_view);

        float grade = data.grade;
        if (grade < 3.0) {
            star_view.setVisibility(View.INVISIBLE);
            score_tv.setVisibility(View.GONE);
            score_retouch_tv.setVisibility(View.GONE);
        } else {
            star_view.setVisibility(View.VISIBLE);
            score_tv.setVisibility(View.VISIBLE);
            star_view.setRating(grade);
            score_tv.setText(String.valueOf(grade));
            score_retouch_tv.setText(FeaturesUtil.getGradeRetouchString(grade));
        }

        // 评论数量
        holder.setText(R.id.comment_count_tv, StringUtils.getString(R.string.comment_count_format, data.comment_counts));

        // 距离 地点
        holder.setText(R.id.distance_tv, StringUtils.getString(R.string.distance_format, data.distance));
        holder.setText(R.id.location_tv, data.address);

        // tag
        LinearLayout tag_ll = holder.getView(R.id.tag_ll);
        initTag(data, tag_ll);

        // 价格
        TextView price_tv = holder.getView(R.id.price_tv);
        SpanUtils.with(price_tv)
                .append(StringUtils.getString(R.string.price_unit))
                .setFontSize(12, true)
                .append(data.price)
                .setFontSize(16, true)
                .create();
    }

    /**
     * 图片
     */
    private void loadImage(HotelListResponse data, ImageView image_iv) {
        RequestOptions options = RequestOptions.bitmapTransform(
                new GlideRoundedCornersTransform(10, GlideRoundedCornersTransform.CornerType.LEFT))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray);

        String image = "";
        if (data.images.size() > 0) {
            image = data.images.get(0);
            Glide.with(getContext()).load(image).apply(options).into(image_iv);
        } else {
            Glide.with(getContext()).load(R.mipmap.ic_empty_gray).apply(options).into(image_iv);
        }
    }

    /**
     * tag
     */
    private void initTag(HotelListResponse data, LinearLayout tag_ll) {

        tag_ll.removeAllViews();

        List<HotelListResponse.Tags> tags = data.tags;
        if (tags.size() <= 0) {
            return;
        }

        for (HotelListResponse.Tags tag : tags) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(SizeUtils.dp2px(2));
            TextView tv = new TextView(getContext());
            tv.setLayoutParams(params);
            tv.setPaddingRelative(SizeUtils.dp2px(4), SizeUtils.dp2px(2), SizeUtils.dp2px(4), SizeUtils.dp2px(2));
            tv.setTextColor(ColorUtils.getColor(R.color.blue_4C9EF2));
            tv.setTextSize(11);
            tv.setText(tag.name);
            tv.setBackgroundResource(R.drawable.shape_blue_stoke_radius_1);
            tag_ll.addView(tv);
        }

    }


}
