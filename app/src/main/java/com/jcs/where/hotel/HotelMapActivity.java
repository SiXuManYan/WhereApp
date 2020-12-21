package com.jcs.where.hotel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelMapListBean;
import com.jcs.where.hotel.card.CardPagerAdapter;
import com.jcs.where.hotel.card.ShadowTransformer;
import com.jcs.where.hotel.event.HotelEvent;
import com.jcs.where.manager.TokenManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * 目的地选择页面
 */
public class HotelMapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    private static final int REQ_SEARCH = 777;
    private static final String EXT_STARTDATE = "startDate";
    private static final String EXT_ENDDATE = "endDate";
    private static final String EXT_STARTWEEK = "startWeek";
    private static final String EXT_ENDWEEK = "endWeek";
    private static final String EXT_ALLDAY = "allDay";
    private static final String EXT_CITY = "city";
    private static final String EXT_CITYID = "cityId";
    private static final String EXT_PRICE = "price";
    private static final String EXT_STAR = "star";
    private static final String EXT_STARTYEAR = "startYear";
    private static final String EXT_ENDYEAR = "endYear";
    private static final String EXT_ROOMNUMBER = "roomNumber";
    private static final LatLng ADELAIDE = new LatLng(14.6778362, 120.5306459);
    private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();
    private final List<View> views = new ArrayList<View>();
    private final int READ_CODE = 10;
    private final int READ_LOCATIONCODE = 11;
    private GoogleMap mMap;
    private ViewPager viewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private final int size = 10;
    private double lat = 14.6778362;
    private double lng = 120.5306459;
    private TextView startDayTv, endDayTv, cityTv;
    private String mStartYear, mStartDate, mStartWeek, mEndYear, mEndData, mEndWeek, mAllDay, mRoomNum;
    private int lastPostition = 0;
    private int lastScrollPosition = 0;
    private ImageView clearIv, mHotelListIv;
    private FusedLocationProviderClient fusedLocationClient;
    private String useInputText = "";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean mAddressRequested;
    private boolean clickLocation = false;

    public static void goTo(Context context, String startDate, String endDate, String startWeek, String endWeek, String allDay, String city, String cityId, String price, String star, String startYear, String endYear, String roomNumber) {
        Intent intent = new Intent(context, HotelMapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXT_STARTDATE, startDate);
        intent.putExtra(EXT_ENDDATE, endDate);
        intent.putExtra(EXT_STARTWEEK, startWeek);
        intent.putExtra(EXT_ENDWEEK, endWeek);
        intent.putExtra(EXT_ALLDAY, allDay);
        intent.putExtra(EXT_CITY, city);
        intent.putExtra(EXT_CITYID, cityId);
        intent.putExtra(EXT_PRICE, price);
        intent.putExtra(EXT_STAR, star);
        intent.putExtra(EXT_STARTYEAR, startYear);
        intent.putExtra(EXT_ENDYEAR, endYear);
        intent.putExtra(EXT_ROOMNUMBER, roomNumber);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        viewPager = findViewById(R.id.viewpager);
        mHotelListIv = findViewById(R.id.listIv);
        mCardAdapter = new CardPagerAdapter(HotelMapActivity.this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mMarkerRainbow.get(position).showInfoWindow();
                TextView priceTv = views.get(position).findViewById(R.id.tv_price);
                views.get(position).setBackground(ContextCompat.getDrawable(HotelMapActivity.this, R.drawable.ic_markselected));
                priceTv.setTextColor(getResources().getColor(R.color.white));
                ((ViewGroup) views.get(position).getParent()).removeView(views.get(position));
                mMarkerRainbow.get(position).setIcon(fromView(HotelMapActivity.this, views.get(position)));
                TextView price1Tv = views.get(lastScrollPosition).findViewById(R.id.tv_price);
                views.get(lastScrollPosition).setBackground(ContextCompat.getDrawable(HotelMapActivity.this, R.drawable.ic_mark));
                price1Tv.setTextColor(getResources().getColor(R.color.blue_4C9EF2));
                ((ViewGroup) views.get(lastScrollPosition).getParent()).removeView(views.get(lastScrollPosition));
                mMarkerRainbow.get(lastScrollPosition).setIcon(fromView(HotelMapActivity.this, views.get(lastScrollPosition)));
                lastScrollPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        startDayTv = findViewById(R.id.startDayTv);
        endDayTv = findViewById(R.id.endDayTv);
        cityTv = findViewById(R.id.cityTv);
        findViewById(R.id.toChooseDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View customView = View.inflate(HotelMapActivity.this, R.layout.pop_maptitle, null);
                TextView startDateTv = customView.findViewById(R.id.tv_startdate);
                startDateTv.setText(getIntent().getStringExtra(EXT_STARTDATE));
                TextView endDateTv = customView.findViewById(R.id.tv_enddate);
                endDateTv.setText(getIntent().getStringExtra(EXT_ENDDATE));
                TextView allDayTv = customView.findViewById(R.id.tv_allday);
                allDayTv.setText(getIntent().getStringExtra(EXT_ALLDAY));
                TextView roomNumTv = customView.findViewById(R.id.tv_roomnum);
                roomNumTv.setText(mRoomNum);
                customView.findViewById(R.id.iv_roomreduce).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int roomNum = Integer.valueOf(roomNumTv.getText().toString());
                        if (roomNum == 1) {
                            showToast("不能再减了");
                            return;
                        } else {
                            roomNum--;
                            roomNumTv.setText(roomNum + "");
                            mRoomNum = roomNumTv.getText().toString();
                        }
                    }
                });
                customView.findViewById(R.id.iv_roomadd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int roomNum1 = Integer.parseInt(roomNumTv.getText().toString());
                        roomNum1++;
                        roomNumTv.setText(String.valueOf(roomNum1));
                        mRoomNum = roomNumTv.getText().toString();
                    }
                });
                customView.findViewById(R.id.ll_choosedate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        });
        findViewById(R.id.myLocationView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIsGooglePlayConn();
            }
        });
        findViewById(R.id.searchBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelSearchActivity.goTo(HotelMapActivity.this, getIntent().getStringExtra(EXT_CITYID), REQ_SEARCH);
            }
        });
        clearIv = findViewById(R.id.clearIv);
        clearIv.setVisibility(View.GONE);
        clearIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityTv.setText("请输入酒店名称");
                useInputText = "";
                cityTv.setTextColor(getResources().getColor(R.color.grey_b7b7b7));
                clearIv.setVisibility(View.GONE);
                initData();
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showToast("无权限");
            findViewById(R.id.myLocationGroup).setVisibility(View.GONE);
        } else {
            showToast("有权限");
            findViewById(R.id.myLocationGroup).setVisibility(View.VISIBLE);
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void initData() {
        mStartYear = getIntent().getStringExtra(EXT_STARTYEAR);
        mStartDate = getIntent().getStringExtra(EXT_STARTDATE);
        mStartWeek = getIntent().getStringExtra(EXT_STARTWEEK);
        mEndYear = getIntent().getStringExtra(EXT_ENDYEAR);
        mEndData = getIntent().getStringExtra(EXT_ENDDATE);
        mEndWeek = getIntent().getStringExtra(EXT_ENDWEEK);
        mAllDay = getIntent().getStringExtra(EXT_ALLDAY);
        mRoomNum = getIntent().getStringExtra(EXT_ROOMNUMBER);
        startDayTv.setText(getIntent().getStringExtra(EXT_STARTDATE).replace("月", "-").replace("日", ""));
        endDayTv.setText(getIntent().getStringExtra(EXT_ENDDATE).replace("月", "-").replace("日", ""));

        showLoading();
        String url = null;
        if (getIntent().getStringExtra(EXT_PRICE) == null) {
            url = "hotelapi/v1/map/hotels?lat=" + lat + "&lng=" + lng + "&area_id=" + getIntent().getStringExtra(EXT_CITYID) + "&star_level=" + getIntent().getStringExtra(EXT_STAR) + "&search_input=" + useInputText;
        }
        if (getIntent().getStringExtra(EXT_STAR) == null) {
            url = "hotelapi/v1/map/hotels?lat=" + lat + "&lng=" + lng + "&area_id=" + getIntent().getStringExtra(EXT_CITYID) + "&price_range=" + getIntent().getStringExtra(EXT_PRICE) + "&search_input=" + useInputText;
        }
        if (getIntent().getStringExtra(EXT_PRICE) == null && getIntent().getStringExtra(EXT_STAR) == null) {
            url = "hotelapi/v1/map/hotels?lat=" + lat + "&lng=" + lng + "&area_id=" + getIntent().getStringExtra(EXT_CITYID) + "&search_input=" + useInputText;
        }
        if (getIntent().getStringExtra(EXT_PRICE) != null && getIntent().getStringExtra(EXT_STAR) != null) {
            url = "hotelapi/v1/map/hotels?lat=" + lat + "&lng=" + lng + "&area_id=" + getIntent().getStringExtra(EXT_CITYID) + "&price_range=" + getIntent().getStringExtra(EXT_PRICE) + "&star_level=" + getIntent().getStringExtra(EXT_STAR) + "&search_input=" + useInputText;
        }
        if (mMap != null) {
            mMap.clear();
        }
        if (mMarkerRainbow != null) {
            mMarkerRainbow.clear();
        }
        if (mCardAdapter != null) {
            mCardAdapter.clear();
        }
        if (viewPager != null) {
            viewPager.removeAllViews();
        }
        if (views != null) {
            views.clear();
        }
        HttpUtils.doHttpReqeust("GET", url, null, "", TokenManager.get().getToken(HotelMapActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<HotelMapListBean>>() {
                    }.getType();
                    List<HotelMapListBean> list = gson.fromJson(result, type);
                    // Center camera on Adelaide marker

                    lastPostition = 0;
                    lastScrollPosition = 0;
                    if (list != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10f));
                        for (int i = 0; i < list.size(); i++) {
                            View view = LayoutInflater.from(HotelMapActivity.this).inflate(R.layout.custom_marker_layout, null);
                            TextView peiceTv = view.findViewById(R.id.tv_price);
                            peiceTv.setText("php " + list.get(i).getPrice());
                            views.add(view);
                            BitmapDescriptor bitmapDescriptor = fromView(HotelMapActivity.this, view);
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(
                                            list.get(i).getLat(),
                                            list.get(i).getLng()))
                                    // .title("₱" + list.get(i).getPrice() + "起")
                                    .icon(bitmapDescriptor));
                            mMarkerRainbow.add(marker);
                        }

                        views.get(0).setBackground(ContextCompat.getDrawable(HotelMapActivity.this, R.drawable.ic_markselected));
                        TextView priceTv = views.get(0).findViewById(R.id.tv_price);
                        priceTv.setTextColor(getResources().getColor(R.color.white));
                        ((ViewGroup) views.get(0).getParent()).removeView(views.get(0));
                        mMarkerRainbow.get(0).setIcon(fromView(HotelMapActivity.this, views.get(0)));

                        for (int i = 0; i < list.size(); i++) {
                            mCardAdapter.addCardItem(list.get(i));
                        }
                        mCardShadowTransformer = new ShadowTransformer(viewPager, mCardAdapter);
                        mCardShadowTransformer.enableScaling(true);
                        viewPager.setAdapter(mCardAdapter);
                        viewPager.setPageTransformer(false, mCardShadowTransformer);
                        viewPager.setOffscreenPageLimit(3);
                        if (clickLocation == true) {

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marklocation)));
                        }
                    } else {
                        showToast("未查询到该酒店");
                    }
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    showToast(errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void bindListener() {
        mHotelListIv.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotelmap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap == null) {
            return;
        }
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMarkerClickListener(this);
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 2 * t);

                if (t > 0.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });

        if (marker.getId().equals(mMarkerRainbow.get(lastPostition).getId())) {

        } else {
            for (int i = 0; i < mMarkerRainbow.size(); i++) {
                if (marker.getId().equals(mMarkerRainbow.get(i).getId())) {
                    viewPager.setCurrentItem(i);
                    TextView priceTv = views.get(i).findViewById(R.id.tv_price);
                    views.get(i).setBackground(ContextCompat.getDrawable(HotelMapActivity.this, R.drawable.ic_markselected));
                    priceTv.setTextColor(getResources().getColor(R.color.white));
                    ((ViewGroup) views.get(i).getParent()).removeView(views.get(i));
                    marker.setIcon(fromView(HotelMapActivity.this, views.get(i)));
                    TextView price1Tv = views.get(lastPostition).findViewById(R.id.tv_price);
                    views.get(lastPostition).setBackground(ContextCompat.getDrawable(HotelMapActivity.this, R.drawable.ic_mark));
                    price1Tv.setTextColor(getResources().getColor(R.color.blue_4C9EF2));
                    ((ViewGroup) views.get(lastPostition).getParent()).removeView(views.get(lastPostition));
                    mMarkerRainbow.get(lastPostition).setIcon(fromView(HotelMapActivity.this, views.get(lastPostition)));
                    lastPostition = i;
                }
            }

        }
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Evect(HotelEvent hotelEvent) {
        HotelDetailActivity.goTo(HotelMapActivity.this,
                hotelEvent.getId(),
                mStartDate,
                mEndData,
                mStartWeek,
                mEndWeek,
                mAllDay,
                mStartYear,
                mEndYear,
                mRoomNum);
    }

    @Override
    protected void onDestroy() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SEARCH && data != null) {
            clearIv.setVisibility(View.VISIBLE);

            cityTv.setTextColor(getResources().getColor(R.color.grey_666666));
            cityTv.setText(data.getStringExtra(HotelSearchActivity.EXT_SELECT_SEARCH));
            useInputText = data.getStringExtra(HotelSearchActivity.EXT_SELECT_SEARCH);
            initData();
        }
    }

    /**
     * GoogleMap：根据传入的 view，创建 BitmapDescriptor 对象
     */
    public BitmapDescriptor fromView(Context context, View view) {
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.addView(view);
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = getBitmapFromView(frameLayout);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        bitmap.recycle();
        return bitmapDescriptor;
    }

    /**
     * Convert a view to bitmap
     */
    public Bitmap getBitmapFromView(View view) {
        try {
            banTextViewHorizontallyScrolling(view);
            view.destroyDrawingCache();
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            Bitmap bitmap = view.getDrawingCache();
            return bitmap != null ? bitmap.copy(Bitmap.Config.ARGB_8888, false) : null;
        } catch (Throwable ex) {
            return null;
        }
    }

    /**
     * 禁止 TextView 水平滚动
     */
    private void banTextViewHorizontallyScrolling(View view) {
        if (view instanceof ViewGroup) {
            for (int index = 0; index < ((ViewGroup) view).getChildCount(); ++index) {
                banTextViewHorizontallyScrolling(((ViewGroup) view).getChildAt(index));
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setHorizontallyScrolling(false);
        }
    }

    private void checkIsGooglePlayConn() {
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            initArea(mLastLocation);
        }
        mAddressRequested = true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HotelMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, READ_LOCATIONCODE);
            ActivityCompat.requestPermissions(HotelMapActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, READ_CODE);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, "No geocoder available", Toast.LENGTH_LONG).show();
                return;
            }
            if (mAddressRequested) {
                initArea(mLastLocation);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void initArea(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        clickLocation = true;
        initData();
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == mHotelListIv){
            finish();
        }
    }
}
