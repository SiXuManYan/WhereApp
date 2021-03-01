package com.jcs.where.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jcs.where.R;
import com.jcs.where.utils.GlideUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 一般页面顶部的轮播图
 * create by zyf on 2021/1/2 8:35 PM
 */
public class JcsBanner extends ConstraintLayout {
    public static final String DEFAULT = "default";
    private ViewPager mViewPager;
    private Adapter mAdapter;
    private List<String> mPicData;
    private List<ImageView> mDots;
    private GradientDrawable mGradientDrawable;
    private int mCurrentPosition = 0;

    public JcsBanner(@NonNull Context context) {
        this(context, null);
    }

    public JcsBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JcsBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_jcs_banner, this);
        mViewPager = view.findViewById(R.id.viewPager);
        mAdapter = new Adapter();
        mPicData = new ArrayList<>();
        mDots = new ArrayList<>();
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageView lastDot = mDots.get(mCurrentPosition);
                lastDot.setBackgroundResource(R.drawable.dot_normal);
                ImageView dot = mDots.get(position);
                dot.setBackgroundResource(R.drawable.dot_selected);
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mGradientDrawable = new GradientDrawable();
        // 圆角矩形
        mGradientDrawable.setShape(GradientDrawable.RECTANGLE);
        // 圆角
        mGradientDrawable.setCornerRadius(getPxFromDp(8));
        // 颜色
        mGradientDrawable.setColor(Color.parseColor("#FFFFFF"));
        // 这句话一定不要忘记，不然没效果
        setBackground(mGradientDrawable);
    }

    public void setPicData(List<String> picData) {
        this.mPicData.clear();
        this.mPicData.addAll(picData);
        addDot(picData.size());
        mAdapter.notifyDataSetChanged();
    }

    private void addDot(int size) {
        ImageView lastDot = null;
        // 只有一个图片的话不需要展示dot
        if (size == 1) {
            return;
        }
        for (int i = 0; i < size; i++) {
            ImageView dot = new ImageView(getContext());
            dot.setId(i);
            ConstraintLayout.LayoutParams lp = new LayoutParams(getPxFromDp(5), getPxFromDp(5));
            if (i == 0) {
                // 第一个
                lp.startToStart = this.getId();

                // 设置为链头
                lp.horizontalChainStyle = LayoutParams.CHAIN_PACKED;
                // 第一个dot 默认为选中背景
                dot.setBackgroundResource(R.drawable.dot_selected);
            } else {
                lastDot = mDots.get(i - 1);

                // 设置普通状态的 dot 背景
                dot.setBackgroundResource(R.drawable.dot_normal);

                // 中间部分 设置 start 和 上一个 dot 的end
                lp.startToEnd = lastDot.getId();
                ConstraintLayout.LayoutParams lastLp = (LayoutParams) lastDot.getLayoutParams();
                lastLp.endToStart = dot.getId();
                lastDot.setLayoutParams(lastLp);
                lp.leftMargin = getPxFromDp(5);

                if (i == size - 1) {
                    // 最后一个 设置 end
                    lp.endToEnd = this.getId();
                }
            }

            lp.bottomToBottom = this.getId();
            lp.bottomMargin = getPxFromDp(15);

            dot.setLayoutParams(lp);
            mDots.add(dot);
            addView(dot);
        }
    }

    public void addPicUrl(String url) {
        if (this.mPicData == null) {
            this.mPicData = new ArrayList<>();
        }

        mPicData.add(url);
    }

    protected int getPxFromDp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPicData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.page_banner, container, false);
            view.setTag(position);
            RoundedImageView photoIv = view.findViewById(R.id.iconIv);
            String picUrl = mPicData.get(position);
            if (picUrl.equals(DEFAULT)) {
                photoIv.setImageResource(R.mipmap.ic_glide_default);
            } else {
                GlideUtil.load(getContext(), picUrl, photoIv);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
