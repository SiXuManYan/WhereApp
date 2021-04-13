package com.jcs.where.integral.child.task;

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
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.response.recommend.HomeRecommendResponse;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.image.GlideRoundedCornersTransform;
import com.jcs.where.widget.ratingstar.RatingStarView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Wangsw  2021/3/2 11:39.
 * 推荐列表
 */
public class HomeRecommendAdapter extends BaseMultiItemQuickAdapter<HomeRecommendResponse, BaseViewHolder> implements LoadMoreModule {


    public HomeRecommendAdapter(boolean newType) {
        super();
        if (newType) {
            addItemType(HomeRecommendResponse.MODULE_TYPE_1_HOTEL, R.layout.item_home_recommend_hotel_2);
            addItemType(HomeRecommendResponse.MODULE_TYPE_2_SERVICE, R.layout.item_home_recommend_service_2);
            addItemType(HomeRecommendResponse.MODULE_TYPE_3_FOOD, R.layout.item_home_recommend_food_2);
            addItemType(HomeRecommendResponse.MODULE_TYPE_4_TRAVEL, R.layout.item_home_recommend_travel_2);
        } else {
            addItemType(HomeRecommendResponse.MODULE_TYPE_1_HOTEL, R.layout.item_home_recommend_hotel);
            addItemType(HomeRecommendResponse.MODULE_TYPE_2_SERVICE, R.layout.item_home_recommend_service);
            addItemType(HomeRecommendResponse.MODULE_TYPE_3_FOOD, R.layout.item_home_recommend_food);
            addItemType(HomeRecommendResponse.MODULE_TYPE_4_TRAVEL, R.layout.item_home_recommend_travel);
        }
    }


    @Override
    protected void convert(@NotNull BaseViewHolder holder, HomeRecommendResponse data) {

        int itemViewType = holder.getItemViewType();
        switch (itemViewType) {
            case HomeRecommendResponse.MODULE_TYPE_1_HOTEL:
                bindHotelView(holder, data);
                break;
            case HomeRecommendResponse.MODULE_TYPE_2_SERVICE:
                bindServiceView(holder, data);
                break;
            case HomeRecommendResponse.MODULE_TYPE_3_FOOD:
                bindFoodView(holder, data);
                break;
            case HomeRecommendResponse.MODULE_TYPE_4_TRAVEL:
                bindTraverView(holder, data);
                break;
            default:
                break;
        }


    }

    /**
     * 美食
     */
    private void bindFoodView(BaseViewHolder holder, HomeRecommendResponse data) {

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
        holder.setText(R.id.area_name_tv, data.area_name);
        holder.setText(R.id.restaurant_type_tv, data.restaurant_type);

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

        // 人均
        TextView per_price_tv = holder.getView(R.id.per_price_tv);
        per_price_tv.setText(StringUtils.getString(R.string.per_price_format, data.per_price));
    }

    /**
     * 旅游view
     */
    private void bindTraverView(BaseViewHolder holder, HomeRecommendResponse data) {

        // 图片
        ImageView image_iv = holder.getView(R.id.image_iv);
        loadImage(data, image_iv);

        // 标题
        holder.setText(R.id.title_tv, data.title);

        // 评分
        holder.setText(R.id.score_tv, String.valueOf(data.grade));
        holder.setText(R.id.comment_count_tv, StringUtils.getString(R.string.comment_count_format, data.comment_num));

        // tag
        LinearLayout tag_ll = holder.getView(R.id.tag_ll);
        initTag(data, tag_ll);

        // 地址
        holder.setText(R.id.address_tv, data.address);
        holder.setText(R.id.food_distance_tv, StringUtils.getString(R.string.distance_format, data.distance));

    }

    /**
     * 综合服务
     *
     * @param holder
     * @param data
     */
    private void bindServiceView(BaseViewHolder holder, HomeRecommendResponse data) {

        // 图片
        ImageView image_iv = holder.getView(R.id.image_iv);
        loadImage(data, image_iv);

        // 标题
        holder.setText(R.id.title_tv, data.title);

        // tag
        LinearLayout tag_ll = holder.getView(R.id.tag_ll);
        initTag(data, tag_ll);

        // 地址
        holder.setText(R.id.address_tv, data.address);
    }

    /**
     * 酒店推荐
     */
    private void bindHotelView(BaseViewHolder holder, HomeRecommendResponse data) {

        // 图片
        ImageView image_iv = holder.getView(R.id.image_iv);
        loadImage(data, image_iv);

        // 标题
        holder.setText(R.id.title_tv, data.title);

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
        holder.setText(R.id.comment_count_tv, StringUtils.getString(R.string.comment_count_format, data.comment_num));

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
    private void loadImage(HomeRecommendResponse data, ImageView image_iv) {
        RequestOptions options = RequestOptions.bitmapTransform(
                new GlideRoundedCornersTransform(10, GlideRoundedCornersTransform.CornerType.LEFT))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray);

        String image = "";


        if (data.images != null && data.images.length > 0) {
            image = data.images[0];
            Glide.with(getContext()).load(image).apply(options).into(image_iv);
        } else {
            Glide.with(getContext()).load(R.mipmap.ic_empty_gray).apply(options).into(image_iv);
        }

    }

    /**
     * tag
     */
    private void initTag(HomeRecommendResponse data, LinearLayout tag_ll) {

        tag_ll.removeAllViews();

        String[] tags = data.tags;
        if (tags.length <= 0) {
            return;
        }

        for (String tag : tags) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(SizeUtils.dp2px(2));
            TextView tv = new TextView(getContext());
            tv.setLayoutParams(params);
            tv.setPaddingRelative(SizeUtils.dp2px(4), SizeUtils.dp2px(1), SizeUtils.dp2px(4), SizeUtils.dp2px(1));
            tv.setTextColor(ColorUtils.getColor(R.color.blue_4C9EF2));
            tv.setTextSize(11);
            tv.setText(tag);
            tv.setBackgroundResource(R.drawable.shape_blue_stoke_radius_1);
            tv.setSingleLine(true);
            tag_ll.addView(tv);
        }

    }


}
