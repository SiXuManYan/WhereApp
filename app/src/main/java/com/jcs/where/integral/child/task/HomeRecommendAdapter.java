package com.jcs.where.integral.child.task;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
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


    public HomeRecommendAdapter() {
        super();
        addItemType(HomeRecommendResponse.MODULE_TYPE_1_HOTEL, R.layout.item_home_recommend_hotel);
        addItemType(HomeRecommendResponse.MODULE_TYPE_2_SERVICE, R.layout.item_home_recommend_service);
        addItemType(HomeRecommendResponse.MODULE_TYPE_3_FOOD, R.layout.item_home_recommend_food);
        addItemType(HomeRecommendResponse.MODULE_TYPE_4_TRAVEL, R.layout.item_home_recommend_travel);
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
            star_view.setVisibility(View.INVISIBLE);
            score_tv.setVisibility(View.GONE);
        } else {
            star_view.setVisibility(View.VISIBLE);
            score_tv.setVisibility(View.VISIBLE);
            star_view.setRating(grade);
            score_tv.setText(String.valueOf(grade));
        }

        // 评论数
        holder.setText(R.id.comment_count_tv, StringUtils.getString(R.string.comment_count_format, data.comment_num));

        // 距离 地点
        holder.setText(R.id.distance_tv, StringUtils.getString(R.string.distance_format, data.distance));
        holder.setText(R.id.location_tv, data.area_name);

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
        holder.setText(R.id.address_tv, data.area_name);
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
        holder.setText(R.id.address_tv, data.area_name);
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
        holder.setText(R.id.location_tv, data.area_name);

        // tag
        LinearLayout tag_ll = holder.getView(R.id.tag_ll);
        initTag(data, tag_ll);

        // 价格
        holder.setText(R.id.price_tv, StringUtils.getString(R.string.price_unit_format, data.price));

    }

    /**
     * 图片
     */
    private void loadImage(HomeRecommendResponse data, ImageView image_iv) {
        if (data.images.length > 0) {

            RequestOptions options = RequestOptions.bitmapTransform(
                    new GlideRoundedCornersTransform(10, GlideRoundedCornersTransform.CornerType.LEFT));

            Glide.with(getContext()).load(data.images[0]).apply(options).into(image_iv);
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
            tv.setPaddingRelative(SizeUtils.dp2px(4), SizeUtils.dp2px(2), SizeUtils.dp2px(4), SizeUtils.dp2px(2));
            tv.setTextColor(ColorUtils.getColor(R.color.blue_4C9EF2));
            tv.setTextSize(11);
            tv.setText(tag);
            tv.setBackgroundResource(R.drawable.shape_blue_stoke_radius_1);
            tag_ll.addView(tv);
        }

    }


}
