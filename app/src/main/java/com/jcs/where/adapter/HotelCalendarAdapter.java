package com.jcs.where.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.entity.JSectionEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import androidx.core.content.ContextCompat;

public class HotelCalendarAdapter extends BaseSectionQuickAdapter<HotelCalendarAdapter.HotelCalendarBean, BaseViewHolder> {


    public HotelCalendarAdapter(int sectionHeadResId, @Nullable List<HotelCalendarBean> data) {
        super(sectionHeadResId, data);
//        init();
    }

    public HotelCalendarAdapter(int sectionHeadResId) {
        super(sectionHeadResId);
//        init();
    }

    public HotelCalendarAdapter(int sectionHeadResId, int layoutResId, @Nullable List<HotelCalendarBean> data) {
        super(sectionHeadResId, layoutResId, data);
//        init();
    }

    public void init() {
        addItemType(HotelCalendarBean.YEAR_MONTH_DAY, R.layout.item_hotel_calendar_dialog_header);
        addItemType(HotelCalendarBean.DAY, R.layout.item_hotel_calendar_dialog_child);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder baseViewHolder, @NotNull HotelCalendarBean hotelCalendarBean) {
        baseViewHolder.setText(R.id.yearMonthTv, hotelCalendarBean.showDate);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HotelCalendarBean hotelCalendarBean) {
        // day 为 0 ，说明是空白item，不需要绘制
        if (hotelCalendarBean.day != 0) {
            View itemView = baseViewHolder.itemView;
            TextView actionTv = baseViewHolder.findView(R.id.actionTv);
            baseViewHolder.setText(R.id.dayTv, String.valueOf(hotelCalendarBean.day));
            if (hotelCalendarBean.isStartDay) {
                itemView.setBackgroundResource(R.mipmap.start_date_selected);
                actionTv.setText(R.string.enter_stay);
            } else if (hotelCalendarBean.isEndDay) {
                itemView.setBackgroundResource(R.mipmap.end_date_selected);
                actionTv.setText(R.string.leave_stay);
            } else if (hotelCalendarBean.isSelected) {
                itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_D5EAFF));
                actionTv.setText("");
            } else {
                actionTv.setText("");
                itemView.setBackgroundResource(0);
                itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            }
        }
    }

    public static class HotelCalendarBean extends JSectionEntity {
        long time;
        String showDate;
        int year;
        int month;
        int day;
        boolean isSelected;
        boolean isStartDay;
        boolean isEndDay;
        boolean isToday;
        public static final int YEAR_MONTH_DAY = 1;
        public static final int DAY = 2;
        int itemType;
        boolean isHeader;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getShowDate() {
            return showDate;
        }

        public void setShowDate(String showDate) {
            this.showDate = showDate;
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

        @Override
        public boolean isHeader() {
            return isHeader;
        }
    }
}
