package com.jcs.where.features.gourmet.restaurant.map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcs.where.R;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse;
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.image.GlideRoundedCornersTransform;
import com.jcs.where.widget.ratingstar.RatingStarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw  2021/5/15 14:18.
 */
public class RestaurantMapAdapter extends PagerAdapter {

    private final List<RestaurantResponse> mData = new ArrayList<>();
    private final Context context;

    public RestaurantMapAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<RestaurantResponse> data) {
        mData.clear();
        mData.addAll(data);

    }


    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_map_restaurant, container, false);

        if (!mData.isEmpty()) {
            bindItem(view, mData.get(position));
        }

        container.addView(view);
        return view;
    }

    private void bindItem(View view, RestaurantResponse data) {
        RelativeLayout parent_rl = view.findViewById(R.id.parent_rl);
        TextView name_tv = view.findViewById(R.id.name_tv);
        TextView score_tv = view.findViewById(R.id.score_tv);
        TextView comment_count_tv = view.findViewById(R.id.comment_count_tv);
        TextView area_name_tv = view.findViewById(R.id.area_name_tv);
        TextView restaurant_type_tv = view.findViewById(R.id.restaurant_type_tv);
        TextView per_price_tv = view.findViewById(R.id.per_price_tv);
        ImageView image_iv = view.findViewById(R.id.image_iv);

        RatingStarView star_view = view.findViewById(R.id.star_view);
        parent_rl.setOnClickListener(v -> {
            context.startActivity(new Intent(context, RestaurantDetailActivity.class).putExtra(Constant.PARAM_ID, data.getId()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });


        name_tv.setText(data.getTitle());


        // 星级

        float grade = data.getGrade();
//        if (grade < 3.0) {
//            star_view.setVisibility(View.GONE);
//            score_tv.setVisibility(View.GONE);
//        } else {
//            star_view.setVisibility(View.VISIBLE);
//            score_tv.setVisibility(View.VISIBLE);
//            star_view.setRating(grade);
//            score_tv.setText(String.valueOf(grade));
//        }

        star_view.setRating(grade);
        score_tv.setText(String.valueOf(grade));

        // 评论数
        comment_count_tv.setText(StringUtils.getString(R.string.comment_count_format2, data.getComment_num()));

        // 地域 、 餐厅类型
        area_name_tv.setText(data.getTrading_area());
        restaurant_type_tv.setText(data.getType());

        // 人均
        per_price_tv.setText(StringUtils.getString(R.string.per_price_format, data.getPer_price()));

        loadImage(data, image_iv);
    }


    private void loadImage(RestaurantResponse data, ImageView image_iv) {
        RequestOptions options = RequestOptions.bitmapTransform(
                new GlideRoundedCornersTransform(10, GlideRoundedCornersTransform.CornerType.LEFT))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray);
        String image = "";
        if (data.getImages().size() > 0) {
            image = data.getImages().get(0);
            Glide.with(context).load(image).apply(options).into(image_iv);
        } else {
            Glide.with(context).load(R.mipmap.ic_empty_gray).apply(options).into(image_iv);
        }

    }


}
