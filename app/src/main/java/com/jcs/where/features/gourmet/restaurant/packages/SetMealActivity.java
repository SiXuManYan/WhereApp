package com.jcs.where.features.gourmet.restaurant.packages;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcs.where.R;
import com.jcs.where.api.response.gourmet.dish.DishDetailResponse;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.image.GlideRoundedCornersTransform;

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


    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_meal;
    }

    @Override
    protected void initView() {

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
    protected void initData() {
        mEatInFoodId = getIntent().getStringExtra(Constant.PARAM_ID);
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
        now_price_tv.setText(getString(R.string.price_unit_format, data.price));
        now_price2_tv.setText(getString(R.string.price_unit_format, data.price));

        String oldPrice = getString(R.string.price_unit_format, data.original_price);
        SpannableStringBuilder builder = new SpanUtils().append(oldPrice)
                .setStrikethrough().create();
        old_price_tv.setText(builder);
        old_price2_tv.setText(builder);
        sales_tv.setText(getString(R.string.sale_format,data.sale_num));
        set_meal_content_tv.setText(data.meals);
        rule_tv.setText(data.rule);

    }


    private void onBuyNowClick(View view) {

    }

    private void onBuyAfterClick(View view) {

    }

    private void onShoppingCartClick(View view) {

    }

}
