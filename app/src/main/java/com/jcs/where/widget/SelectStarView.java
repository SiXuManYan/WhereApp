package com.jcs.where.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcs.where.R;
import com.jcs.where.utils.DimenUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * create by zyf on 2021/2/20 7:59 下午
 */
public class SelectStarView extends LinearLayout {

    /**
     * 评级最多五星
     */
    private final int MAX_STAR_NUM = 5;

    /**
     * 评级星星数量（黄色）
     */
    private double mStarNum = 0;

    private SelectStarListener mListener;
    private List<String> mSatisfaction;

    public SelectStarView(Context context) {
        this(context, null);
    }

    public SelectStarView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectStarView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        Log.e("SelectStarView", "SelectStarView: " + "-----");
        addStar();
        initSatisfaction();
    }

    private void initSatisfaction() {
        mSatisfaction = new ArrayList<>();
        mSatisfaction.add(getContext().getString(R.string.very_unsatisfactory));
        mSatisfaction.add(getContext().getString(R.string.unsatisfactory));
        mSatisfaction.add(getContext().getString(R.string.commonly));
        mSatisfaction.add(getContext().getString(R.string.satisfied));
        mSatisfaction.add(getContext().getString(R.string.very_satisfied));
    }

    private void addStar() {
        removeAllViews();
        for (int i = 0; i < MAX_STAR_NUM; i++) {
            Log.e("SelectStarView", "addStar: " + "-----" + i);
            ImageView star = new ImageView(getContext());
            star.setTag(i);
            star.setOnClickListener(this::onStarClicked);
            star.setImageResource(R.mipmap.ic_star_yellow);
            LinearLayout.LayoutParams lp = new LayoutParams(DimenUtil.toDp(getContext(), 25), DimenUtil.toDp(getContext(), 25));
            if (i > 0) {
                lp.leftMargin = DimenUtil.toDp(getContext(), 15);
            }
            star.setLayoutParams(lp);
            addView(star);
        }
    }

    private void updateStar(int clicked) {
        for (int i = 0; i < MAX_STAR_NUM; i++) {
            ImageView star = (ImageView) getChildAt(i);
            if (i > clicked) {
                star.setImageResource(R.mipmap.ic_star_grey);
            } else {
                star.setImageResource(R.mipmap.ic_star_yellow);
            }
        }
    }

    private void onStarClicked(View view) {
        int clicked = (int) view.getTag();
        updateStar(clicked);
        int score = clicked + 1;
        mListener.onStarSelect(score, mSatisfaction.get(clicked));
    }

    public void setListener(SelectStarListener listener) {
        this.mListener = listener;
    }

    public void setStartNum(double startNum) {
        this.mStarNum = startNum;
        addStar();
        invalidate();
    }

    public interface SelectStarListener {
        void onStarSelect(int score, String satisfaction);
    }
}
