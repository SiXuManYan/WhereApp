package com.jcs.where.features.gourmet.restaurant.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcs.where.R;
import com.jcs.where.api.response.gourmet.comment.CommentResponse;
import com.jcs.where.api.response.gourmet.dish.DishResponse;
import com.jcs.where.api.response.gourmet.restaurant.RestaurantDetailResponse;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.features.gourmet.takeaway.TakeawayActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.image.GlideRoundedCornersTransform;
import com.jcs.where.view.CommentView;
import com.jcs.where.view.DishView;
import com.jcs.where.widget.ratingstar.RatingStarView;

import java.util.List;

import static com.jcs.where.utils.Constant.PARAM_ID;


/**
 * Created by Wangsw  2021/4/1 10:28.
 * 餐厅详情
 */
public class RestaurantDetailActivity extends BaseMvpActivity<RestaurantDetailPresenter> implements RestaurantDetailView {


    private ImageView image_iv;
    private TextView name_tv;
    private TextView score_tv;
    private RatingStarView star_view;
    private TextView comment_count_tv;
    private TextView per_price_tv;
    private TextView address_tv;
    private TextView distance_tv;
    private TextView time_tv;
    private TextView support_takeaway_tv;
    private DishView dish_view;
    private CommentView comment_view;
    private ImageView shopping_cart, navigation_iv, chat_iv;
    private View dish_split_v;

    /**
     * 餐厅id
     */
    private String mRestaurantId;

    /**
     * 餐厅名称
     */
    private String mRestaurantName;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_restaurant_detail;
    }

    @Override
    protected void initView() {

        image_iv = findViewById(R.id.image_iv);
        name_tv = findViewById(R.id.name_tv);
        score_tv = findViewById(R.id.score_tv);
        star_view = findViewById(R.id.star_view);
        comment_count_tv = findViewById(R.id.comment_count_tv);
        per_price_tv = findViewById(R.id.per_price_tv);
        address_tv = findViewById(R.id.address_tv);
        distance_tv = findViewById(R.id.distance_tv);
        time_tv = findViewById(R.id.time_tv);
        support_takeaway_tv = findViewById(R.id.support_takeaway_tv);
        dish_view = findViewById(R.id.dish_view);
        comment_view = findViewById(R.id.comment_view);
        shopping_cart = findViewById(R.id.shopping_cart);
        navigation_iv = findViewById(R.id.navigation_iv);
        chat_iv = findViewById(R.id.chat_iv);
        dish_split_v = findViewById(R.id.dish_split_v);

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {
        mRestaurantId = getIntent().getStringExtra(Constant.PARAM_ID);
        presenter = new RestaurantDetailPresenter(this);
        presenter.getDetail(mRestaurantId);
        presenter.getDishList(mRestaurantId);
        presenter.getCommentList(mRestaurantId);

    }

    @Override
    protected void bindListener() {
        shopping_cart.setOnClickListener(this::onShoppingCartClick);
        navigation_iv.setOnClickListener(v -> showComing());
        chat_iv.setOnClickListener(v -> showComing());
        support_takeaway_tv.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_ID, mRestaurantId);
            startActivity(TakeawayActivity.class, bundle);
        });
    }


    @Override
    public void bindData(RestaurantDetailResponse data) {
        if (data.images != null && !data.images.isEmpty()) {
            RequestOptions options = RequestOptions.bitmapTransform(
                    new GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                    .error(R.mipmap.ic_empty_gray)
                    .placeholder(R.mipmap.ic_empty_gray);
            Glide.with(this).load(data.images.get(0)).apply(options).into(image_iv);
        }

        mRestaurantName = data.title;
        mJcsTitle.setMiddleTitle(mRestaurantName);
        name_tv.setText(mRestaurantName);

        score_tv.setText(String.valueOf(data.grade));
        star_view.setRating(data.grade);
        comment_count_tv.setText(getString(R.string.parentheses_contain_string, data.comment_num));
        per_price_tv.setText(getString(R.string.price_unit_format, data.per_price));
        address_tv.setText(data.address);
        time_tv.setText(getString(R.string.time_format, data.start_time, data.end_time));
        if (data.take_out_status == 2) {
            support_takeaway_tv.setVisibility(View.VISIBLE);
        } else {
            support_takeaway_tv.setVisibility(View.GONE);
        }
        if (data.im_status == 1) {
            chat_iv.setVisibility(View.VISIBLE);
        } else {
            chat_iv.setVisibility(View.GONE);
        }
        distance_tv.setText(getString(R.string.distance_format, data.distance));
    }

    @Override
    public void bindDishData(List<DishResponse> data) {
        dish_view.setData(data, mRestaurantId, mRestaurantName);
        dish_view.setVisibility(View.VISIBLE);
        dish_split_v.setVisibility(View.VISIBLE);
        shopping_cart.setVisibility(View.VISIBLE);
    }

    @Override
    public void bindCommentData(List<CommentResponse> data) {
        comment_view.setData(data);
        comment_view.setVisibility(View.VISIBLE);
    }

    private void onShoppingCartClick(View view) {
        showComing();
    }

}
