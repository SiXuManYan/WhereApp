package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelDetailBean;
import com.jcs.where.bean.RoomListBean;
import com.jcs.where.manager.TokenManager;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.BaseQuickAdapter;
import co.tton.android.base.view.ToastUtils;

public class HotelDetailActivity extends BaseActivity {

    private static final String EXT_ID = "id";
    private static final String EXT_STARTDAY = "startDay";
    private static final String EXT_ENDDAY = "endDay";
    private static final String EXT_STARTWEEK = "startWeek";
    private static final String EXT_ENDWEEK = "endWeek";
    private static final String EXT_ALLDAY = "allDay";
    private static final String EXT_STARTYEAR = "startYear";
    private static final String EXT_ENDYEAR = "endYear";

    private Toolbar toolbar;
    private ImageView photoIv;
    private TextView nameTv, startTimeTv, starTv, commnetNumberTv;
    private RelativeLayout faceBookRl;
    private TextView faceBookTv;
    private View faceLine;
    private TextView checkInTv, checkOutTv, addressTv;
    private String phone;
    private TextView startDateTv, startWeekTv, endDateTv, endWeekTv, allDayTv;
    private RecyclerView roomRv, facilitiesRv;
    private RoomAdapter roomAdapter;
    private FacilitiesAdapter facilitiesAdapter;


    public static void goTo(Context context, int id, String startDate, String endDate, String startWeek, String endWeek, String allDay, String startYear, String endYear) {
        Intent intent = new Intent(context, HotelDetailActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.putExtra(EXT_STARTDAY, startDate);
        intent.putExtra(EXT_ENDDAY, endDate);
        intent.putExtra(EXT_STARTWEEK, startWeek);
        intent.putExtra(EXT_ENDWEEK, endWeek);
        intent.putExtra(EXT_ALLDAY, allDay);
        intent.putExtra(EXT_STARTYEAR, startYear);
        intent.putExtra(EXT_ENDYEAR, endYear);
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
        photoIv = V.f(this, R.id.iv_photo);
        nameTv = V.f(this, R.id.tv_name);
        startTimeTv = V.f(this, R.id.tv_startTime);
        starTv = V.f(this, R.id.tv_star);
        commnetNumberTv = V.f(this, R.id.tv_commentnumber);
        faceBookRl = V.f(this, R.id.rl_facebook);
        faceBookTv = V.f(this, R.id.tv_facebookadress);
        faceLine = V.f(this, R.id.faceline);
        checkInTv = V.f(this, R.id.tv_checkin);
        checkOutTv = V.f(this, R.id.tv_checkout);
        addressTv = V.f(this, R.id.tv_address);
        V.f(this, R.id.rl_navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showLong(HotelDetailActivity.this, "导航");
            }
        });
        V.f(this, R.id.rl_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone();
            }
        });
        startDateTv = V.f(this, R.id.tv_startday);
        startDateTv.setText(getIntent().getStringExtra(EXT_STARTDAY));
        startWeekTv = V.f(this, R.id.tv_startweek);
        startWeekTv.setText(getIntent().getStringExtra(EXT_STARTWEEK));
        endDateTv = V.f(this, R.id.tv_endday);
        endDateTv.setText(getIntent().getStringExtra(EXT_ENDDAY));
        endWeekTv = V.f(this, R.id.tv_endweek);
        endWeekTv.setText(getIntent().getStringExtra(EXT_ENDWEEK));
        allDayTv = V.f(this, R.id.tv_allday);
        allDayTv.setText(getIntent().getStringExtra(EXT_ALLDAY));
        roomRv = V.f(this, R.id.rv_room);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HotelDetailActivity.this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        roomAdapter = new RoomAdapter(HotelDetailActivity.this);
        roomRv.setLayoutManager(linearLayoutManager);
        facilitiesRv = V.f(this, R.id.rv_facilities);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HotelDetailActivity.this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        facilitiesRv.setLayoutManager(gridLayoutManager);
        facilitiesAdapter = new FacilitiesAdapter(HotelDetailActivity.this);
        initData();
    }

    private void initData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hotel/" + getIntent().getIntExtra(EXT_ID, 0), null, "", TokenManager.get().getToken(HotelDetailActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    HotelDetailBean hotelDetailBean = new Gson().fromJson(result, HotelDetailBean.class);
                    if (!TextUtils.isEmpty(hotelDetailBean.getImages().get(0))) {
                        Picasso.with(HotelDetailActivity.this).load(hotelDetailBean.getImages().get(0)).into(photoIv);
                    } else {
                        photoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
                    }
                    nameTv.setText(hotelDetailBean.getName());
                    startTimeTv.setText(hotelDetailBean.getStart_business_time());
                    starTv.setText(hotelDetailBean.getGrade() + "");
                    commnetNumberTv.setText(hotelDetailBean.getComment_counts() + "");
                    if (TextUtils.isEmpty(hotelDetailBean.getFacebook_link())) {
                        faceBookRl.setVisibility(View.GONE);
                        faceLine.setVisibility(View.GONE);
                    } else {
                        faceBookRl.setVisibility(View.VISIBLE);
                        faceLine.setVisibility(View.VISIBLE);
                        faceBookTv.setText(hotelDetailBean.getFacebook_link());
                    }
                    checkInTv.setText("入住时间：" + hotelDetailBean.getPolicy().getCheck_in_time().substring(0, 5));
                    checkOutTv.setText("退房时间：" + hotelDetailBean.getPolicy().getCheck_out_time().substring(0, 5));
                    addressTv.setText(hotelDetailBean.getAddress());
                    phone = hotelDetailBean.getTel();
                    facilitiesAdapter.setData(hotelDetailBean.getFacilities());
                    facilitiesRv.setAdapter(facilitiesAdapter);
                    initRoomList();
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelDetailActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelDetailActivity.this, e.getMessage());
            }
        });
    }

    private void initRoomList() {
        showLoading();
        String url = "hotelapi/v1/hotel/" + getIntent().getIntExtra(EXT_ID, 0) + "/rooms?start_date=" + getIntent().getStringExtra(EXT_STARTYEAR) + "-" + getIntent().getStringExtra(EXT_STARTDAY).replace("月", "-").replace("日", "") + "&end_date=" + getIntent().getStringExtra(EXT_ENDYEAR) + "-" + getIntent().getStringExtra(EXT_ENDDAY).replace("月", "-").replace("日", "") + "&room_num=" + "1";
        HttpUtils.doHttpReqeust("GET", url, null, "", TokenManager.get().getToken(HotelDetailActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<RoomListBean>>() {
                    }.getType();
                    List<RoomListBean> list = gson.fromJson(result, type);
                    roomAdapter.setData(list);
                    roomRv.setAdapter(roomAdapter);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelDetailActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelDetailActivity.this, e.getMessage());
            }
        });
    }

    private void initCommentList(){

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hoteldetail;
    }

    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        startActivity(intent);
    }

    private class RoomAdapter extends BaseQuickAdapter<RoomListBean> {

        public RoomAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_room;
        }

        @Override
        protected void initViews(QuickHolder holder, RoomListBean data, int position) {

        }
    }

    private class FacilitiesAdapter extends BaseQuickAdapter<HotelDetailBean.FacilitiesBean> {

        public FacilitiesAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId(int viewType) {
            return R.layout.item_facilities;
        }

        @Override
        protected void initViews(QuickHolder holder, HotelDetailBean.FacilitiesBean data, int position) {
            ImageView iconIv = holder.findViewById(R.id.iv_icon);
            if (!TextUtils.isEmpty(data.getIcon())) {
                Picasso.with(HotelDetailActivity.this).load(data.getIcon()).into(iconIv);
            } else {
                iconIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_test));
            }
            TextView nameTv = holder.findViewById(R.id.tv_name);
            nameTv.setText(data.getName());
        }
    }
}
