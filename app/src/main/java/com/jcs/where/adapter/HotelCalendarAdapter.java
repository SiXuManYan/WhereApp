package com.jcs.where.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.entity.JSectionEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
        baseViewHolder.setText(R.id.dayTv, String.valueOf(hotelCalendarBean.day == 0 ? "" : hotelCalendarBean.day));
    }

    public static class HotelCalendarBean extends JSectionEntity {
        long time;
        String showDate;
        int year;
        int month;
        int day;
        boolean isSelected;
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

        @Override
        public boolean isHeader() {
            return isHeader;
        }
    }
}
