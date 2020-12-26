package com.jcs.where.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.entity.JSectionEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

import androidx.core.content.ContextCompat;

public class JcsCalendarAdapter extends BaseSectionQuickAdapter<JcsCalendarAdapter.CalendarBean, BaseViewHolder> {
    private boolean mIsEndSelected = false;

    public JcsCalendarAdapter(int sectionHeadResId, int layoutResId, @Nullable List<CalendarBean> data) {
        super(sectionHeadResId, layoutResId, data);
    }


    @Override
    protected void convertHeader(@NotNull BaseViewHolder baseViewHolder, @NotNull CalendarBean calendarBean) {
        baseViewHolder.setText(R.id.yearMonthTv, calendarBean.showYearMonthDate);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, CalendarBean calendarBean) {
        View dateView = baseViewHolder.findView(R.id.dateView);
        View leftView = baseViewHolder.findView(R.id.leftView);
        View rightView = baseViewHolder.findView(R.id.rightView);
        // day 为 0 ，说明是空白item
        if (calendarBean.day != 0) {
            baseViewHolder.setText(R.id.dayTv, String.valueOf(calendarBean.day));
            View itemView = baseViewHolder.itemView;
            TextView actionTv = baseViewHolder.findView(R.id.actionTv);
            if (calendarBean.isStartDay) {
                if (mIsEndSelected) {
                    rightView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_D5EAFF));
                }
                dateView.setBackgroundResource(R.mipmap.start_date_selected);
                actionTv.setText(R.string.enter_stay);
            } else if (calendarBean.isEndDay) {
                leftView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_D5EAFF));
                dateView.setBackgroundResource(R.mipmap.end_date_selected);
                actionTv.setText(R.string.leave_stay);
            } else if (calendarBean.isSelected) {
                leftView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_D5EAFF));
                rightView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_D5EAFF));
                dateView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_D5EAFF));
                actionTv.setText("");
            } else {
                actionTv.setText("");
                dateView.setBackgroundResource(0);
                leftView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                rightView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            }
        } else {
            baseViewHolder.setText(R.id.dayTv, "");
            dateView.setBackgroundResource(0);
            dateView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            leftView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            rightView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }
    }

    public boolean isEndSelected() {
        return mIsEndSelected;
    }

    public void setEndSelected(boolean mIsEndSelected) {
        this.mIsEndSelected = mIsEndSelected;
    }

    public static class CalendarBean extends JSectionEntity implements Serializable {
        long time;
        String showYearMonthDate;
        /**
         * 9月10日
         */
        String showMonthDayDate;
        /**
         * 09-10
         */
        String showMonthDayDateWithSplit;
        String showWeekday;
        int year;
        int month;
        int day;
        boolean isSelected;
        boolean isStartDay;
        boolean isEndDay;
        boolean isToday;
        int itemType;
        boolean isHeader;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getShowYearMonthDate() {
            return showYearMonthDate;
        }

        public void setShowYearMonthDate(String showYearMonthDate) {
            this.showYearMonthDate = showYearMonthDate;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean isToday() {
            return isToday;
        }

        public void setToday(boolean today) {
            isToday = today;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public void setIsHeader(boolean isHeader) {
            this.isHeader = isHeader;
        }

        public boolean isStartDay() {
            return isStartDay;
        }

        public void setStartDay(boolean select) {
            isSelected = select;
            isStartDay = select;
        }

        public boolean isEndDay() {
            return isEndDay;
        }

        public void setEndDay(boolean select) {
            isSelected = select;
            isEndDay = select;
        }

        public String getShowMonthDayDate() {
            return showMonthDayDate;
        }

        public void setShowMonthDayDate(String showMonthDayDate) {
            this.showMonthDayDate = showMonthDayDate;
        }

        public String getShowWeekday() {
            return showWeekday;
        }

        public void setShowWeekday(String showWeekday) {
            this.showWeekday = showWeekday;
        }

        public String getShowMonthDayDateWithSplit() {
            return showMonthDayDateWithSplit;
        }

        public void setShowMonthDayDateWithSplit(String showMonthDayDateWithSplit) {
            this.showMonthDayDateWithSplit = showMonthDayDateWithSplit;
        }

        @Override
        public String toString() {
            return "HotelCalendarBean{" +
                    "time=" + time +
                    ", showYearMonthDate='" + showYearMonthDate + '\'' +
                    ", showMonthDayDate='" + showMonthDayDate + '\'' +
                    ", showWeekday='" + showWeekday + '\'' +
                    ", year=" + year +
                    ", month=" + month +
                    ", day=" + day +
                    ", isSelected=" + isSelected +
                    ", isStartDay=" + isStartDay +
                    ", isEndDay=" + isEndDay +
                    ", isToday=" + isToday +
                    ", itemType=" + itemType +
                    ", isHeader=" + isHeader +
                    '}';
        }

        @Override
        public boolean isHeader() {
            return isHeader;
        }
    }
}
