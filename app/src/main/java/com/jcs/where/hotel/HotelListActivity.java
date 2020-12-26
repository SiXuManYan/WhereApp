package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.adapter.JcsCalendarAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.home.dialog.JcsCalendarDialog;
import com.jcs.where.hotel.fragment.HotelListFragment;
import com.jcs.where.hotel.tablayout.ColorClipTabLayout;
import com.jcs.where.model.HotelListModel;
import com.jcs.where.view.EnterStayInfoView;
import com.jcs.where.view.popup.PopupConstraintLayoutAdapter;
import com.jcs.where.view.popup.TopPopupConstraintLayout;

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
    private static final String EXT_START_DATE_BEAN = "startDate";
    private static final String EXT_END_DATE_BEAN = "endDate";
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
    private String mAllDay, mRoomNum, mParentCategoryId;
    private List<Fragment> fragments;
    private ImageView clearIv;
    private TopPopupConstraintLayout mTopPopupLayout;
    private EnterStayInfoView mEnterStayInfoView;
    private JcsCalendarDialog mCalendarDialog;

    private HotelListModel mModel;
    private JcsCalendarAdapter.CalendarBean mStartDateBean;
    private JcsCalendarAdapter.CalendarBean mEndDateBean;

    public static void goTo(Context context, JcsCalendarAdapter.CalendarBean startDateBean, JcsCalendarAdapter.CalendarBean endDateBean, String allDay, String city, String cityId, String price, String star, String roomNumber, String categoryId) {
        Intent intent = new Intent(context, HotelListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXT_START_DATE_BEAN, startDateBean);
        intent.putExtra(EXT_END_DATE_BEAN, endDateBean);
        intent.putExtra(EXT_ALLDAY, allDay);
        intent.putExtra(EXT_CITY, city);
        intent.putExtra(EXT_CITYID, cityId);
        intent.putExtra(EXT_PRICE, price);
        intent.putExtra(EXT_STAR, star);
        intent.putExtra(EXT_ROOMNUMBER, roomNumber);
        intent.putExtra(EXT_CATEGORY_ID, categoryId);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mTopPopupLayout = findViewById(R.id.topPopupLayout);
        mEnterStayInfoView = findViewById(R.id.enterStayInfoView);
        mTab = findViewById(R.id.tab);
        mViewPager = findViewById(R.id.viewPager);
        startDayTv = findViewById(R.id.startDayTv);
        endDayTv = findViewById(R.id.endDayTv);
        cityTv = findViewById(R.id.cityTv);
        // cityTv.setText(getIntent().getStringExtra(EXT_CITY));
        mChooseDataView = findViewById(R.id.toChooseDate);

//        findViewById(R.id.iv_map).setOnClickListener(view -> HotelMapActivity.goTo(HotelListActivity.this,
//                mStartDate,
//                mEndData,
//                mStartWeek,
//                mEndWeek,
//                mAllDay,
//                getIntent().getStringExtra(EXT_CITY),
//                getIntent().getStringExtra(EXT_CITYID),
//                getIntent().getStringExtra(EXT_PRICE),
//                getIntent().getStringExtra(EXT_STAR),
//                mStartYear,
//                mEndYear,
//                mRoomNum
//        ));
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
        mCalendarDialog = new JcsCalendarDialog();
        mCalendarDialog.initCalendar();
        Intent intent = getIntent();
        mStartDateBean = (JcsCalendarAdapter.CalendarBean) intent.getSerializableExtra(EXT_START_DATE_BEAN);
        mEndDateBean = (JcsCalendarAdapter.CalendarBean) intent.getSerializableExtra(EXT_END_DATE_BEAN);
        mAllDay = intent.getStringExtra(EXT_ALLDAY);
        mRoomNum = intent.getStringExtra(EXT_ROOMNUMBER);
        mParentCategoryId = intent.getStringExtra(EXT_CATEGORY_ID);
        startDayTv.setText(mStartDateBean.getShowMonthDayDateWithSplit());
        endDayTv.setText(mEndDateBean.getShowMonthDayDateWithSplit());
        mTopPopupLayout.setAdapter(new PopupConstraintLayoutAdapter() {

            @Override
            public int getMaxHeight() {
                return getPxFromDp(120);
            }
        });

        mEnterStayInfoView.bindEnterStayInfoAdapter(this::toShowCalendarDialog);
        mEnterStayInfoView.setStartAndEnd(mStartDateBean, mEndDateBean);

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
        mChooseDataView.setOnClickListener(this::onChooseViewClicked);
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
                    getIntent().getStringExtra(EXT_START_DATE_BEAN),
                    getIntent().getStringExtra(EXT_END_DATE_BEAN),
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

    public void onChooseViewClicked(View view) {
        mTopPopupLayout.showOrHide();
    }

    public void toShowCalendarDialog() {
        if (!mCalendarDialog.isVisible()) {
            mCalendarDialog.show(getSupportFragmentManager());
        }
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
