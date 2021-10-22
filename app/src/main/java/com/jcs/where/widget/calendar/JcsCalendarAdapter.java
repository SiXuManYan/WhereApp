package com.jcs.where.widget.calendar;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.entity.JSectionEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

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
        TextView actionTv = baseViewHolder.findView(R.id.actionTv);
        if (actionTv == null || dateView == null || leftView == null || rightView == null) {
            return;
        }
        // day 为 0 ，说明是空白item
        if (calendarBean.day != 0) {
            baseViewHolder.setText(R.id.dayTv, String.valueOf(calendarBean.day));
            View itemView = baseViewHolder.itemView;
            if (calendarBean.isStartDay) {
                if (mIsEndSelected) {
                    rightView.setBackgroundColor(ColorUtils.getColor(R.color.color_DCE8FF));
                }
                dateView.setBackgroundResource(R.mipmap.start_date_selected);
                actionTv.setText(R.string.calendar_enter_stay);
            } else if (calendarBean.isEndDay) {
                leftView.setBackgroundColor(ColorUtils.getColor(R.color.color_DCE8FF));
                dateView.setBackgroundResource(R.mipmap.end_date_selected);
                actionTv.setText(R.string.calendar_leave_stay);
            } else if (calendarBean.isSelected) {
                leftView.setBackgroundColor(ColorUtils.getColor(R.color.color_DCE8FF));
                rightView.setBackgroundColor(ColorUtils.getColor(R.color.color_DCE8FF));
                dateView.setBackgroundColor(ColorUtils.getColor(R.color.color_DCE8FF));
                actionTv.setText("");
            } else {
                actionTv.setText("");
                dateView.setBackgroundResource(0);
                leftView.setBackgroundColor(Color.WHITE);
                rightView.setBackgroundColor(Color.WHITE);
            }
        } else {
            baseViewHolder.setText(R.id.dayTv, "");
            dateView.setBackgroundResource(0);
            dateView.setBackgroundColor(Color.WHITE);
            leftView.setBackgroundColor(Color.WHITE);
            rightView.setBackgroundColor(Color.WHITE);
        }
    }

    public boolean isEndSelected() {
        return mIsEndSelected;
    }

    public void setEndSelected(boolean mIsEndSelected) {
        this.mIsEndSelected = mIsEndSelected;
    }

    public static class CalendarBean extends JSectionEntity implements Serializable {
        public long time;

        /**
         * 中文：2021年10月
         * en: Oct 2021
         */
        public String showYearMonthDate;
        /**
         * 中文：9月10日
         * en: Oct 22
         */
        public String showMonthDayDate;
        /**
         * 中文：09-10
         * en: OCT 22
         */
        public String showMonthDayDateWithSplit;
        /**
         * 中文：2021-01-10
         * en: 2021-10-11
         */
        public String showYearMonthDayDateWithSplit;

        /**
         * 中英文：Fri
         */
        public String showWeekday;
        public int year;
        public int month;
        public int day;
        public boolean isSelected;
        public boolean isStartDay;
        public boolean isEndDay;
        public boolean isToday;
        public int itemType;
        public boolean isHeader;

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

        public String getShowYearMonthDayDateWithSplit() {
            return showYearMonthDayDateWithSplit;
        }

        public void setShowYearMonthDayDateWithSplit(String showYearMonthDayDateWithSplit) {
            this.showYearMonthDayDateWithSplit = showYearMonthDayDateWithSplit;
        }

        @Override
        public boolean isHeader() {
            return isHeader;
        }
    }
}
