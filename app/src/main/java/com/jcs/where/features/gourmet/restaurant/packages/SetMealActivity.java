package com.jcs.where.features.gourmet.restaurant.packages;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcs.where.R;
import com.jcs.where.api.request.AddCartRequest;
import com.jcs.where.api.response.gourmet.cart.Products;
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse;
import com.jcs.where.api.response.gourmet.dish.DishDetailResponse;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.features.gourmet.cart.ShoppingCartActivity;
import com.jcs.where.features.gourmet.order.OrderSubmitActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.image.GlideRoundedCornersTransform;

import java.util.ArrayList;

/**
 * Created by Wangsw  2021/4/6 14:38.
 * 套餐详情
 */
public class SetMealActivity extends BaseMvpActivity<SetMealPresenter> implements SetMealView {

    private TextView name_tv;
    private ImageView image_iv;
    private TextView now_price_tv;
    private TextView old_price_tv;
    private TextView sales_tv;
    private TextView set_meal_content_tv;
    private TextView rule_tv;
    private ImageView shopping_cart;
    private TextView now_price2_tv;
    private TextView old_price2_tv;
    private TextView buy_after_tv;
    private TextView buy_now_tv;

    private String mEatInFoodId;
    private String mRestaurantId;
    private String mRestaurantName;
    private DishDetailResponse mData;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_meal;
    }

    @Override
    protected void initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE);
        name_tv = findViewById(R.id.name_tv);
        image_iv = findViewById(R.id.image_iv);
        now_price_tv = findViewById(R.id.now_price_tv);
        old_price_tv = findViewById(R.id.old_price_tv);
        sales_tv = findViewById(R.id.sales_tv);
        set_meal_content_tv = findViewById(R.id.set_meal_content_tv);
        rule_tv = findViewById(R.id.rule_tv);
        shopping_cart = findViewById(R.id.shopping_cart);
        now_price2_tv = findViewById(R.id.now_price2_tv);
        old_price2_tv = findViewById(R.id.old_price2_tv);
        buy_after_tv = findViewById(R.id.buy_after_tv);
        buy_now_tv = findViewById(R.id.buy_now_tv);
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {
        mEatInFoodId = getIntent().getStringExtra(Constant.PARAM_ID);
        mRestaurantId = getIntent().getStringExtra(Constant.PARAM_RESTAURANT_ID);
        mRestaurantName = getIntent().getStringExtra(Constant.PARAM_RESTAURANT_NAME);
        presenter = new SetMealPresenter(this);
        presenter.getDetail(mEatInFoodId);
    }

    @Override
    protected void bindListener() {
        shopping_cart.setOnClickListener(this::onShoppingCartClick);
        buy_after_tv.setOnClickListener(this::onBuyAfterClick);
        buy_now_tv.setOnClickListener(this::onBuyNowClick);
    }


    @Override
    public void bindData(DishDetailResponse data) {

        RequestOptions options = RequestOptions.bitmapTransform(
                new GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray);
        Glide.with(this).load(data.image).apply(options).into(image_iv);

        name_tv.setText(data.title);
        now_price_tv.setText(getString(R.string.price_unit_format, data.price.toPlainString()));
        now_price2_tv.setText(getString(R.string.price_unit_format, data.price.toPlainString()));

        String oldPrice = getString(R.string.price_unit_format, data.original_price);
        SpannableStringBuilder builder = new SpanUtils().append(oldPrice)
                .setStrikethrough().create();
        old_price_tv.setText(builder);
        old_price2_tv.setText(builder);
        sales_tv.setText(getString(R.string.sale_format, data.sale_num));
        set_meal_content_tv.setText(data.meals);
        rule_tv.setText(data.rule);
        mData = data;

    }


    private void onBuyNowClick(View view) {

        Products products = new Products();
        products.good_data.id = mData.id;
        products.good_data.title = mData.title;
        products.good_data.image = mData.image;
        products.good_data.price = mData.price;
        products.good_data.original_price = mData.original_price;

        products.good_num = 1;
        products.nativeIsSelect = true;

        ShoppingCartResponse response = new ShoppingCartResponse();
        response.restaurant_id = mRestaurantId;
        response.nativeIsSelect = true;
        response.restaurant_name = mRestaurantName;
        response.products.add(products);

        ArrayList<ShoppingCartResponse> value = new ArrayList<>();
        value.add(response);
        Bundle bundle = new Bundle();

        bundle.putSerializable(Constant.PARAM_DATA, value);
        bundle.putString(Constant.PARAM_TOTAL_PRICE, mData.price.toPlainString());

        startActivityAfterLogin(OrderSubmitActivity.class, bundle);

    }

    private void onBuyAfterClick(View view) {
        AddCartRequest request = new AddCartRequest();
        request.good_id = String.valueOf(mData.id);
        request.good_num = 1;
        presenter.addCart(request);
    }

    private void onShoppingCartClick(View view) {
        startActivityAfterLogin(ShoppingCartActivity.class);
    }

}
