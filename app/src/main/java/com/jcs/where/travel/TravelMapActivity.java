package com.jcs.where.travel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelTypeBean;
import com.jcs.where.bean.TravelListBean;
import com.jcs.where.hotel.card.ShadowTransformer;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.travel.fragment.TravelListFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.ToastUtils;

public class TravelMapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ViewPager viewPager;
    private TravelCardAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();
    private final List<View> views = new ArrayList<View>();
    private String lat = "14.6778362";
    private String lng = "120.5306459";
    private TextView cityTv;
    private RelativeLayout hotelMapRl;
    private int lastPostition = 0;
    private int lastScrollPosition = 0;
    private ImageView clearIv;
    private SlidingUpPanelLayout mLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragments;


    private static final LatLng ADELAIDE = new LatLng(14.6778362, 120.5306459);

    private final int READ_CODE = 10;
    private final int READ_LOCATIONCODE = 11;
    private FusedLocationProviderClient fusedLocationClient;
    private String useInputText = "";
    private SlidingTabLayout mTab;

    public static void goTo(Context context) {
        Intent intent = new Intent(context, TravelMapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setStatusBar();

        initView();
    }

    private void initView() {
        viewPager = V.f(this, R.id.viewpager);
        hotelMapRl = V.f(this, R.id.rl_hotelmap);
        mCardAdapter = new TravelCardAdapter(TravelMapActivity.this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mMarkerRainbow.get(position).showInfoWindow();
                TextView priceTv = views.get(position).findViewById(R.id.tv_price);
                views.get(position).setBackground(getResources().getDrawable(R.drawable.ic_markselected));
                priceTv.setTextColor(getResources().getColor(R.color.white));
                ((ViewGroup) views.get(position).getParent()).removeView(views.get(position));
                mMarkerRainbow.get(position).setIcon(fromView(TravelMapActivity.this, views.get(position)));
                TextView price1Tv = views.get(lastScrollPosition).findViewById(R.id.tv_price);
                views.get(lastScrollPosition).setBackground(getResources().getDrawable(R.drawable.ic_mark));
                price1Tv.setTextColor(getResources().getColor(R.color.blue_4C9EF2));
                ((ViewGroup) views.get(lastScrollPosition).getParent()).removeView(views.get(lastScrollPosition));
                mMarkerRainbow.get(lastScrollPosition).setIcon(fromView(TravelMapActivity.this, views.get(lastScrollPosition)));
                lastScrollPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        cityTv = V.f(this, R.id.tv_city);
        V.f(this, R.id.rl_mylocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(TravelMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TravelMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TravelMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, READ_LOCATIONCODE);
                    ActivityCompat.requestPermissions(TravelMapActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, READ_CODE);
                    return;
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(TravelMapActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marklocation)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10f));
                                }
                            }
                        });
            }
        });
        clearIv = V.f(this, R.id.iv_clear);
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
        mLayout = V.f(this, R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("ssss", "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        mTab = V.f(this, R.id.tab);
        mViewPager = V.f(this, R.id.viewPager);
        initData();
    }

    private void initData() {
        showLoading();
        String url = "travelapi/v1/searchList?lat=" + lat + "&lng=" + lng + "&name=" + useInputText;
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

        HttpUtils.doHttpReqeust("GET", url, null, "", TokenManager.get().getToken(TravelMapActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {

                    TravelListBean travelListBean = new Gson().fromJson(result, TravelListBean.class);
                    lastPostition = 0;
                    lastScrollPosition = 0;
                    if (travelListBean.getData() != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ADELAIDE, 10f));
                        for (int i = 0; i < travelListBean.getData().size(); i++) {
                            View view = LayoutInflater.from(TravelMapActivity.this).inflate(R.layout.custom_marker_layout, null);
                            TextView peiceTv = V.f(view, R.id.tv_price);
                            peiceTv.setText(travelListBean.getData().get(i).getName());
                            views.add(view);
                            BitmapDescriptor bitmapDescriptor = fromView(TravelMapActivity.this, view);
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(
                                            travelListBean.getData().get(i).getLat(),
                                            travelListBean.getData().get(i).getLng()))
                                    // .title("₱" + list.get(i).getPrice() + "起")
                                    .icon(bitmapDescriptor));
                            mMarkerRainbow.add(marker);
                        }

                        views.get(0).setBackground(getResources().getDrawable(R.drawable.ic_markselected));
                        TextView priceTv = views.get(0).findViewById(R.id.tv_price);
                        priceTv.setTextColor(getResources().getColor(R.color.white));
                        ((ViewGroup) views.get(0).getParent()).removeView(views.get(0));
                        mMarkerRainbow.get(0).setIcon(fromView(TravelMapActivity.this, views.get(0)));

                        for (int i = 0; i < travelListBean.getData().size(); i++) {
                            mCardAdapter.addCardItem(travelListBean.getData().get(i));
                        }
                        mCardShadowTransformer = new ShadowTransformer(viewPager, mCardAdapter);
                        mCardShadowTransformer.enableScaling(true);
                        viewPager.setAdapter(mCardAdapter);
                        viewPager.setPageTransformer(false, mCardShadowTransformer);
                        viewPager.setOffscreenPageLimit(3);
                        getTab();
                    } else {
                        ToastUtils.showLong(TravelMapActivity.this, "未查询到该酒店");
                    }
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(TravelMapActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(TravelMapActivity.this, e.getMessage());
            }
        });
    }

    private void getTab() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "travelapi/v1/categories", null, "", TokenManager.get().getToken(TravelMapActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<HotelTypeBean>>() {
                    }.getType();
                    List<HotelTypeBean> list = gson.fromJson(result, type);
                    HotelTypeBean bean = new HotelTypeBean();
                    bean.setId(0);
                    bean.setName("全部");
                    list.add(0, bean);
                    initTab(list);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(TravelMapActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(TravelMapActivity.this, e.getMessage());
            }
        });
    }

    private void initTab(List<HotelTypeBean> list) {


        final List<String> titles = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            titles.add(list.get(i).getName());
        }

        fragments = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            fragments.add(TravelListFragment.newInstance(String.valueOf(list.get(i).getId())));
        }
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        mTab.setViewPager(mViewPager, titles);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_travelmap;
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
                    views.get(i).setBackground(getResources().getDrawable(R.drawable.ic_markselected));
                    priceTv.setTextColor(getResources().getColor(R.color.white));
                    ((ViewGroup) views.get(i).getParent()).removeView(views.get(i));
                    marker.setIcon(fromView(TravelMapActivity.this, views.get(i)));
                    TextView price1Tv = views.get(lastPostition).findViewById(R.id.tv_price);
                    views.get(lastPostition).setBackground(getResources().getDrawable(R.drawable.ic_mark));
                    price1Tv.setTextColor(getResources().getColor(R.color.blue_4C9EF2));
                    ((ViewGroup) views.get(lastPostition).getParent()).removeView(views.get(lastPostition));
                    mMarkerRainbow.get(lastPostition).setIcon(fromView(TravelMapActivity.this, views.get(lastPostition)));
                    lastPostition = i;
                }
            }

        }
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        return false;
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

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
