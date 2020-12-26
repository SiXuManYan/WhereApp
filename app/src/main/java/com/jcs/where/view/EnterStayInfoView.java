package com.jcs.where.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.adapter.JcsCalendarAdapter;
import com.jcs.where.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * create by zyf on 2020/12/26 12:28 PM
 */
public class EnterStayInfoView extends ConstraintLayout {
    private View mToChooseView;
    private TextView mStartDayTv;
    private TextView mEndDayTv;
    private TextView mTotalDayTv;
    private TextView mRoomNumTv;
    private ImageView mAddIv;
    private ImageView mReduceIv;

    /**
     * 默认 1 间
     */
    private int mRoomNum = 1;
    private EnterStayInfoAdapter mAdapter;
    private JcsCalendarAdapter.CalendarBean mStartBean, mEndBean;

    public EnterStayInfoView(@NonNull Context context) {
        this(context, null);
    }

    public EnterStayInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EnterStayInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_enter_stay_info, this);
        mToChooseView = view.findViewById(R.id.toChooseDate);
        mStartDayTv = view.findViewById(R.id.startDayTv);
        mEndDayTv = view.findViewById(R.id.endDayTv);
        mTotalDayTv = view.findViewById(R.id.totalStayDayTv);
        mRoomNumTv = view.findViewById(R.id.roomNumTv);
        mAddIv = view.findViewById(R.id.roomAddIv);
        mReduceIv = view.findViewById(R.id.roomReduceIv);

        // 默认 1 间
        mRoomNumTv.setText(String.valueOf(mRoomNum));
        mToChooseView.setOnClickListener(this::onChooseViewClicked);
        mAddIv.setOnClickListener(this::onAddClicked);
        mReduceIv.setOnClickListener(this::onReduceClicked);
    }

    public EnterStayInfoView setStartAndEnd(JcsCalendarAdapter.CalendarBean startBean, JcsCalendarAdapter.CalendarBean endBean) {
        mStartBean = startBean;
        mEndBean = endBean;
        setStartDay(mStartBean.getShowMonthDayDate());
        setEndDay(mEndBean.getShowMonthDayDate());
        mTotalDayTv.setText(getTotalDay());
        return this;
    }

    public String getTotalDay() {
        long startTime = mStartBean.getTime();
        long endTime = mEndBean.getTime();
        long temp = endTime - startTime;
        long dayNum = TimeUnit.DAYS.convert(temp, TimeUnit.MILLISECONDS);
        return String.format(getContext().getString(R.string.total_night), dayNum);
    }

    public EnterStayInfoView setStartDay(String startDay) {
        mStartDayTv.setText(startDay);
        return this;
    }

    public EnterStayInfoView setEndDay(String endDay) {
        mEndDayTv.setText(endDay);
        return this;
    }

    public void setTotalDay(String totalDay) {
        mTotalDayTv.setText(totalDay);
    }

    public void setRoomNum(int roomNum) {
        mRoomNum = roomNum;
        mRoomNumTv.setText(String.valueOf(mRoomNum));
    }

    public void bindEnterStayInfoAdapter(EnterStayInfoAdapter enterStayInfoAdapter) {
        mAdapter = enterStayInfoAdapter;
    }

    public void onChooseViewClicked(View view) {
        if (mAdapter != null) {
            mAdapter.toChooseDate();
        } else {
            Log.e("EnterStayInfoView", "onChooseViewClicked: " + "please bind EnterStayInfoAdapter");
        }
    }

    public void onAddClicked(View view) {
        mRoomNum++;
        mRoomNumTv.setText(String.valueOf(mRoomNum));
    }

    public void onReduceClicked(View view) {
        if (mRoomNum < 2) {
            showToast(getContext().getString(R.string.can_not_reduce));
        } else {
            mRoomNum--;
            mRoomNumTv.setText(String.valueOf(mRoomNum));
        }
    }

    protected void showToast(String msg) {
        ToastUtils.showLong(getContext(), msg);
    }

    public interface EnterStayInfoAdapter {
        void toChooseDate();
    }
}
