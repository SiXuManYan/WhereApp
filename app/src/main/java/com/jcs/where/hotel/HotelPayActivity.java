package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.jcs.where.R;
import com.jcs.where.bean.HotelPayBean;
import com.jcs.where.utils.StatusBarUtils;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.utils.ValueUtils;

public class HotelPayActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXT_PAYBEAN = "payBean";
    private TextView hotelNameTv, roomNameTv, breakfaseTv, bedTv, peopleTv, windowTv, startDateTv, endDateTv, nightTv, roomNumberTv, priceTv, payTv;
    private HotelPayBean hotelPayBean;
    private CheckBox wechatPayCb, aliPayCb, bankPayCb;

    public static void goTo(Context context, HotelPayBean hotelPayBean) {
        Intent intent = new Intent(context, HotelPayActivity.class);
        intent.putExtra(EXT_PAYBEAN, hotelPayBean);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, ValueUtils.getColor(this, R.color.white), 0);
        StatusBarUtils.setStatusBarLightMode(getWindow());
        hotelPayBean = getIntent().getParcelableExtra(EXT_PAYBEAN);
        initView();
    }

    private void initView() {
        hotelNameTv = V.f(this, R.id.tv_hotelname);
        hotelNameTv.setText(hotelPayBean.hotelName);
        roomNameTv = V.f(this, R.id.tv_roomname);
        roomNameTv.setText(hotelPayBean.roomName);
        breakfaseTv = V.f(this, R.id.tv_breakfast);
        breakfaseTv.setText(hotelPayBean.breakfast + ",");
        bedTv = V.f(this, R.id.tv_bed);
        bedTv.setText(hotelPayBean.bed + ",");
        peopleTv = V.f(this, R.id.tv_people);
        peopleTv.setText(hotelPayBean.people + "人入住,");
        windowTv = V.f(this, R.id.tv_window);
        if (hotelPayBean.window == 1) {
            windowTv.setText("有窗");
        } else {
            windowTv.setText("无窗");
        }
        startDateTv = V.f(this, R.id.tv_startdate);
        startDateTv.setText(hotelPayBean.startDate);
        endDateTv = V.f(this, R.id.tv_enddate);
        endDateTv.setText(hotelPayBean.endDate);
        nightTv = V.f(this, R.id.tv_night);
        nightTv.setText(hotelPayBean.night);
        roomNumberTv = V.f(this, R.id.tv_roomnumber);
        roomNumberTv.setText(hotelPayBean.roomNumber);
        priceTv = V.f(this, R.id.tv_price);
        priceTv.setText(hotelPayBean.price);
        wechatPayCb = V.f(this, R.id.cb_wechatpay);
        wechatPayCb.setChecked(true);
        aliPayCb = V.f(this, R.id.cb_alipay);
        bankPayCb = V.f(this, R.id.cb_bankpay);
        V.f(this, R.id.rl_wechatpay).setOnClickListener(this);
        V.f(this, R.id.rl_alipay).setOnClickListener(this);
        V.f(this, R.id.rl_bankpay).setOnClickListener(this);
        payTv = V.f(this, R.id.tv_pay);
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
                payTv.setText("微信支付  " + hotelPayBean.price);
                break;
            case R.id.rl_alipay:
                wechatPayCb.setChecked(false);
                aliPayCb.setChecked(true);
                bankPayCb.setChecked(false);
                payTv.setText("支付宝支付  " + hotelPayBean.price);
                break;
            case R.id.rl_bankpay:
                wechatPayCb.setChecked(false);
                aliPayCb.setChecked(false);
                bankPayCb.setChecked(true);
                payTv.setText("首都银行支付  " + hotelPayBean.price);
                break;
            case R.id.tv_pay:
                HotelOrderDetailActivity.goTo(HotelPayActivity.this,hotelPayBean.orderId);
                break;
        }
    }
}
