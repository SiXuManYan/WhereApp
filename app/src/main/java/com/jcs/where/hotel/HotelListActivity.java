package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.hotel.fragment.HotelListFragment;
import com.jcs.where.hotel.tablayout.ColorClipTabLayout;
import com.jcs.where.model.HotelListModel;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.annotations.NonNull;

/**
 * 酒店商家列表模式
 * 酒店查询搜索页面
 */
public class HotelListActivity extends BaseActivity {

    private static final int REQ_SEARCH = 666;
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
    private static final String EXT_CATEGORY_ID = "categoryId";
    private ColorClipTabLayout mTab;
    private ViewPager mViewPager;
    private TextView startDayTv, endDayTv, cityTv;
    private View mChooseDataView;
    private String mStartYear, mStartDate, mStartWeek, mEndYear, mEndData, mEndWeek, mAllDay, mRoomNum, mParentCategoryId;
    private List<Fragment> fragments;
    private ImageView clearIv;

    private HotelListModel mModel;

    public static void goTo(Context context, String startDate, String endDate, String startWeek, String endWeek, String allDay, String city, String cityId, String price, String star, String startYear, String endYear, String roomNumber, String categoryId) {
        Intent intent = new Intent(context, HotelListActivity.class);
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
        intent.putExtra(EXT_CATEGORY_ID, categoryId);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mTab = findViewById(R.id.tab);
        mViewPager = findViewById(R.id.viewPager);
        startDayTv = findViewById(R.id.startDayTv);
        startDayTv.setText(getIntent().getStringExtra(EXT_STARTDATE).replace("月", "-").replace("日", ""));
        endDayTv = findViewById(R.id.endDayTv);
        endDayTv.setText(getIntent().getStringExtra(EXT_ENDDATE).replace("月", "-").replace("日", ""));
        cityTv = findViewById(R.id.cityTv);
        // cityTv.setText(getIntent().getStringExtra(EXT_CITY));
        mChooseDataView = findViewById(R.id.toChooseDate);
        mChooseDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new DatePopupWindow
//                        .Builder((Activity) HotelListActivity.this, Calendar.getInstance().getTime(), view)
//                        .setInitSelect(-1, -1, -1, -1)
//                        .setInitDay(false)
//                        .setDateOnClickListener(new DatePopupWindow.DateOnClickListener() {
//                            @Override
//                            public void getDate(String startYear, String endYear, String startDate, String endDate, String startWeek, String endWeek, int allDay, int startGroupPosition, int startChildPosition, int endGroupPosition, int endChildPosition) {
//                                String mStartTime = CalendarUtil.FormatDateYMD(startDate);
//                                String mEndTime = CalendarUtil.FormatDateYMD(endDate);
//                                startDayTv.setText(mStartTime.replace("月", "-").replace("日", ""));
//                                endDayTv.setText(mEndTime.replace("月", "-").replace("日", ""));
//                                mStartYear = startYear;
//                                mEndYear = endYear;
//                                mStartDate = mStartTime;
//                                mEndData = mEndTime;
//                                mStartWeek = startWeek;
//                                mEndWeek = endWeek;
//                                mAllDay = "共" + allDay + "晚";
//                                //  ToastUtils.showLong(HotelListActivity.this, "您选择了：" + mStartTime + startWeek + "到" + mEndTime + endWeek);
//                            }
//                        }).builder();

                View customView = View.inflate(HotelListActivity.this, R.layout.pop_maptitle, null);
                TextView startDateTv = customView.findViewById(R.id.tv_startdate);
                startDateTv.setText(mStartDate);
                TextView endDateTv = customView.findViewById(R.id.tv_enddate);
                endDateTv.setText(mEndData);
                TextView allDayTv = customView.findViewById(R.id.tv_allday);
                allDayTv.setText(mAllDay);
                TextView roomNumTv = customView.findViewById(R.id.tv_roomnum);
                roomNumTv.setText(mRoomNum);
                customView.findViewById(R.id.iv_roomreduce).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int roomNum = Integer.parseInt(roomNumTv.getText().toString());
                        if (roomNum == 1) {
                            showToast("不能再减了");
                        } else {
                            roomNum--;
                            roomNumTv.setText(String.valueOf(roomNum));
                            for (int i = 0; i < fragments.size(); i++) {
                                ((HotelListFragment) fragments.get(i)).changeData(mStartDate, mEndData, mStartWeek, mEndWeek, mAllDay, mStartYear, mEndYear, roomNumTv.getText().toString());
                            }
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
                        for (int i = 0; i < fragments.size(); i++) {
                            ((HotelListFragment) fragments.get(i)).changeData(mStartDate, mEndData, mStartWeek, mEndWeek, mAllDay, mStartYear, mEndYear, roomNumTv.getText().toString());
                        }
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
        findViewById(R.id.iv_map).setOnClickListener(view -> HotelMapActivity.goTo(HotelListActivity.this,
                mStartDate,
                mEndData,
                mStartWeek,
                mEndWeek,
                mAllDay,
                getIntent().getStringExtra(EXT_CITY),
                getIntent().getStringExtra(EXT_CITYID),
                getIntent().getStringExtra(EXT_PRICE),
                getIntent().getStringExtra(EXT_STAR),
                mStartYear,
                mEndYear,
                mRoomNum
        ));
        findViewById(R.id.cityTv).setOnClickListener(view -> HotelSearchActivity.goTo(HotelListActivity.this, getIntent().getStringExtra(EXT_CITYID), REQ_SEARCH));
        clearIv = findViewById(R.id.clearIv);
        clearIv.setVisibility(View.GONE);
        clearIv.setOnClickListener(view -> {
            cityTv.setText("请输入酒店名称");
            ((HotelListFragment) fragments.get(0)).setSearchText("");
            clearIv.setVisibility(View.GONE);
            cityTv.setTextColor(getResources().getColor(R.color.grey_b7b7b7));
        });
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
        mParentCategoryId = getIntent().getStringExtra(EXT_CATEGORY_ID);

        mModel = new HotelListModel();
        showLoading();
        int[] categories = new int[]{Integer.parseInt(mParentCategoryId)};
        mModel.getCategories(3, categories, new BaseObserver<List<CategoryResponse>>() {
            @Override
            public void onNext(@NonNull List<CategoryResponse> categoryResponses) {
                stopLoading();
                CategoryResponse first = new CategoryResponse();
                first.setName("全部");
                categoryResponses.add(0, first);
                initTab(categoryResponses);
            }

            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }
        });
    }

    @Override
    protected void bindListener() {

    }

    private void initTab(List<CategoryResponse> list) {

        final List<String> titles = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            titles.add(list.get(i).getName());
        }

        mTab.setSelectedTabIndicatorHeight(0);
        fragments = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            fragments.add(HotelListFragment.newInstance(String.valueOf(list.get(i).getId()),
                    getIntent().getStringExtra(EXT_CITYID),
                    getIntent().getStringExtra(EXT_PRICE),
                    getIntent().getStringExtra(EXT_STAR),
                    getIntent().getStringExtra(EXT_STARTDATE),
                    getIntent().getStringExtra(EXT_ENDDATE),
                    getIntent().getStringExtra(EXT_STARTWEEK),
                    getIntent().getStringExtra(EXT_ENDWEEK),
                    getIntent().getStringExtra(EXT_ALLDAY),
                    getIntent().getStringExtra(EXT_STARTYEAR),
                    getIntent().getStringExtra(EXT_ENDYEAR),
                    getIntent().getStringExtra(EXT_ROOMNUMBER)));
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
        mTab.setupWithViewPager(mViewPager);

        mTab.setLastSelectedTabPosition(0);
        mTab.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(titles.size());
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotellist;
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SEARCH && data != null) {
            clearIv.setVisibility(View.VISIBLE);
            mViewPager.setCurrentItem(0);
            cityTv.setText(data.getStringExtra(HotelSearchActivity.EXT_SELECT_SEARCH));
            cityTv.setTextColor(getResources().getColor(R.color.grey_666666));
            ((HotelListFragment) fragments.get(0)).setSearchText(data.getStringExtra(HotelSearchActivity.EXT_SELECT_SEARCH));
        }
    }
}
