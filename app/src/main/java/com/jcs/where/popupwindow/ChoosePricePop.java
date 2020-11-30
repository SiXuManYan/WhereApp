package com.jcs.where.popupwindow;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jcs.where.R;

public class ChoosePricePop extends PopupWindow implements View.OnClickListener {

    private final Activity activity;
    private final View rootView;
    private ImageView btnClose;
    private TextView priceTv1, priceTv2, priceTv3, priceTv4, priceTv5, priceTv6, priceTv7, priceTv8;
    private TextView starTv1, starTv2, starTv3, starTv4;
    private TextView resetTv, finishTv;
    private PriceOnClickListener mOnClickListener = null;
    private String price, star;

    private ChoosePricePop(Builder builder) {
        this.activity = builder.context;
        this.mOnClickListener = builder.mOnClickListener;
        LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.popupwindow_choose_price, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOnDismissListener(new ShareDismissListener());
        backgroundAlpha(activity, 0.5f);
        price = builder.price;
        star = builder.star;
        initView();
        create(builder.parentView);
    }

    private void initView() {
        btnClose = (ImageView) rootView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoosePricePop.this.dismiss();
            }
        });
        priceTv1 = (TextView) rootView.findViewById(R.id.tv_price1);
        priceTv2 = (TextView) rootView.findViewById(R.id.tv_price2);
        priceTv3 = (TextView) rootView.findViewById(R.id.tv_price3);
        priceTv4 = (TextView) rootView.findViewById(R.id.tv_price4);
        priceTv5 = (TextView) rootView.findViewById(R.id.tv_price5);
        priceTv6 = (TextView) rootView.findViewById(R.id.tv_price6);
        priceTv7 = (TextView) rootView.findViewById(R.id.tv_price7);
        priceTv8 = (TextView) rootView.findViewById(R.id.tv_price8);
        starTv1 = (TextView) rootView.findViewById(R.id.tv_star1);
        starTv2 = (TextView) rootView.findViewById(R.id.tv_star2);
        starTv3 = (TextView) rootView.findViewById(R.id.tv_star3);
        starTv4 = (TextView) rootView.findViewById(R.id.tv_star4);
        resetTv = (TextView) rootView.findViewById(R.id.tv_reset);
        finishTv = (TextView) rootView.findViewById(R.id.tv_finish);
        priceTv1.setOnClickListener(this);
        priceTv2.setOnClickListener(this);
        priceTv3.setOnClickListener(this);
        priceTv4.setOnClickListener(this);
        priceTv5.setOnClickListener(this);
        priceTv6.setOnClickListener(this);
        priceTv7.setOnClickListener(this);
        priceTv8.setOnClickListener(this);
        starTv1.setOnClickListener(this);
        starTv2.setOnClickListener(this);
        starTv3.setOnClickListener(this);
        starTv4.setOnClickListener(this);
        resetTv.setOnClickListener(this);
        finishTv.setOnClickListener(this);
        ((View) rootView.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
        priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
        priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
        priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
        priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
        priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
        priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
        priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
        priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        starTv1.setBackgroundResource(R.drawable.bg_priceunselect);
        starTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        starTv2.setBackgroundResource(R.drawable.bg_priceunselect);
        starTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        starTv3.setBackgroundResource(R.drawable.bg_priceunselect);
        starTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        starTv4.setBackgroundResource(R.drawable.bg_priceunselect);
        starTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
        if (price != null) {
            if (price.equals(priceTv1.getText().toString())) {
                priceTv1.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv1.getText().toString();
            } else if (price.equals(priceTv2.getText().toString())) {
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv2.getText().toString();
            } else if (price.equals(priceTv3.getText().toString())) {
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv3.getText().toString();
            } else if (price.equals(priceTv4.getText().toString())) {
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv4.getText().toString();
            } else if (price.equals(priceTv5.getText().toString())) {
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv5.getText().toString();
            } else if (price.equals(priceTv6.getText().toString())) {
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv6.getText().toString();
            } else if (price.equals(priceTv7.getText().toString())) {
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv7.getText().toString();
            } else if (price.equals(priceTv8.getText().toString())) {
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                price = priceTv8.getText().toString();
            }
        }
        if (star != null) {
            if (star.equals(starTv1.getText().toString())) {
                starTv1.setBackgroundResource(R.drawable.bg_priceselected);
                starTv1.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                starTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                star = starTv1.getText().toString();
            } else if (star.equals(starTv2.getText().toString())) {
                starTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv2.setBackgroundResource(R.drawable.bg_priceselected);
                starTv2.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                starTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                star = starTv2.getText().toString();
            } else if (star.equals(starTv3.getText().toString())) {
                starTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv3.setBackgroundResource(R.drawable.bg_priceselected);
                starTv3.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                starTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                star = starTv3.getText().toString();
            } else if (star.equals(starTv4.getText().toString())) {
                starTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv4.setBackgroundResource(R.drawable.bg_priceselected);
                starTv4.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                star = starTv4.getText().toString();
            }
        }

    }

    private void create(View view) {
        this.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_price1:
                priceTv1.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv1.getText().toString();
                break;
            case R.id.tv_price2:
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv2.getText().toString();
                break;
            case R.id.tv_price3:
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv3.getText().toString();
                break;
            case R.id.tv_price4:
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv4.getText().toString();
                break;
            case R.id.tv_price5:
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv5.getText().toString();
                break;
            case R.id.tv_price6:
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv6.getText().toString();
                break;
            case R.id.tv_price7:
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = priceTv7.getText().toString();
                break;
            case R.id.tv_price8:
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceselected);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                price = priceTv8.getText().toString();
                break;
            case R.id.tv_star1:
                starTv1.setBackgroundResource(R.drawable.bg_priceselected);
                starTv1.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                starTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                star = starTv1.getText().toString();
                break;
            case R.id.tv_star2:
                starTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv2.setBackgroundResource(R.drawable.bg_priceselected);
                starTv2.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                starTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                star = starTv2.getText().toString();
                break;
            case R.id.tv_star3:
                starTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv3.setBackgroundResource(R.drawable.bg_priceselected);
                starTv3.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                starTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                star = starTv3.getText().toString();
                break;
            case R.id.tv_star4:
                starTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv4.setBackgroundResource(R.drawable.bg_priceselected);
                starTv4.setTextColor(activity.getResources().getColor(R.color.blue_4C9EF2));
                star = starTv4.getText().toString();
                break;
            case R.id.tv_reset:
                priceTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv5.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv5.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv6.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv6.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv7.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv7.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                priceTv8.setBackgroundResource(R.drawable.bg_priceunselect);
                priceTv8.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                price = null;
                starTv1.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv1.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv2.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv2.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv3.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv3.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                starTv4.setBackgroundResource(R.drawable.bg_priceunselect);
                starTv4.setTextColor(activity.getResources().getColor(R.color.grey_666666));
                star = null;
                break;
            case R.id.tv_finish:
                if (mOnClickListener != null) {
                    mOnClickListener.getDate(price, star);
                }
                ChoosePricePop.this.dismiss();
                break;
        }
    }

    public interface PriceOnClickListener {
        void getDate(String price, String star);
    }

    public static class Builder {
        private final Activity context;
        private final View parentView;
        private PriceOnClickListener mOnClickListener = null;
        private final String price;
        private final String star;

        public Builder(Activity context, View parentView, String price, String star) {
            this.context = context;
            this.parentView = parentView;
            this.price = price;
            this.star = star;
        }

        public ChoosePricePop builder() {
            return new ChoosePricePop(this);
        }

        public Builder setPriceOnClickListener(PriceOnClickListener mlListener) {
            mOnClickListener = mlListener;
            return this;
        }
    }

    private class ShareDismissListener implements OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(activity, 1f);
        }
    }

}
