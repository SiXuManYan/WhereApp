package com.jcs.where.hotel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.HotelOrderDetailResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.model.OrderModel;
import com.makeramen.roundedimageview.RoundedImageView;

import io.reactivex.annotations.NonNull;

public class HotelOrderDetailActivity extends BaseActivity {


    private static final String EXT_ID = "id";

    private TextView typeTv, cancelTv, priceTv, hotelNmaeTv, scoreTv, addressTv;
    private TextView roomNameTv, startDateTv, endDateTv, nightTv;
    private TextView bedTv, breakfastTv, windowTv, wifiTv, peopleTv, contactsTv, phoneTv;
    private RoundedImageView photoIv;
    private OrderModel mModel;
    private String mOrderId;

    public static void goTo(Context context, String id) {
        Intent intent = new Intent(context, HotelOrderDetailActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

    @Override
    protected void initView() {
        typeTv = findViewById(R.id.tv_ordertype);
        cancelTv = findViewById(R.id.tv_cancel);
        priceTv = findViewById(R.id.tv_price);
        photoIv = findViewById(R.id.iv_photo);
        hotelNmaeTv = findViewById(R.id.tv_name);
        scoreTv = findViewById(R.id.tv_score);
        addressTv = findViewById(R.id.tv_address);
        roomNameTv = findViewById(R.id.tv_roomname);
        startDateTv = findViewById(R.id.tv_startdate);
        endDateTv = findViewById(R.id.tv_enddate);
        nightTv = findViewById(R.id.tv_night);
        bedTv = findViewById(R.id.tv_bed);
        breakfastTv = findViewById(R.id.tv_breakfast);
        windowTv = findViewById(R.id.tv_window);
        wifiTv = findViewById(R.id.tv_wifi);
        peopleTv = findViewById(R.id.tv_people);
        contactsTv = findViewById(R.id.tv_contacts);
        phoneTv = findViewById(R.id.tv_phone);
    }

    @Override
    protected void initData() {
        mModel = new OrderModel();
        mOrderId = getIntent().getStringExtra(EXT_ID);
        showLoading();
        mModel.getHotelOrderDetail(Integer.parseInt(mOrderId), new BaseObserver<HotelOrderDetailResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull HotelOrderDetailResponse hotelOrderDetailResponse) {
                stopLoading();
                mJcsTitle.setMiddleTitle("订单号" + hotelOrderDetailResponse.getTrade_no());
                typeTv.setText("已支付");
                cancelTv.setText(hotelOrderDetailResponse.getStart_date() + "00:00前可致电商家免费取消");
                priceTv.setText("₱" + hotelOrderDetailResponse.getPrice());
                if (hotelOrderDetailResponse.getImages() != null) {
                    Glide.with(HotelOrderDetailActivity.this).load(hotelOrderDetailResponse.getImages().get(0)).into(photoIv);
                } else {
                    photoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
                }
                hotelNmaeTv.setText(hotelOrderDetailResponse.getHotel_name());
                scoreTv.setText(String.valueOf(hotelOrderDetailResponse.getGrade()));
                addressTv.setText(hotelOrderDetailResponse.getHotel_addr());
                roomNameTv.setText(hotelOrderDetailResponse.getRoom_name());
                startDateTv.setText(hotelOrderDetailResponse.getStart_date());
                endDateTv.setText(hotelOrderDetailResponse.getEnd_date());
                nightTv.setText(hotelOrderDetailResponse.getDays() + "晚");
                bedTv.setText(hotelOrderDetailResponse.getRoom_type());
                if (hotelOrderDetailResponse.getBreakfast_type() == 1) {
                    breakfastTv.setText("含早");
                } else {
                    breakfastTv.setText("不含早");
                }
                if (hotelOrderDetailResponse.getWindow_type() == 1) {
                    windowTv.setText("有窗");
                } else {
                    windowTv.setText("无窗");
                }
                if (hotelOrderDetailResponse.getWifi_type() == 1) {
                    wifiTv.setText("免费无线");
                } else {
                    wifiTv.setText("无wifi");
                }
                peopleTv.setText(hotelOrderDetailResponse.getPeople_num() + "人入住");
                contactsTv.setText(hotelOrderDetailResponse.getUsername());
                phoneTv.setText(hotelOrderDetailResponse.getPhone());

            }
        });
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotelorderdetail;
    }
}
