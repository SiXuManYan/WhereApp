package com.jiechengsheng.city.widget.calendar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.base.BaseDialog;
import com.jiechengsheng.city.home.decoration.HotelCalendarItemDecoration;
import com.jiechengsheng.city.utils.LocalLanguageUtil;
import com.jiechengsheng.city.widget.calendar.JcsCalendarAdapter.CalendarBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class JcsCalendarDialog extends BaseDialog {

    private ImageView mCloseIv;
    private RecyclerView mRecycler;
    private TextView mEnsureBtn;
    private List<CalendarBean> mBeans;
    private CalendarBean mStartBean, mEndBean;
    private JcsCalendarAdapter mAdapter;
    private final int[] mStartAndEndItemPosition = new int[2];
    private final List<Integer> mSelectBetweenStartAndEnd = new ArrayList<>();
    private HotelCalendarItemDecoration mItemDecoration;
    private OnDateSelectedListener mOnDateSelectedListener;
    private SimpleDateFormat mYearMonthSF;
    private SimpleDateFormat mMonthDaySF;
    private SimpleDateFormat mMonthDayWithSplitSF;
    private SimpleDateFormat mYearMonthDayWithSplitSF;
    private SimpleDateFormat mWeekdaySF;

    @Override
    protected boolean isBottom() {
        return false;
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_hotel_calendar;
    }

    @Override
    protected int getHeight() {
        return ScreenUtils.getScreenHeight();
    }

    @Override
    protected void initView(View view) {
        mRecycler = view.findViewById(R.id.calendarRecycler);
        mCloseIv = view.findViewById(R.id.close);
        mEnsureBtn = view.findViewById(R.id.ensureBtn);
    }

    @Override
    protected void initData() {
        if (mBeans == null || mBeans.size() == 0) {
            return;
        }
        mAdapter = new JcsCalendarAdapter(R.layout.item_hotel_calendar_dialog_header, R.layout.item_hotel_calendar_dialog_child, mBeans);
        mRecycler.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 7);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItem(position).isHeader()) {
                    return 7;
                }
                return 1;
            }
        });
        mRecycler.setLayoutManager(gridLayoutManager);
        mItemDecoration = new HotelCalendarItemDecoration(getContext());
        mRecycler.addItemDecoration(mItemDecoration);


        // 默认选中

        int start = mStartAndEndItemPosition[0];
        int end = mStartAndEndItemPosition[1];

        if (start == 0 &&   end == 0) {
            try {
                performItemClick(getBeanPosition(mStartBean));
                performItemClick(getBeanPosition(mEndBean));
            } catch (Exception ignored) {

            }

        }

    }

    @Override
    protected void bindListener() {
        mCloseIv.setOnClickListener(view -> dismiss());
        mAdapter.setOnItemClickListener(this::onItemClick);
        mEnsureBtn.setOnClickListener(this::onEnsureBtnClicked);
    }

    public void onEnsureBtnClicked(View view) {
        int start = mStartAndEndItemPosition[0];
        int end = mStartAndEndItemPosition[1];
        if (start > 0 && end > 0) {
            if (mOnDateSelectedListener != null) {
                mStartBean = mAdapter.getItem(start);
                mEndBean = mAdapter.getItem(end);
                mOnDateSelectedListener.onDateSelected(mAdapter.getItem(start), mAdapter.getItem(end));
            }
        }
        dismiss();
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.mOnDateSelectedListener = onDateSelectedListener;
    }

    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        performItemClick(position);
    }

    private void performItemClick(int position) {
        CalendarBean item = mAdapter.getItem(position);
        if (position == mStartAndEndItemPosition[0]) {
            //点击位置是已保存的开始日期，什么都不做
            return;
        }
        // item.getDay() 为 0，说明点击的是空白位置，不需要执行
        if (item.getDay() != 0) {
            // mSelectItemPosition索引为0存储startDate
            // mSelectItemPosition索引为1存储endDate
            int startDatePosition = mStartAndEndItemPosition[0];
            int endDatePosition = mStartAndEndItemPosition[1];
            if (startDatePosition != 0 && endDatePosition != 0) {
                // 说明已经选好了开始和结束日期，再次点击，把已保存的日期索引都清空
                // 重新选择
                unSelectStart(mAdapter.getItem(startDatePosition));
                unSelectEnd(mAdapter.getItem(endDatePosition));
                mAdapter.notifyItemChanged(startDatePosition);
                mAdapter.notifyItemChanged(endDatePosition);
                // 把已保存的日期索引都清空
                mStartAndEndItemPosition[0] = 0;
                mStartAndEndItemPosition[1] = 0;
                startDatePosition = 0;
                endDatePosition = 0;
                unSelectStartToEnd();
                mAdapter.setEndSelected(false);
            }
            // 若为0表示这次选择的是开始日期
            // 若当前选择的索引小于mSelectItemPosition[0]，说明选择了一个更早的开始日期
            //   则需要对已保存的开始日期覆盖
            if (startDatePosition == 0) {
                // 保存 开始 日期的索引
                mStartAndEndItemPosition[0] = position;
                selectStart(item);
            } else if (startDatePosition > position) {
                // 重置原来保存的 开始 日期的选择状态
                CalendarBean oldStart = mAdapter.getItem(startDatePosition);
                unSelectStart(oldStart);
                mAdapter.notifyItemChanged(startDatePosition);
                // 保存 开始 日期的索引
                mStartAndEndItemPosition[0] = position;
                selectStart(item);
                unSelectStartToEnd();
            } else {
                // 保存 结束 日期的索引
                mStartAndEndItemPosition[1] = position;
                selectEnd(item);
                // 表示已经选择了结束日期
                // 在 ItemDecoration 的 onDraw() 中需要这个状态
                mAdapter.setEndSelected(true);
                // 开始日期与结束日期之间的item设置为选择状态
                // 并将这部分item的position保存，用于重置状态
                selectStartToEnd(startDatePosition, position);
                mItemDecoration.setStartPosition(startDatePosition);
                mItemDecoration.setEndPosition(position);
                mAdapter.notifyItemChanged(startDatePosition);
            }
            // 刷新索引位置的item视图
            mAdapter.notifyItemChanged(position);
        }
    }

    /**
     * 将已选择的开始结束日期之间的item重置选择状态
     */
    private void unSelectStartToEnd() {
        int size = mSelectBetweenStartAndEnd.size();
        int itemCount = mAdapter.getItemCount();
        for (int i = 0; i < size; i++) {
            Integer position = mSelectBetweenStartAndEnd.get(i);
            if (position < itemCount) {
                CalendarBean item = mAdapter.getItem(position);
                if (item != null) {
                    item.setSelected(false);
                    mAdapter.notifyItemChanged(position);
                }
            }
        }

        mSelectBetweenStartAndEnd.clear();
    }

    /**
     * 将开始日期和结束日期之间的item设置为选择状态
     *
     * @param startDatePosition 开始日期索引
     * @param position          结束日期索引
     */
    private void selectStartToEnd(int startDatePosition, int position) {
        int itemCount = mAdapter.getItemCount();
        int middlePosition = startDatePosition + 1;
        while (middlePosition < position) {
            if (middlePosition < itemCount) {
                mSelectBetweenStartAndEnd.add(middlePosition);
                mAdapter.getItem(middlePosition).setSelected(true);
                mAdapter.notifyItemChanged(middlePosition);
            }
            middlePosition++;
        }
    }

    private void selectStart(CalendarBean bean) {
        bean.setEndDay(false);
        bean.setStartDay(true);
    }

    private void selectEnd(CalendarBean bean) {
        bean.setStartDay(false);
        bean.setEndDay(true);
    }

    public CalendarBean getStartBean() {
        return mStartBean;
    }

    public void setStartBean(CalendarBean startBean) {
        this.mStartBean = startBean;
    }

    public CalendarBean getEndBean() {
        return mEndBean;
    }

    public void setEndBean(CalendarBean endBean) {
        this.mEndBean = endBean;
    }

    private void unSelectStart(CalendarBean bean) {
        bean.setStartDay(false);
    }

    private void unSelectEnd(CalendarBean bean) {
        bean.setEndDay(false);
    }

    @Override
    protected boolean isTransparent() {
        return true;
    }

    public void initCalendar() {

        Locale languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(getContext());

        mYearMonthSF = new SimpleDateFormat(StringUtils.getString(R.string.date_format_year_month), languageLocale);
        mMonthDaySF = new SimpleDateFormat(StringUtils.getString(R.string.date_format_month_day), languageLocale);
        mMonthDayWithSplitSF = new SimpleDateFormat(StringUtils.getString(R.string.date_format_mm_dd), languageLocale);
        mYearMonthDayWithSplitSF = new SimpleDateFormat(StringUtils.getString(R.string.date_format_yyyy_mm_dd), languageLocale);
        mWeekdaySF = new SimpleDateFormat("E", languageLocale);

        Calendar instance = Calendar.getInstance();

        // 用户从来没做过日期选择，默认是当天和下一天
        if (mStartBean == null) {
            // 获得当前日期
            mStartBean = new CalendarBean();
            deployCalendarBean(mStartBean, instance);
            // 默认住一天，那么end就是start的下一天
            instance.add(Calendar.DAY_OF_YEAR, 1);
            mEndBean = new CalendarBean();
            deployCalendarBean(mEndBean, instance);
            instance.clear();
        }


        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();

        instance.set(Calendar.YEAR, 2023);
        instance.set(Calendar.MONTH, 11);

        int max = instance.getActualMaximum(Calendar.DATE);


        // 开始日期
        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
        int nowMonth = Calendar.getInstance().get(Calendar.MONTH);
        int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        start.set(Calendar.YEAR, nowYear);
        start.set(Calendar.MONTH, nowMonth);
        start.set(Calendar.DAY_OF_MONTH, nowDay);

        // 结束日期
        end.set(Calendar.YEAR, instance.get(Calendar.YEAR));
        end.set(Calendar.MONTH, instance.get(Calendar.MONTH));
        end.set(Calendar.DAY_OF_MONTH, max);

        mBeans = new ArrayList<>();


        int tempMonth = -1;
        int tempYear = 0;
        while (start.getTime().getTime() <= end.getTime().getTime()) {
            if (tempMonth == -1 || start.get(Calendar.YEAR) > tempYear || start.get(Calendar.MONTH) - tempMonth == 1) {
                tempMonth = start.get(Calendar.MONTH);
                tempYear = start.get(Calendar.YEAR);
                CalendarBean header = new CalendarBean();
                header.setShowYearMonthDate(mYearMonthSF.format(start.getTime()));
                header.setMIsHeader(true);
                mBeans.add(header);

                int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
                for (int ofWeek = dayOfWeek - 1; ofWeek > 0; ofWeek--) {
                    mBeans.add(new CalendarBean());
                }
            }

            CalendarBean day = new CalendarBean();
            deployCalendarBean(day, start);
            mBeans.add(day);
            start.add(Calendar.DAY_OF_MONTH, 1);
        }


    }


    private int getBeanPosition(CalendarBean bean) {
        int startDay = bean.getDay();
        int position = 0;

        for (int i = 0; i < mAdapter.getData().size(); i++) {
            CalendarBean item = mAdapter.getData().get(i);
            if (item.getDay() == startDay) {
                position = i;
                break;
            }
        }
        return position;
    }


    private void deployCalendarBean(CalendarBean calendarBean, Calendar instance) {
        calendarBean.setTime(instance.getTime().getTime());
        calendarBean.setDay(instance.get(Calendar.DAY_OF_MONTH));
        calendarBean.setMonth(instance.get(Calendar.MONTH));
        calendarBean.setYear(instance.get(Calendar.YEAR));
        calendarBean.setShowYearMonthDate(mYearMonthSF.format(instance.getTime()));
        calendarBean.setShowMonthDayDate(mMonthDaySF.format(instance.getTime()));
        calendarBean.setShowMonthDayDateWithSplit(mMonthDayWithSplitSF.format(instance.getTime()));
        calendarBean.setShowYearMonthDayDateWithSplit(mYearMonthDayWithSplitSF.format(instance.getTime()));
        calendarBean.setShowWeekday(mWeekdaySF.format(instance.getTime()));
    }

    public String getTotalDay() {
        long startTime = mStartBean.getTime();
        long endTime = mEndBean.getTime();
        long temp = endTime - startTime;
        long dayNum = TimeUnit.DAYS.convert(temp, TimeUnit.MILLISECONDS);
        return String.format(getContext().getString(R.string.total_night), dayNum);
    }

    public int getTotalDay2() {
        long startTime = mStartBean.getTime();
        long endTime = mEndBean.getTime();
        long temp = endTime - startTime;
        return (int) TimeUnit.DAYS.convert(temp, TimeUnit.MILLISECONDS);
    }

    public interface OnDateSelectedListener {
        void onDateSelected(CalendarBean startDate, CalendarBean endDate);
    }


}
