package com.jcs.where.hotel.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.HotelOrderDetailResponse;
import com.jcs.where.api.response.HotelOrderResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.hotel.model.HotelPayModel;

import io.reactivex.annotations.NonNull;

public class HotelPayActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXT_PAYBEAN = "HotelOrderResponse";
    private TextView hotelNameTv, roomNameTv, breakfaseTv, bedTv, peopleTv, windowTv, startDateTv, endDateTv, nightTv, roomNumberTv, priceTv, payTv;
    private HotelOrderResponse hotelOrderResponse;
    private HotelPayModel mModel;
    private CheckBox wechatPayCb, aliPayCb, bankPayCb;
    private HotelOrderDetailResponse mOrderDetail;

    public static void goTo(Context context, HotelOrderResponse hotelOrderResponse) {
        Intent intent = new Intent(context, HotelPayActivity.class);
        intent.putExtra(EXT_PAYBEAN, hotelOrderResponse);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        hotelNameTv = findViewById(R.id.tv_hotelname);
        roomNameTv = findViewById(R.id.tv_roomname);
        breakfaseTv = findViewById(R.id.tv_breakfast);
        bedTv = findViewById(R.id.tv_bed);
        peopleTv = findViewById(R.id.tv_people);
        windowTv = findViewById(R.id.tv_window);
        startDateTv = findViewById(R.id.tv_startdate);
        endDateTv = findViewById(R.id.tv_enddate);
        nightTv = findViewById(R.id.tv_night);
        roomNumberTv = findViewById(R.id.tv_roomnumber);
        priceTv = findViewById(R.id.tv_price);
        wechatPayCb = findViewById(R.id.cb_wechatpay);
        wechatPayCb.setChecked(true);
        aliPayCb = findViewById(R.id.cb_alipay);
        bankPayCb = findViewById(R.id.cb_bankpay);
        payTv = findViewById(R.id.tv_pay);
    }

    @Override
    protected void initData() {
        mModel = new HotelPayModel();
        hotelOrderResponse = (HotelOrderResponse) getIntent().getSerializableExtra(EXT_PAYBEAN);

        mModel.getHotelOrderDetail(hotelOrderResponse.getId(), new BaseObserver<HotelOrderDetailResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {

            }

            @Override
            public void onNext(@NonNull HotelOrderDetailResponse hotelOrderDetailResponse) {
                deployOrderDetail(hotelOrderDetailResponse);
            }
        });
    }

    private void deployOrderDetail(HotelOrderDetailResponse hotelOrderDetailResponse) {
        mOrderDetail = hotelOrderDetailResponse;
        hotelNameTv.setText(hotelOrderDetailResponse.getHotel_name());
        roomNameTv.setText(hotelOrderDetailResponse.getRoom_name());
        breakfaseTv.setText(hotelOrderDetailResponse.getBreakfast_type() == 1 ? getString(R.string.with_breakfast) + "," : getString(R.string.no_breakfast) + ",");
        bedTv.setText(hotelOrderDetailResponse.getRoom_type() + ",");
        peopleTv.setText(hotelOrderDetailResponse.getPeople_num() + "人入住,");
        if (hotelOrderDetailResponse.getWindow_type() == 1) {
            windowTv.setText("有窗");
        } else {
            windowTv.setText("无窗");
        }

        startDateTv.setText(hotelOrderDetailResponse.getStart_date());
        endDateTv.setText(hotelOrderDetailResponse.getEnd_date());
        nightTv.setText(String.valueOf(hotelOrderDetailResponse.getDays()));
        roomNumberTv.setText(String.valueOf(hotelOrderDetailResponse.getRoom_num()));
        priceTv.setText(String.valueOf(hotelOrderDetailResponse.getPrice()));
    }

    @Override
    protected void bindListener() {
        findViewById(R.id.rl_wechatpay).setOnClickListener(this);
        findViewById(R.id.rl_alipay).setOnClickListener(this);
        findViewById(R.id.rl_bankpay).setOnClickListener(this);
        payTv.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotelpay;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_wechatpay:
                wechatPayCb.setChecked(true);
                aliPayCb.setChecked(false);
                bankPayCb.setChecked(false);
                payTv.setText("微信支付  " + mOrderDetail.getPrice());
                break;
            case R.id.rl_alipay:
                wechatPayCb.setChecked(false);
                aliPayCb.setChecked(true);
                bankPayCb.setChecked(false);
                payTv.setText("支付宝支付  " + mOrderDetail.getPrice());
                break;
            case R.id.rl_bankpay:
                wechatPayCb.setChecked(false);
                aliPayCb.setChecked(false);
                bankPayCb.setChecked(true);
                payTv.setText("首都银行支付  " + mOrderDetail.getPrice());
                break;
            case R.id.tv_pay:
                showToast("支付成功");
                finish();
                HotelOrderDetailActivity.goTo(HotelPayActivity.this, String.valueOf(mOrderDetail.getId()));
                break;
        }
    }
}
