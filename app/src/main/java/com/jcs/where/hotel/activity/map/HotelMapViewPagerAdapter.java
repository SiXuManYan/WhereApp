package com.jcs.where.hotel.activity.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcs.where.R;
import com.jcs.where.bean.HotelMapListBean;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.image.GlideRoundedCornersTransform;
import com.jcs.where.widget.ratingstar.RatingStarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wangsw  2021/3/10 11:18.
 */
public class HotelMapViewPagerAdapter extends PagerAdapter {


    private final List<HotelMapListBean> mData = new ArrayList<>();
    private final Context context;

    public HotelMapViewPagerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<HotelMapListBean> data) {
        mData.clear();
        mData.addAll(data);
//        notifyDataSetChanged();
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

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_home_recommend_hotel, container, false);

        if (!mData.isEmpty()) {
            bindHotelView(view, mData.get(position));
        }

        container.addView(view);
        return view;
    }


    /**
     * 酒店推荐
     */
    private void bindHotelView(View view, HotelMapListBean data) {

        // 图片
        ImageView image_iv = view.findViewById(R.id.image_iv);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) image_iv.getLayoutParams();
        layoutParams.weight = 25;
        image_iv.setLayoutParams(layoutParams);


        loadImage(data, image_iv);

        // 标题
        TextView title_tv = view.findViewById(R.id.title_tv);
        title_tv.setText(data.getName());

        // 星级
        TextView score_tv = view.findViewById(R.id.score_tv);
        TextView score_retouch_tv = view.findViewById(R.id.score_retouch_tv);
        RatingStarView star_view = view.findViewById(R.id.star_view);

        float grade = data.getGrade();
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
        TextView comment_count_tv = view.findViewById(R.id.comment_count_tv);
        comment_count_tv.setText(StringUtils.getString(R.string.comment_count_format, String.valueOf(data.comment_counts)));


        // 距离 地点
        TextView distance_tv = view.findViewById(R.id.distance_tv);
        TextView location_tv = view.findViewById(R.id.location_tv);

        distance_tv.setText(StringUtils.getString(R.string.distance_format, String.valueOf(data.getDistance())));
        location_tv.setText(data.getAddress());

        // tag
        LinearLayout tag_ll = view.findViewById(R.id.tag_ll);
        initTag(data, tag_ll);

        // 价格
        TextView price_tv = view.findViewById(R.id.price_tv);
        price_tv.setText(StringUtils.getString(R.string.price_unit_format, String.valueOf(data.price)));

    }

    private void loadImage(HotelMapListBean data, ImageView image_iv) {
        RequestOptions options = RequestOptions.bitmapTransform(
                new GlideRoundedCornersTransform(10, GlideRoundedCornersTransform.CornerType.LEFT))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray);

        String image = "";
        if (data.images.size() > 0) {
            image = data.images.get(0);
            Glide.with(context).load(image).apply(options).into(image_iv);
        } else {
            Glide.with(context).load(R.mipmap.ic_empty_gray).apply(options).into(image_iv);
        }

    }


    /**
     * tag
     */
    private void initTag(HotelMapListBean data, LinearLayout tag_ll) {

        tag_ll.removeAllViews();

        List<HotelMapListBean.TagsBean> tags = data.getTags();
        if (tags.size() <= 0) {
            return;
        }

        for (HotelMapListBean.TagsBean tag : tags) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(SizeUtils.dp2px(2));
            TextView tv = new TextView(context);
            tv.setLayoutParams(params);
            tv.setPaddingRelative(SizeUtils.dp2px(4), SizeUtils.dp2px(2), SizeUtils.dp2px(4), SizeUtils.dp2px(2));
            tv.setTextColor(ColorUtils.getColor(R.color.blue_4C9EF2));
            tv.setTextSize(11);
            tv.setText(tag.getName());
            tv.setBackgroundResource(R.drawable.shape_blue_stoke_radius_1);
            tag_ll.addView(tv);
        }

    }


}
