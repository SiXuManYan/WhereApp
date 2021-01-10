package com.jcs.where.hotel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.utils.LocalLanguageUtil;
import com.jcs.where.widget.calendar.JcsCalendarAdapter;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.CategoryResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.widget.calendar.JcsCalendarDialog;
import com.jcs.where.hotel.fragment.HotelListFragment;
import com.jcs.where.hotel.helper.HotelSelectDateHelper;
import com.jcs.where.hotel.tablayout.ColorClipTabLayout;
import com.jcs.where.model.HotelListModel;
import com.jcs.where.view.EnterStayInfoView;
import com.jcs.where.view.popup.PopupConstraintLayoutAdapter;
import com.jcs.where.view.popup.PopupConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private ColorClipTabLayout mTab;
    private ViewPager mViewPager;
    private TextView startDayTv, endDayTv, cityTv;
    private TextView mLiveTextPromptTv, mLeaveTextPromptTv, mCityTv;
    private View mChooseDataView;
    private String mParentCategoryId;
    private int mTotalDay, mRoomNum;
    private List<Fragment> fragments;
    private ImageView clearIv;
    private PopupConstraintLayout mTopPopupLayout;
    private EnterStayInfoView mEnterStayInfoView;
    private JcsCalendarDialog mCalendarDialog;

    private HotelListModel mModel;
    private JcsCalendarAdapter.CalendarBean mStartDateBean;
    private JcsCalendarAdapter.CalendarBean mEndDateBean;

    public static void goTo(Context context, JcsCalendarAdapter.CalendarBean startDateBean, JcsCalendarAdapter.CalendarBean endDateBean, String totalDay, String city, String cityId, String price, String star, int roomNumber, String categoryId) {
        Intent intent = new Intent(context, HotelListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(HotelSelectDateHelper.EXT_START_DATE_BEAN, startDateBean);
        intent.putExtra(HotelSelectDateHelper.EXT_END_DATE_BEAN, endDateBean);
        intent.putExtra(HotelSelectDateHelper.EXT_TOTAL_DAY, totalDay);
        intent.putExtra(HotelSelectDateHelper.EXT_CITY, city);
        intent.putExtra(HotelSelectDateHelper.EXT_CITY_ID, cityId);
        intent.putExtra(HotelSelectDateHelper.EXT_PRICE, price);
        intent.putExtra(HotelSelectDateHelper.EXT_STAR, star);
        intent.putExtra(HotelSelectDateHelper.EXT_ROOM_NUMBER, roomNumber);
        intent.putExtra(HotelSelectDateHelper.EXT_CATEGORY_ID, categoryId);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mCalendarDialog = new JcsCalendarDialog();
        mCalendarDialog.initCalendar();

        mTopPopupLayout = findViewById(R.id.topPopupLayout);
        mEnterStayInfoView = findViewById(R.id.enterStayInfoView);
        mTab = findViewById(R.id.tab);
        mViewPager = findViewById(R.id.viewPager);
        startDayTv = findViewById(R.id.startDayTv);
        endDayTv = findViewById(R.id.endDayTv);
        cityTv = findViewById(R.id.cityTv);
        mLeaveTextPromptTv = findViewById(R.id.leaveTextPrompt);
        mLiveTextPromptTv = findViewById(R.id.liveTextPrompt);
        // cityTv.setText(getIntent().getStringExtra(EXT_CITY));
        mChooseDataView = findViewById(R.id.toChooseDate);

        findViewById(R.id.cityTv).setOnClickListener(view -> HotelSearchActivity.goTo(HotelListActivity.this, getIntent().getStringExtra(HotelSelectDateHelper.EXT_CITY_ID), REQ_SEARCH));
        clearIv = findViewById(R.id.clearIv);
        clearIv.setVisibility(View.GONE);
        clearIv.setOnClickListener(view -> {
            cityTv.setText(getString(R.string.input_hotel_name));
            ((HotelListFragment) fragments.get(0)).setSearchText("");
            clearIv.setVisibility(View.GONE);
            cityTv.setTextColor(getResources().getColor(R.color.grey_b7b7b7));
        });
    }

    @Override
    protected void initData() {
        Locale languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this);
        if (!languageLocale.getLanguage().equals("zh")){
            mLiveTextPromptTv.setVisibility(View.GONE);
            mLeaveTextPromptTv.setVisibility(View.GONE);
        }
        Intent intent = getIntent();
        mStartDateBean = (JcsCalendarAdapter.CalendarBean) intent.getSerializableExtra(HotelSelectDateHelper.EXT_START_DATE_BEAN);
        mEndDateBean = (JcsCalendarAdapter.CalendarBean) intent.getSerializableExtra(HotelSelectDateHelper.EXT_END_DATE_BEAN);
        mTotalDay = intent.getIntExtra(HotelSelectDateHelper.EXT_TOTAL_DAY, 0);
        mRoomNum = intent.getIntExtra(HotelSelectDateHelper.EXT_ROOM_NUMBER, 1);
        mParentCategoryId = intent.getStringExtra(HotelSelectDateHelper.EXT_CATEGORY_ID);
        startDayTv.setText(mStartDateBean.getShowMonthDayDateWithSplit());
        endDayTv.setText(mEndDateBean.getShowMonthDayDateWithSplit());
        mTopPopupLayout.setAdapter(new PopupConstraintLayoutAdapter() {

            @Override
            public boolean showShadow() {
                return true;
            }

            @Override
            public int getMaxHeight() {
                return getPxFromDp(120);
            }
        });

        mEnterStayInfoView.bindEnterStayInfoAdapter(this::toShowCalendarDialog);
        mEnterStayInfoView.setStartAndEnd(mStartDateBean, mEndDateBean);
        mEnterStayInfoView.setRoomNum(mRoomNum);

        mModel = new HotelListModel();
        showLoading();
        mModel.getCategories(3, mParentCategoryId, new BaseObserver<List<CategoryResponse>>() {
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
        mCalendarDialog.setOnDateSelectedListener(this::onDateSelected);
        findViewById(R.id.iv_map).setOnClickListener(this::onMapClicked);
    }

    private void onMapClicked(View view) {
        Intent intent = getIntent();
        HotelMapActivity.goTo(
                HotelListActivity.this,
                mStartDateBean,
                mEndDateBean,
                mTotalDay,
                intent.getStringExtra(HotelSelectDateHelper.EXT_CITY),
                intent.getStringExtra(HotelSelectDateHelper.EXT_CITY_ID),
                intent.getStringExtra(HotelSelectDateHelper.EXT_PRICE),
                intent.getStringExtra(HotelSelectDateHelper.EXT_STAR),
                mRoomNum,
                intent.getStringExtra(HotelSelectDateHelper.EXT_CATEGORY_ID)
        );
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
                    getIntent().getStringExtra(HotelSelectDateHelper.EXT_CITY_ID),
                    getIntent().getStringExtra(HotelSelectDateHelper.EXT_PRICE),
                    getIntent().getStringExtra(HotelSelectDateHelper.EXT_STAR),
                    mStartDateBean,
                    mEndDateBean,
                    mTotalDay,
                    mRoomNum));
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

    public void onDateSelected(JcsCalendarAdapter.CalendarBean startDate, JcsCalendarAdapter.CalendarBean endDate) {
        mEnterStayInfoView.setStartAndEnd(startDate, endDate);
        if (startDate != null) {
            startDayTv.setText(startDate.getShowMonthDayDateWithSplit());
        }

        if (endDate != null) {
            endDayTv.setText(endDate.getShowMonthDayDateWithSplit());
        }
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotel_list;
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
