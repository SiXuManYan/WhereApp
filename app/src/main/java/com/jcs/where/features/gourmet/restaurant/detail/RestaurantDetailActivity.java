package com.jcs.where.features.gourmet.restaurant.detail;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.view.CommentView;
import com.jcs.where.view.DishView;
import com.jcs.where.widget.ratingstar.RatingStarView;


/**
 * Created by Wangsw  2021/4/1 10:28.
 * 餐厅详情
 */
public class RestaurantDetailActivity extends BaseMvpActivity<RestaurantDetailPresenter> implements RestaurantDetailView {


    private LinearLayout scoreLl;
    private TextView scoreTv;
    private RatingStarView starView;
    private TextView commentCountTv;
    private TextView perPriceTv;
    private ImageView addressIcon;
    private TextView addressTv;
    private TextView distanceTv;
    private ImageView timeIcon;
    private TextView timeTv;
    private TextView supportTakeawayTv;
    private DishView dishView;
    private CommentView commentView;
    private ImageView shoppingCart;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_restaurant_detail;
    }

    @Override
    protected void initView() {

        scoreLl = findViewById(R.id.score_ll);
        scoreTv = findViewById(R.id.score_tv);
        starView = findViewById(R.id.star_view);
        commentCountTv = findViewById(R.id.comment_count_tv);
        perPriceTv = findViewById(R.id.per_price_tv);
        addressIcon = findViewById(R.id.addressIcon);
        addressTv = findViewById(R.id.address_tv);
        distanceTv = findViewById(R.id.distance_tv);
        timeIcon = findViewById(R.id.timeIcon);
        timeTv = findViewById(R.id.time_tv);
        supportTakeawayTv = findViewById(R.id.support_takeaway_tv);
        dishView = findViewById(R.id.dish_view);
        commentView = findViewById(R.id.comment_view);
        shoppingCart = findViewById(R.id.shopping_cart);

    }

    @Override
    protected void initData() {
        presenter = new RestaurantDetailPresenter(this);
    }

    @Override
    protected void bindListener() {

    }


}
