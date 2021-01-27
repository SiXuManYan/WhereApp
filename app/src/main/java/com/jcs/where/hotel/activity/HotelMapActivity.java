package com.jcs.where.hotel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
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
import com.jcs.where.widget.calendar.JcsCalendarAdapter;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelMapListBean;
import com.jcs.where.widget.calendar.JcsCalendarDialog;
import com.jcs.where.hotel.card.CardPagerAdapter;
import com.jcs.where.hotel.card.ShadowTransformer;
import com.jcs.where.hotel.event.HotelEvent;
import com.jcs.where.hotel.helper.HotelSelectDateHelper;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.view.EnterStayInfoView;
import com.jcs.where.view.popup.PopupConstraintLayoutAdapter;
import com.jcs.where.view.popup.PopupConstraintLayout;

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
public class HotelMapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final int REQ_SEARCH = 777;
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
    private TextView mStartDayTv, mEndDayTv, mCityTv;
    private String mStartYear, mStartDate, mStartWeek, mEndYear, mEndData, mEndWeek;
    private int mTotalDay, mRoomNum;
    private int lastPostition = 0;
    private int lastScrollPosition = 0;
    private ImageView clearIv, mHotelListIv;
    private FusedLocationProviderClient fusedLocationClient;
    private String useInputText = "";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean mAddressRequested;
    private boolean clickLocation = false;
    private View mChooseDataView;
    private JcsCalendarDialog mCalendarDialog;
    private PopupConstraintLayout mTopPopupLayout;
    private EnterStayInfoView mEnterStayInfoView;

    private JcsCalendarAdapter.CalendarBean mStartDateBean;
    private JcsCalendarAdapter.CalendarBean mEndDateBean;

    public static void goTo(Context context, JcsCalendarAdapter.CalendarBean startDateBean, JcsCalendarAdapter.CalendarBean endDateBean, int totalDay, String city, String cityId, String price, String star, int roomNumber, String categoryId) {
        Intent intent = new Intent(context, HotelMapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(HotelSelectDateHelper.EXT_START_DATE_BEAN, startDateBean);
        intent.putExtra(HotelSelectDateHelper.EXT_END_DATE_BEAN, endDateBean);
        intent.putExtra(HotelSelectDateHelper.EXT_TOTAL_DAY, totalDay);
        intent.putExtra(HotelSelectDateHelper.EXT_CITY, city);
        intent.putExtra(HotelSelectDateHelper.EXT_CITY_ID, cityId);
        intent.putExtra(HotelSelectDateHelper.EXT_PRICE, price);
        intent.putExtra(HotelSelectDateHelper.EXT_STAR, star);
        intent.putExtra(HotelSelectDateHelper.EXT_ROOM_NUMBER, roomNumber);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mCalendarDialog = new JcsCalendarDialog();
        mCalendarDialog.initCalendar(this);
        mChooseDataView = findViewById(R.id.toChooseDate);
        mTopPopupLayout = findViewById(R.id.topPopupLayout);
        mEnterStayInfoView = findViewById(R.id.enterStayInfoView);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
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
        mStartDayTv = findViewById(R.id.startDayTv);
        mEndDayTv = findViewById(R.id.endDayTv);
        mCityTv = findViewById(R.id.cityTv);

        clearIv = findViewById(R.id.clearIv);
        clearIv.setVisibility(View.GONE);
        clearIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCityTv.setText(getString(R.string.input_hotel_name));
                useInputText = "";
                mCityTv.setTextColor(getResources().getColor(R.color.grey_b7b7b7));
                clearIv.setVisibility(View.GONE);
                initData();
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showToast(getString(R.string.permission_none));
            findViewById(R.id.myLocationGroup).setVisibility(View.GONE);
        } else {
            showToast(getString(R.string.permission_has));
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
        mStartDateBean = (JcsCalendarAdapter.CalendarBean) getIntent().getSerializableExtra(HotelSelectDateHelper.EXT_START_DATE_BEAN);
        mEndDateBean = (JcsCalendarAdapter.CalendarBean) getIntent().getSerializableExtra(HotelSelectDateHelper.EXT_END_DATE_BEAN);
        mTotalDay = getIntent().getIntExtra(HotelSelectDateHelper.EXT_TOTAL_DAY, 0);
        mRoomNum = getIntent().getIntExtra(HotelSelectDateHelper.EXT_ROOM_NUMBER, 0);
        mTopPopupLayout.setAdapter(new PopupConstraintLayoutAdapter() {

            @Override
            public int getMaxHeight() {
                return getPxFromDp(120);
            }
        });

        mStartDayTv.setText(mStartDateBean.getShowMonthDayDateWithSplit());
        mEndDayTv.setText(mEndDateBean.getShowMonthDayDateWithSplit());
        mEnterStayInfoView.bindEnterStayInfoAdapter(this::toShowCalendarDialog);
        mEnterStayInfoView.setStartAndEnd(mStartDateBean, mEndDateBean);
        mEnterStayInfoView.setRoomNum(mRoomNum);

        showLoading();
        String url = null;
        if (getIntent().getStringExtra(HotelSelectDateHelper.EXT_PRICE) == null) {
            url = "hotelapi/v1/map/hotels?lat=" + lat + "&lng=" + lng + "&area_id=" + getIntent().getStringExtra(HotelSelectDateHelper.EXT_CITY_ID) + "&star_level=" + getIntent().getStringExtra(HotelSelectDateHelper.EXT_STAR) + "&search_input=" + useInputText;
        }
        if (getIntent().getStringExtra(HotelSelectDateHelper.EXT_STAR) == null) {
            url = "hotelapi/v1/map/hotels?lat=" + lat + "&lng=" + lng + "&area_id=" + getIntent().getStringExtra(HotelSelectDateHelper.EXT_CITY_ID) + "&price_range=" + getIntent().getStringExtra(HotelSelectDateHelper.EXT_PRICE) + "&search_input=" + useInputText;
        }
        if (getIntent().getStringExtra(HotelSelectDateHelper.EXT_PRICE) == null && getIntent().getStringExtra(HotelSelectDateHelper.EXT_STAR) == null) {
            url = "hotelapi/v1/map/hotels?lat=" + lat + "&lng=" + lng + "&area_id=" + getIntent().getStringExtra(HotelSelectDateHelper.EXT_CITY_ID) + "&search_input=" + useInputText;
        }
        if (getIntent().getStringExtra(HotelSelectDateHelper.EXT_PRICE) != null && getIntent().getStringExtra(HotelSelectDateHelper.EXT_STAR) != null) {
            url = "hotelapi/v1/map/hotels?lat=" + lat + "&lng=" + lng + "&area_id=" + getIntent().getStringExtra(HotelSelectDateHelper.EXT_CITY_ID) + "&price_range=" + getIntent().getStringExtra(HotelSelectDateHelper.EXT_PRICE) + "&star_level=" + getIntent().getStringExtra(HotelSelectDateHelper.EXT_STAR) + "&search_input=" + useInputText;
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
        mChooseDataView.setOnClickListener(this::onChooseViewClicked);
        mHotelListIv.setOnClickListener(v -> finish());
        mCalendarDialog.setOnDateSelectedListener(this::onDateSelected);
        findViewById(R.id.myLocationView).setOnClickListener(view -> checkIsGooglePlayConn());
        mCityTv.setOnClickListener(view -> HotelSearchActivity.goTo(HotelMapActivity.this, getIntent().getStringExtra(HotelSelectDateHelper.EXT_CITY_ID), HotelSearchActivity.SearchTag.HOTEL, REQ_SEARCH));
    }

    public void toShowCalendarDialog() {
        if (!mCalendarDialog.isVisible()) {
            mCalendarDialog.show(getSupportFragmentManager());
        }
    }

    public void onChooseViewClicked(View view) {
        mTopPopupLayout.showOrHide();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotel_map;
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

    public void onDateSelected(JcsCalendarAdapter.CalendarBean startDate, JcsCalendarAdapter.CalendarBean endDate) {
        mEnterStayInfoView.setStartAndEnd(startDate, endDate);
        if (startDate != null) {
            mStartDayTv.setText(startDate.getShowMonthDayDateWithSplit());
        }

        if (endDate != null) {
            mEndDayTv.setText(endDate.getShowMonthDayDateWithSplit());
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
                mStartDateBean,
                mEndDateBean,
                mTotalDay,
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

            mCityTv.setTextColor(getResources().getColor(R.color.grey_666666));
            mCityTv.setText(data.getStringExtra(HotelSearchActivity.EXT_SELECT_SEARCH));
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
}
