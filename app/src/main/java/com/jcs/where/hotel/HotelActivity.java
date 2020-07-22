package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atuan.datepickerlibrary.CalendarUtil;
import com.atuan.datepickerlibrary.DatePopupWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.GuessYouLikeHotelBean;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.popupwindow.ChoosePricePop;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;

public class HotelActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQ_SELECT_CITY = 100;
    private TextView locationTv, startDateTv, startWeekTv, endDateTv, endWeekTv, allDayTv, roomNumTv, priceAndStarTv;
    private RelativeLayout chooseDateRl;
    private ImageView reduceIv, addIv;
    private RecyclerView showRv;
    private GuessYouLikeAdapter guessYouLikeAdapter;
    private int startGroup = -1;
    private int endGroup = -1;
    private int startChild = -1;
    private int endChild = -1;
    private String cityId = "0";

    public static void goTo(Context context) {
        Intent intent = new Intent(context, HotelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        initView();
        guessYouLikeAdapter = new GuessYouLikeAdapter(HotelActivity.this);
    }

    private void initView() {
        locationTv = V.f(this, R.id.tv_location);
        locationTv.setOnClickListener(this);
        chooseDateRl = V.f(this, R.id.rl_choosedate);
        chooseDateRl.setOnClickListener(this);
        startDateTv = V.f(this, R.id.tv_startday);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        startDateTv.setText(simpleDateFormat.format(date));
        startWeekTv = V.f(this, R.id.tv_startweek);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date(System.currentTimeMillis());
        startWeekTv.setText("周" + CalendarUtil.getWeekByFormat(simpleDateFormat1.format(date1)));
        endDateTv = V.f(this, R.id.tv_endday);
        endDateTv.setText(getOldDate(1));
        endWeekTv = V.f(this, R.id.tv_endweek);
        endWeekTv.setText("周" + CalendarUtil.getWeekByFormat(getOldWeek(1)));
        allDayTv = V.f(this, R.id.tv_allday);
        roomNumTv = V.f(this, R.id.tv_roomnum);
        reduceIv = V.f(this, R.id.iv_roomreduce);
        reduceIv.setOnClickListener(this);
        addIv = V.f(this, R.id.iv_roomadd);
        addIv.setOnClickListener(this);
        roomNumTv = V.f(this, R.id.tv_roomnum);
        priceAndStarTv = V.f(this, R.id.tv_priceandstar);
        priceAndStarTv.setOnClickListener(this);
        showRv = V.f(this, R.id.rv_show);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelActivity.this,
                LinearLayoutManager.VERTICAL, false);
        showRv.setLayoutManager(linearLayoutManager);
        V.f(this, R.id.tv_search).setOnClickListener(this);
        initData();
    }

    private void initData() {
        showLoading();
        Map<String, String> params = new HashMap<>();
        params.put("lat", "");
        params.put("lng", "");
        params.put("area_id", "1");
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hotels/recommends", null, "", TokenManager.get().getToken(HotelActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<GuessYouLikeHotelBean>>() {
                    }.getType();
                    List<GuessYouLikeHotelBean> list = gson.fromJson(result, type);
                    guessYouLikeAdapter.setData(list);
                    showRv.setAdapter(guessYouLikeAdapter);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelActivity.this, e.getMessage());
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotel;
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                Intent intent = new Intent(HotelActivity.this, CityPickerActivity.class);
                startActivityForResult(intent, REQ_SELECT_CITY);
                break;
            case R.id.rl_choosedate:
                new DatePopupWindow
                        .Builder(HotelActivity.this, Calendar.getInstance().getTime(), view)
                        .setInitSelect(startGroup, startChild, endGroup, endChild)
                        .setInitDay(false)
                        .setDateOnClickListener(new DatePopupWindow.DateOnClickListener() {
                            @Override
                            public void getDate(String startDate, String endDate, String startWeek, String endWeek, int allDay, int startGroupPosition, int startChildPosition, int endGroupPosition, int endChildPosition) {
                                startGroup = startGroupPosition;
                                startChild = startChildPosition;
                                endGroup = endGroupPosition;
                                endChild = endChildPosition;
                                String mStartTime = CalendarUtil.FormatDateYMD(startDate);
                                String mEndTime = CalendarUtil.FormatDateYMD(endDate);
                                startDateTv.setText(mStartTime);
                                startWeekTv.setText(startWeek);
                                endDateTv.setText(mEndTime);
                                endWeekTv.setText(endWeek);
                                allDayTv.setText("共" + allDay + "晚");
                            }
                        }).builder();
                break;
            case R.id.iv_roomreduce:
                int roomNum = Integer.valueOf(roomNumTv.getText().toString());
                if (roomNum == 1) {
                    ToastUtils.showLong(HotelActivity.this, "不能再减了");
                    return;
                } else {
                    roomNum--;
                    roomNumTv.setText(roomNum + "");
                }
                break;
            case R.id.iv_roomadd:
                int roomNum1 = Integer.valueOf(roomNumTv.getText().toString());
                roomNum1++;
                roomNumTv.setText(roomNum1 + "");
                break;
            case R.id.tv_priceandstar:
                new ChoosePricePop.Builder(HotelActivity.this, view)
                        .setPriceOnClickListener(new ChoosePricePop.PriceOnClickListener() {
                            @Override
                            public void getDate(String price, String star) {
                                priceAndStarTv.setText(price + "，" + star);
                                priceAndStarTv.setTextColor(getResources().getColor(R.color.black_333333));
                            }
                        }).builder();
                break;
            case R.id.tv_search:
                HotelListActivity.goTo(HotelActivity.this, startDateTv.getText().toString(), endDateTv.getText().toString(), locationTv.getText().toString(),cityId);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_SELECT_CITY) {
            locationTv.setText(data.getStringExtra(CityPickerActivity.EXTRA_CITY));
            cityId = data.getStringExtra(CityPickerActivity.EXTRA_CITYID);
        }
    }

    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("MM月dd日");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    public static String getOldWeek(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    private class GuessYouLikeAdapter extends BaseQuickAdapter<GuessYouLikeHotelBean> {

        public GuessYouLikeAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_guessyoulikehotel;
        }

        @Override
        protected void initViews(QuickHolder holder, GuessYouLikeHotelBean data, int position) {
            RoundedImageView photoIv = holder.findViewById(R.id.iv_photo);
            if (!TextUtils.isEmpty(data.getImages().get(0))) {
                Picasso.with(HotelActivity.this).load(data.getImages().get(0)).into(photoIv);
            } else {
                photoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
            }
            TextView nameTv = holder.findViewById(R.id.tv_name);
            nameTv.setText(data.getName());
            TextView scoreTv = holder.findViewById(R.id.tv_score);
            scoreTv.setText(data.getGrade() + "分");
            TextView commentNumTv = holder.findViewById(R.id.tv_commentnumber);
            commentNumTv.setText(data.getComment_counts() + "条评论");
            TextView distanceTv = holder.findViewById(R.id.tv_distance);
            distanceTv.setText("<" + data.getDistance() + "Km");
            TextView addressTv = holder.findViewById(R.id.tv_address);
            addressTv.setText(data.getAddress());
            TextView priceTv = holder.findViewById(R.id.tv_price);
            priceTv.setText("₱" + data.getPrice() + "起");
            TextView olePriceTv = holder.findViewById(R.id.tv_oldprice);
            olePriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }


}
