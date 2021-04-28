package com.jcs.where.hotel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.HotelOrderDetailResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.government.dialog.CallPhoneDialog;
import com.jcs.where.government.dialog.ToNavigationDialog;
import com.jcs.where.model.OrderModel;
import com.jcs.where.utils.GlideUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;

import io.reactivex.annotations.NonNull;

public class HotelOrderDetailActivity extends BaseActivity {


    private static final String EXT_ID = "id";

    private TextView typeTv, cancelTv, priceTv, hotelNmaeTv, scoreTv, addressTv;
    private TextView roomNameTv, startDateTv, endDateTv, nightTv;
    private TextView bedTv, breakfastTv, windowTv, wifiTv, peopleTv, contactsTv, phoneTv;
    private TextView mToCallMerchantTv,mToNavTv;
    private RoundedImageView photoIv;
    private OrderModel mModel;
    private String mOrderId;
    private HashMap<Integer, String> mHotelOrderStatus;


    private CallPhoneDialog mCallDialog;
    private ToNavigationDialog mToNavigationDialog;

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
        mToCallMerchantTv = findViewById(R.id.toCallMerchantTv);
        mToNavTv = findViewById(R.id.tv_navigation);

        mCallDialog = new CallPhoneDialog();
        mToNavigationDialog = new ToNavigationDialog();
        mJcsTitle.setBackGround(R.color.transplant);
    }

    @Override
    protected void initData() {
        mModel = new OrderModel();
        mOrderId = getIntent().getStringExtra(EXT_ID);
        initHotelOrderStatus();
        showLoading();
        mModel.getHotelOrderDetail(Integer.parseInt(mOrderId), new BaseObserver<HotelOrderDetailResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onSuccess(@NonNull HotelOrderDetailResponse hotelOrderDetailResponse) {
                stopLoading();
                mJcsTitle.setMiddleTitle(String.format(getString(R.string.order_number), hotelOrderDetailResponse.getTrade_no()));
                typeTv.setText(mHotelOrderStatus.get(hotelOrderDetailResponse.getOrder_status()));
                cancelTv.setText(String.format(getString(R.string.order_cancel_prompt), hotelOrderDetailResponse.getStart_date()));
                priceTv.setText(String.format(getString(R.string.show_price_with_forward_unit), hotelOrderDetailResponse.getPrice()));
                if (hotelOrderDetailResponse.getImages() != null) {
                    GlideUtil.load(HotelOrderDetailActivity.this, hotelOrderDetailResponse.getImages().get(0), photoIv);
                } else {
                    photoIv.setImageResource(R.mipmap.ic_glide_default);
                }
                hotelNmaeTv.setText(hotelOrderDetailResponse.getHotel_name());
                scoreTv.setText(String.valueOf(hotelOrderDetailResponse.getGrade()));
                addressTv.setText(hotelOrderDetailResponse.getHotel_addr());
                roomNameTv.setText(hotelOrderDetailResponse.getRoom_name());
                startDateTv.setText(hotelOrderDetailResponse.getStart_date());
                endDateTv.setText(hotelOrderDetailResponse.getEnd_date());
                nightTv.setText(String.format(getString(R.string.total_night), hotelOrderDetailResponse.getDays()));
                bedTv.setText(hotelOrderDetailResponse.getRoom_type());
                if (hotelOrderDetailResponse.getBreakfast_type() == 1) {
                    breakfastTv.setText(getString(R.string.with_breakfast));
                } else {
                    breakfastTv.setText(getString(R.string.no_breakfast));
                }
                if (hotelOrderDetailResponse.getWindow_type() == 1) {
                    windowTv.setText(getString(R.string.with_window));
                } else {
                    windowTv.setText(getString(R.string.no_window));
                }
                if (hotelOrderDetailResponse.getWifi_type() == 1) {
                    wifiTv.setText(getString(R.string.with_wifi));
                } else {
                    wifiTv.setText(getString(R.string.no_wifi));
                }

                peopleTv.setText(String.format(getString(R.string.stay_people_number), hotelOrderDetailResponse.getPeople_num()));
                contactsTv.setText(hotelOrderDetailResponse.getUsername());
                phoneTv.setText(hotelOrderDetailResponse.getPhone());
                mCallDialog.setPhoneNumber(hotelOrderDetailResponse.getPhone());
                mToNavigationDialog.setTargetLocation(hotelOrderDetailResponse.getHotel_addr());
                mToNavigationDialog.setLatitude(hotelOrderDetailResponse.getHotel_lat());
                mToNavigationDialog.setLongitude(hotelOrderDetailResponse.getHotel_lng());

            }
        });
    }

    private void initHotelOrderStatus() {
        mHotelOrderStatus = new HashMap<>();
        mHotelOrderStatus.put(1, getString(R.string.mine_unpaid));
        mHotelOrderStatus.put(2, getString(R.string.mine_booked));
        mHotelOrderStatus.put(3, getString(R.string.mine_reviews));
        mHotelOrderStatus.put(4, getString(R.string.completed));
        mHotelOrderStatus.put(5, getString(R.string.cancelled));
        mHotelOrderStatus.put(6, getString(R.string.refunding));
        mHotelOrderStatus.put(7, getString(R.string.refunded));
        mHotelOrderStatus.put(8, getString(R.string.refund_failed));
    }

    @Override
    protected void bindListener() {
        mToCallMerchantTv.setOnClickListener(this::toCallMerchant);
        mToNavTv.setOnClickListener(this::toNavigation);
    }

    private void toNavigation(View view) {
        mToNavigationDialog.show(getSupportFragmentManager());
    }

    private void toCallMerchant(View view) {
        mCallDialog.show(getSupportFragmentManager());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotel_order_detail;
    }
}
