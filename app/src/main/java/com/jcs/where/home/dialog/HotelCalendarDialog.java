package com.jcs.where.home.dialog;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcs.where.R;
import com.jcs.where.base.BaseDialog;
import com.jcs.where.adapter.HotelCalendarAdapter;
import com.jcs.where.adapter.HotelCalendarAdapter.HotelCalendarBean;
import com.jcs.where.home.decoration.HotelCalendarItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class HotelCalendarDialog extends BaseDialog {

    private ImageView closeIv;
    private RecyclerView mRecycler;
    private List<HotelCalendarBean> mBeans;
    private HotelCalendarAdapter mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.dialog_hotel_calendar;
    }

    @Override
    protected int getHeight() {
        return 684;
    }

    @Override
    protected void initView(View view) {
        mRecycler = view.findViewById(R.id.calendarRecycler);
        closeIv = view.findViewById(R.id.close);
    }

    @Override
    protected void initData() {
        initCalendar();
        mAdapter = new HotelCalendarAdapter(R.layout.item_hotel_calendar_dialog_header, R.layout.item_hotel_calendar_dialog_child, mBeans);
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
        mRecycler.addItemDecoration(new HotelCalendarItemDecoration());

    }

    @Override
    protected void bindListener() {
        closeIv.setOnClickListener(view -> dismiss());
    }

    @Override
    protected boolean isTransparent() {
        return true;
    }

    private void initCalendar() {
        Calendar instance = Calendar.getInstance();
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();

        instance.set(Calendar.YEAR, 2021);
        instance.set(Calendar.MONTH, 11);
        int max = instance.getActualMaximum(Calendar.DATE);
        start.set(Calendar.YEAR, 2020);
        start.set(Calendar.MONTH, 11);
        start.set(Calendar.DAY_OF_MONTH, 1);

        end.set(Calendar.YEAR, 2021);
        end.set(Calendar.MONTH, 11);
        end.set(Calendar.DAY_OF_MONTH, max);

        mBeans = new ArrayList<>();


        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月");
        int tempMonth = -1;
        int tempYear = 0;
        while (start.getTime().getTime() <= end.getTime().getTime()) {
            if (tempMonth == -1 || start.get(Calendar.YEAR) > tempYear || start.get(Calendar.MONTH) - tempMonth == 1) {
                tempMonth = start.get(Calendar.MONTH);
                tempYear = start.get(Calendar.YEAR);
                HotelCalendarBean header = new HotelCalendarBean();
                header.setShowDate(sf.format(start.getTime()));
                header.setIsHeader(true);
                mBeans.add(header);

                int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
                for (int ofWeek = dayOfWeek - 1; ofWeek > 0; ofWeek--) {
                    mBeans.add(new HotelCalendarBean());
                }
            }

            HotelCalendarBean day = new HotelCalendarBean();
            day.setDay(start.get(Calendar.DAY_OF_MONTH));
            mBeans.add(day);
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}
