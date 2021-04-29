package com.jcs.where.view;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jcs.where.R;
import com.jcs.where.api.response.gourmet.dish.DishResponse;
import com.jcs.where.features.gourmet.restaurant.packages.SetMealActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.image.GlideRoundedCornersTransform;

import java.util.List;

/**
 * Created by Wangsw  2021/4/1 14:03.
 * 堂食菜品
 */
public class DishView extends LinearLayout {


    private LinearLayout container_ll;
    private RelativeLayout unfold_rl;
    private TextView unfold_tv;
    private RequestOptions options;

    /**
     * 是否展开
     */
    private boolean isUnfold = false;
    private String mRestaurantId;
    private String mRestaurantName;

    public DishView(Context context) {
        super(context);
        initView();
    }

    public DishView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DishView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DishView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        options = RequestOptions.bitmapTransform(new GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.LEFT))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_dish, this, true);
        container_ll = view.findViewById(R.id.container_ll);
        unfold_rl = view.findViewById(R.id.unfold_rl);
        unfold_tv = view.findViewById(R.id.unfold_tv);
        unfold_rl.setOnClickListener(this::unFoldClick);
    }


    public void setData(List<DishResponse> list, String restaurantId, String restaurantName) {
        mRestaurantId = restaurantId;
        mRestaurantName = restaurantName;
        if (list.isEmpty()) {
            return;
        }

        container_ll.removeAllViews();

        for (DishResponse data : list) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_dish, null);
            setItemData(data, view);
            container_ll.addView(view);
        }

        int childCount = container_ll.getChildCount();
        if (childCount > 2) {
            for (int i = 0; i < childCount; i++) {
                if (i > 1) {
                    container_ll.getChildAt(i).setVisibility(GONE);
                }
            }
            unfold_rl.setVisibility(VISIBLE);
        } else {
            unfold_rl.setVisibility(GONE);
        }


    }

    private void setItemData(DishResponse data, View view) {

        ImageView dishIv = view.findViewById(R.id.dish_iv);
        TextView dishNameTv = view.findViewById(R.id.dish_name_tv);
        TextView salesTv = view.findViewById(R.id.sales_tv);
        TextView nowPriceTv = view.findViewById(R.id.now_price_tv);
        TextView oldPriceTv = view.findViewById(R.id.old_price_tv);
        TextView buyTv = view.findViewById(R.id.buy_tv);

        Glide.with(getContext()).load(data.image).apply(options).into(dishIv);
        dishNameTv.setText(data.title);
        salesTv.setText(StringUtils.getString(R.string.sale_format, data.sale_num));
        nowPriceTv.setText(StringUtils.getString(R.string.price_unit_format, data.price.toPlainString()));

        String oldPrice = StringUtils.getString(R.string.price_unit_format, data.original_price.toPlainString());
        SpannableStringBuilder builder = new SpanUtils().append(oldPrice).setStrikethrough().create();
        oldPriceTv.setText(builder);

        buyTv.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SetMealActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constant.PARAM_ID, String.valueOf(data.id));
            intent.putExtra(Constant.PARAM_RESTAURANT_ID, mRestaurantId);
            intent.putExtra(Constant.PARAM_RESTAURANT_NAME, mRestaurantName);
            getContext().startActivity(intent);
        });
    }

    private void unFoldClick(View view) {
        int childCount = container_ll.getChildCount();

        if (isUnfold) {
            for (int i = 0; i < childCount; i++) {
                if (i > 1) {
                    container_ll.getChildAt(i).setVisibility(GONE);
                }
            }
            unfold_tv.setText(R.string.unfold_more);
        } else {
            for (int i = 0; i < childCount; i++) {
                container_ll.getChildAt(i).setVisibility(VISIBLE);
            }
            unfold_tv.setText(R.string.fold);
        }
        isUnfold = !isUnfold;

    }


}
