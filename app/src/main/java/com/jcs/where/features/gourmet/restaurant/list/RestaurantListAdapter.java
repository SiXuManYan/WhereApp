package com.jcs.where.features.gourmet.restaurant.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse;
import com.jcs.where.api.response.recommend.HomeRecommendResponse;
import com.jcs.where.utils.image.GlideRoundedCornersTransform;
import com.jcs.where.widget.ratingstar.RatingStarView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Wangsw  2021/3/25 14:13.
 * 餐厅列表
 */
public class RestaurantListAdapter extends BaseQuickAdapter<RestaurantResponse ,BaseViewHolder> implements LoadMoreModule {


    public RestaurantListAdapter() {
        super(R.layout.item_home_recommend_food_for_resraurant);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, RestaurantResponse data) {
        bindFoodView(holder, data);
    }

    private void bindFoodView(BaseViewHolder holder, RestaurantResponse data) {
        // 图片
        ImageView image_iv = holder.getView(R.id.image_iv);
        loadImage(data, image_iv);

        // 标题
        holder.setText(R.id.title_tv, data.title);

        // 星级
        RatingStarView star_view = holder.getView(R.id.star_view);
        TextView score_tv = holder.getView(R.id.score_tv);

        float grade = data.grade;
        if (grade < 3.0) {
            star_view.setVisibility(View.GONE);
            score_tv.setVisibility(View.GONE);
        } else {
            star_view.setVisibility(View.VISIBLE);
            score_tv.setVisibility(View.VISIBLE);
            star_view.setRating(grade);
            score_tv.setText(String.valueOf(grade));
        }

        // 评论数
        holder.setText(R.id.comment_count_tv, StringUtils.getString(R.string.comment_count_format2, data.comment_num));

        // 地域 、 餐厅类型
        holder.setText(R.id.area_name_tv, data.trading_area);
        holder.setText(R.id.restaurant_type_tv, data.type);

        // tag
        LinearLayout tag_ll = holder.getView(R.id.tag_ll);
        initTag(data, tag_ll);

        // 外卖
        LinearLayout takeaway_ll = holder.getView(R.id.take_ll);
        if (data.take_out_status == 2) {
            takeaway_ll.setVisibility(View.VISIBLE);
        } else {
            takeaway_ll.setVisibility(View.GONE);
        }
    }

    /**
     * 图片
     */
    private void loadImage(RestaurantResponse data, ImageView image_iv) {
        RequestOptions options = RequestOptions.bitmapTransform(
                new GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray);

        String image = "";


        if (data.images != null && data.images.size() > 0) {
            image = data.images.get(0);
            Glide.with(getContext()).load(image).apply(options).into(image_iv);
        } else {
            Glide.with(getContext()).load(R.mipmap.ic_empty_gray).apply(options).into(image_iv);
        }

    }


    /**
     * tag
     */
    private void initTag(RestaurantResponse data, LinearLayout tag_ll) {

        tag_ll.removeAllViews();

        ArrayList<String> tags = data.tags;
        if (tags.size() <= 0) {
            return;
        }

        for (String tag : tags) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(SizeUtils.dp2px(2));
            TextView tv = new TextView(getContext());
            tv.setLayoutParams(params);
            tv.setPaddingRelative(SizeUtils.dp2px(4), SizeUtils.dp2px(2), SizeUtils.dp2px(4), SizeUtils.dp2px(2));
            tv.setTextColor(ColorUtils.getColor(R.color.blue_4C9EF2));
            tv.setTextSize(11);
            tv.setText(tag);
            tv.setBackgroundResource(R.drawable.shape_blue_stoke_radius_1);
            tag_ll.addView(tv);
        }

    }

}
