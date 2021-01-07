package com.jcs.where.view.popup;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PopupConstraintLayout extends ConstraintLayout {
    private PopupConstraintLayoutAdapter mAdapter;
    private View mShadowView;
    private boolean mIsShow = false;
    private boolean mIsSliding = false;

    private int mAlphaMin = 80;

    private ValueAnimator mSlideTop = null;
    private ValueAnimator mSlideBottom = null;

    public PopupConstraintLayout(@NonNull Context context) {
        this(context, null);
    }

    public PopupConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInit(context);
    }

    public void mInit(Context context) {
        mShadowView = new View(context);

    }

    private void mDeployAnimator() {

        mSlideTop = ValueAnimator.ofInt(mAdapter.getMinHeight(), mAdapter.getMaxHeight());
        mSlideTop.addUpdateListener(this::topUpdateListener);
        mSlideTop.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                changeSlideStatus();
                mShadowView.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                changeSlideStatus();
                mAdapter.onShowCompleted();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mSlideTop.setDuration(mAdapter.getDuration());


        mSlideBottom = ValueAnimator.ofInt(mAdapter.getMaxHeight(), mAdapter.getMinHeight());
        mSlideBottom.addUpdateListener(this::bottomUpdateListener);
        mSlideBottom.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                changeSlideStatus();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mShadowView.setVisibility(View.GONE);
                changeSlideStatus();
                mAdapter.onHideCompleted();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mSlideBottom.setDuration(mAdapter.getDuration());

    }

    private void topUpdateListener(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        getLayoutParams().height = value;
        requestLayout();

        if (mAdapter.showShadow()) {
            mShadowView.setBackgroundColor(Color.argb(mAlphaMin * value / mAdapter.getMaxHeight(), 0, 0, 0));
        }
    }

    private void bottomUpdateListener(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        getLayoutParams().height = value;
        if (value == mAdapter.getMinHeight() && mAdapter.isGoneAfterBottom()) {
            setVisibility(View.GONE);
        }
        requestLayout();
        if (mAdapter.showShadow()) {
            if (value == mAdapter.getMinHeight()) {
                mShadowView.setBackgroundColor(Color.argb(0, 0, 0, 0));
            } else {
                mShadowView.setBackgroundColor(Color.argb(mAlphaMin * value / getMaxHeight(), 0, 0, 0));
            }
        }

        requestLayout();

        if (mAdapter.showShadow()) {
            mShadowView.setBackgroundColor(Color.argb(mAlphaMin * value / mAdapter.getMaxHeight(), 0, 0, 0));
        }
    }


    private void mDeployShadow() {
        if (mAdapter.showShadow()) {

            ViewGroup viewGroup = (ViewGroup) getParent();
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            layoutParams.topToBottom = this.getId();
            layoutParams.bottomToBottom = viewGroup.getId();
            mShadowView.setLayoutParams(layoutParams);
            mShadowView.setVisibility(GONE);

            viewGroup.addView(mShadowView, viewGroup.getChildCount());
            mShadowView.setOnClickListener(this::onShadowViewClicked);
        }
    }

    public void onShadowViewClicked(View view) {
        if (mAdapter.clickOutSideClose()) {
            hideWithAnim();
        }
    }

    private void changeSlideStatus() {
        mIsSliding = !mIsSliding;
    }

    private void changeShowStatus() {
        mIsShow = !mIsShow;
    }

    public boolean isShow(){
        return mIsShow;
    }

    public void showOrHide() {
        if (mIsShow) {
            if (mAdapter.enableAnim()) {
                hideWithAnim();
            } else {
                hide();
            }
        } else {
            if (mAdapter.enableAnim()) {
                showWithAnim();
            } else {
                show();
            }
        }
    }

    public void show() {
        mShadowView.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        changeShowStatus();
    }

    public void hide() {
        mShadowView.setVisibility(GONE);
        setVisibility(GONE);
        changeShowStatus();
    }

    public void showWithAnim() {
        if (!mIsShow && !mIsSliding && mSlideTop != null) {
            mSlideTop.start();
            changeShowStatus();
        }
    }

    public void hideWithAnim() {
        if (mIsShow && !mIsSliding && mSlideBottom != null) {
            mSlideBottom.start();
            changeShowStatus();
        }
    }


    private void mDeployDefaultListener() {
        setOnClickListener(v -> {

        });
    }

    public void setAdapter(PopupConstraintLayoutAdapter adapter) {
        mAdapter = adapter;

        //配置动画
        mDeployAnimator();

        if (mAdapter.enableAnim()) {
            //配置阴影点击事件
            mDeployShadow();
        }

        //配置点击默认点击时间，否则点击时间会穿过Constraint，使得Shadow响应
        mDeployDefaultListener();
    }
}

