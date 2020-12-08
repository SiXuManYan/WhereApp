package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelOrderBean;
import com.jcs.where.bean.HotelPayBean;
import com.jcs.where.bean.SubscribeBean;
import com.jcs.where.codepicker.Country;
import com.jcs.where.codepicker.CountryPicker;
import com.jcs.where.codepicker.OnPick;
import com.jcs.where.manager.TokenManager;

import java.util.HashMap;
import java.util.Map;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.ToastUtils;

public class HotelSubscribeActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXT_BEAN = "bean";
    private TextView hotelNameTv, roomNameTv, startDateTv, startWeekTv, endDateTv, endWeekTv, nightTv;
    private Toolbar toolbar;
    private SubscribeBean subscribeBean;
    private TextView bedTv, breakfastTv, windowTv, wifiTv, peopleTv, cancelTv;
    private TextView phoneTv, roomNumTv, priceTv;
    private TextView mAreaTv;
    private ImageView reduceIv, addIv;
    private EditText nameEt, phoneEt;
    private int night = 1;

    public static void goTo(Context context, SubscribeBean subscribeBean) {
        Intent intent = new Intent(context, HotelSubscribeActivity.class);
        intent.putExtra(EXT_BEAN, subscribeBean);
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
        subscribeBean = getIntent().getParcelableExtra(EXT_BEAN);
        initView();
    }

    private void initView() {
        toolbar = V.f(this, R.id.toolbar);
        setMargins(toolbar, 0, getStatusBarHeight(), 0, 0);
        hotelNameTv = V.f(this, R.id.tv_hotelname);
        hotelNameTv.setText(subscribeBean.hotelName);
        roomNameTv = V.f(this, R.id.tv_roomname);
        roomNameTv.setText(subscribeBean.roomName);
        startDateTv = V.f(this, R.id.tv_startdate);
        startDateTv.setText(subscribeBean.startDate);
        startWeekTv = V.f(this, R.id.tv_startweek);
        startWeekTv.setText("(" + subscribeBean.startWeek + ")");
        endDateTv = V.f(this, R.id.tv_enddate);
        endDateTv.setText(subscribeBean.endDate);
        endWeekTv = V.f(this, R.id.tv_endweek);
        endWeekTv.setText("(" + subscribeBean.endWeek + ")");
        nightTv = V.f(this, R.id.tv_night);
        nightTv.setText(subscribeBean.night);
        bedTv = V.f(this, R.id.tv_bed);
        bedTv.setText(subscribeBean.bed);
        breakfastTv = V.f(this, R.id.tv_breakfast);
        breakfastTv.setText(subscribeBean.breakfast);
        windowTv = V.f(this, R.id.tv_window);
        mAreaTv = findViewById(R.id.areaCodeTv);
        mAreaTv.setOnClickListener(this);
        if (subscribeBean.window == 1) {
            windowTv.setText("有窗");
        } else {
            windowTv.setText("无窗");
        }
        wifiTv = V.f(this, R.id.tv_wifi);
        if (subscribeBean.wifi == 1) {
            wifiTv.setText("有WIFI");
        } else {
            wifiTv.setText("无WIFI");
        }
        peopleTv = V.f(this, R.id.tv_people);
        peopleTv.setText(subscribeBean.people + "人入住");
        cancelTv = V.f(this, R.id.tv_cancel);
        if (subscribeBean.cancel == 1) {
            cancelTv.setText("可取消");
        } else {
            cancelTv.setText("不可取消");
        }
        phoneTv = V.f(this, R.id.tv_phone);
        V.f(this, R.id.rl_choosephone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountryPicker.newInstance(null, new OnPick() {
                    @Override
                    public void onPick(Country country) {
                        phoneTv.setText("+" + country.code);
                    }
                }).show(getSupportFragmentManager(), "country");
            }
        });
        roomNumTv = V.f(this, R.id.tv_roomnum);
        roomNumTv.setText(subscribeBean.roomNumber);
        reduceIv = V.f(this, R.id.iv_roomreduce);
        reduceIv.setOnClickListener(this);
        addIv = V.f(this, R.id.iv_roomadd);
        addIv.setOnClickListener(this);
        priceTv = V.f(this, R.id.tv_price);
        night = Integer.valueOf(subscribeBean.night.replace("₱", "").replace("共", "").replace("晚", ""));
        priceTv.setText("₱" + Integer.valueOf(roomNumTv.getText().toString()) * subscribeBean.roomPrice * night);
        V.f(this, R.id.tv_submit).setOnClickListener(this);
        nameEt = V.f(this, R.id.et_name);
        phoneEt = V.f(this, R.id.et_phone);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotelsubscribe;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_roomreduce:
                int roomNum = Integer.valueOf(roomNumTv.getText().toString());
                if (roomNum == 1) {
                    ToastUtils.showLong(HotelSubscribeActivity.this, "不能再减了");
                    return;
                } else {
                    roomNum--;
                    roomNumTv.setText(roomNum + "");
                    priceTv.setText("₱" + roomNum * subscribeBean.roomPrice * night);
                }
                break;
            case R.id.iv_roomadd:
                int roomNum1 = Integer.valueOf(roomNumTv.getText().toString());
                roomNum1++;
                roomNumTv.setText(roomNum1 + "");
                priceTv.setText("₱" + roomNum1 * subscribeBean.roomPrice * night);
                break;
            case R.id.tv_submit:
                if (TextUtils.isEmpty(nameEt.getText().toString())) {
                    ToastUtils.showLong(HotelSubscribeActivity.this, "请填写姓名");
                    return;
                }
                if (TextUtils.isEmpty(phoneEt.getText().toString().trim())) {
                    ToastUtils.showLong(HotelSubscribeActivity.this, "请填写手机");
                    return;
                }
                submit();
                break;
            case R.id.areaCodeTv:
                showToast("哈哈哈");
                break;
        }
    }

    private void submit() {
        showLoading();
        Map<String, String> params = new HashMap<>();
        params.put("hotel_room_id", subscribeBean.roomId + "");
        params.put("price", priceTv.getText().toString().replace("₱", ""));
        params.put("username", nameEt.getText().toString());
        params.put("phone", phoneEt.getText().toString());
        params.put("start_date", subscribeBean.startYear + "-" + subscribeBean.startDate.replace("月", "-").replace("日", ""));
        params.put("end_date", subscribeBean.endYear + "-" + subscribeBean.endDate.replace("月", "-").replace("日", ""));
        params.put("room_num", roomNumTv.getText().toString());
        Log.d("ssss", params + "");
        HttpUtils.doHttpReqeust("POST", "hotelapi/v1/orders", params, "", TokenManager.get().getToken(HotelSubscribeActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    ToastUtils.showLong(HotelSubscribeActivity.this, "预订成功");
                    HotelOrderBean hotelOrderBean = new Gson().fromJson(result, HotelOrderBean.class);
                    HotelPayBean hotelPayBean = new HotelPayBean();
                    hotelPayBean.hotelName = subscribeBean.hotelName;
                    hotelPayBean.roomName = subscribeBean.roomName;
                    hotelPayBean.bed = subscribeBean.bed;
                    hotelPayBean.breakfast = subscribeBean.breakfast;
                    hotelPayBean.window = subscribeBean.window;
                    hotelPayBean.people = subscribeBean.people;
                    hotelPayBean.startDate = subscribeBean.startDate;
                    hotelPayBean.endDate = subscribeBean.endDate;
                    hotelPayBean.night = subscribeBean.night;
                    hotelPayBean.roomNumber = roomNumTv.getText().toString();
                    hotelPayBean.price = priceTv.getText().toString();
                    hotelPayBean.orderId = hotelOrderBean.getId();
                    hotelPayBean.trade_no = hotelOrderBean.getTrade_no();
                    HotelPayActivity.goTo(HotelSubscribeActivity.this, hotelPayBean);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelSubscribeActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelSubscribeActivity.this, e.getMessage());
            }
        });
    }
}
