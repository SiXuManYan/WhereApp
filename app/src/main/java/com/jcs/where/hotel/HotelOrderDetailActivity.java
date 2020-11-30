package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelOrderDetailBean;
import com.jcs.where.manager.TokenManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.ToastUtils;

public class HotelOrderDetailActivity extends BaseActivity {


    private static final String EXT_ID = "id";

    private TextView orderNumbrTv, typeTv, cancelTv, priceTv, hotelNmaeTv, scoreTv, addressTv;
    private TextView roomNameTv, startDateTv, endDateTv, nightTv;
    private TextView bedTv, breakfastTv, windowTv, wifiTv, peopleTv, contactsTv, phoneTv;
    private RoundedImageView photoIv;
    private Toolbar toolbar;


    public static void goTo(Context context, String id) {
        Intent intent = new Intent(context, HotelOrderDetailActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        initView();
    }

    private void initView() {
        toolbar = V.f(this, R.id.toolbar);
        setMargins(toolbar, 0, getStatusBarHeight(), 0, 0);
        orderNumbrTv = V.f(this, R.id.tv_ordernumber);
        typeTv = V.f(this, R.id.tv_ordertype);
        cancelTv = V.f(this, R.id.tv_cancel);
        priceTv = V.f(this, R.id.tv_price);
        photoIv = V.f(this, R.id.iv_photo);
        hotelNmaeTv = V.f(this, R.id.tv_name);
        scoreTv = V.f(this, R.id.tv_score);
        addressTv = V.f(this, R.id.tv_address);
        roomNameTv = V.f(this, R.id.tv_roomname);
        startDateTv = V.f(this, R.id.tv_startdate);
        endDateTv = V.f(this, R.id.tv_enddate);
        nightTv = V.f(this, R.id.tv_night);
        bedTv = V.f(this, R.id.tv_bed);
        breakfastTv = V.f(this, R.id.tv_breakfast);
        windowTv = V.f(this, R.id.tv_window);
        wifiTv = V.f(this, R.id.tv_wifi);
        peopleTv = V.f(this, R.id.tv_people);
        contactsTv = V.f(this, R.id.tv_contacts);
        phoneTv = V.f(this, R.id.tv_phone);
        initData();
    }

    private void initData() {
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/orders/" + getIntent().getStringExtra(EXT_ID), null, "", TokenManager.get().getToken(HotelOrderDetailActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                Log.d("ssss", result);
                stopLoading();
                if (code == 200) {
                    HotelOrderDetailBean hotelOrderDetailBean = new Gson().fromJson(result, HotelOrderDetailBean.class);
                    orderNumbrTv.setText("订单号" + hotelOrderDetailBean.getTrade_no());
                    typeTv.setText("已支付");
                    cancelTv.setText(hotelOrderDetailBean.getStart_date() + "00:00前可致电商家免费取消");
                    priceTv.setText("₱" + hotelOrderDetailBean.getPrice());
                    if (hotelOrderDetailBean.getImages() != null) {
                        Picasso.with(HotelOrderDetailActivity.this).load(hotelOrderDetailBean.getImages().get(0)).into(photoIv);
                    } else {
                        photoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
                    }
                    hotelNmaeTv.setText(hotelOrderDetailBean.getHotel_name());
                    scoreTv.setText(hotelOrderDetailBean.getGrade());
                    addressTv.setText(hotelOrderDetailBean.getHotel_addr());
                    roomNameTv.setText(hotelOrderDetailBean.getRoom_name());
                    startDateTv.setText(hotelOrderDetailBean.getStart_date());
                    endDateTv.setText(hotelOrderDetailBean.getEnd_date());
                    nightTv.setText(hotelOrderDetailBean.getDays() + "晚");
                    bedTv.setText(hotelOrderDetailBean.getRoom_type());
                    if (hotelOrderDetailBean.getBreakfast_type() == 1) {
                        breakfastTv.setText("含早");
                    } else {
                        breakfastTv.setText("不含早");
                    }
                    if (hotelOrderDetailBean.getWindow_type() == 1) {
                        windowTv.setText("有窗");
                    } else {
                        windowTv.setText("无窗");
                    }
                    if (hotelOrderDetailBean.getWifi_type() == 1) {
                        wifiTv.setText("免费无线");
                    } else {
                        wifiTv.setText("无wifi");
                    }
                    peopleTv.setText(hotelOrderDetailBean.getPeople_num() + "人入住");
                    contactsTv.setText(hotelOrderDetailBean.getUsername());
                    phoneTv.setText(hotelOrderDetailBean.getPhone());
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelOrderDetailActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelOrderDetailActivity.this, e.getMessage());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotelorderdetail;
    }
}
