package com.jcs.where.government.fragment;

import android.view.View;

import androidx.viewpager.widget.ViewPager;


public class ShadowTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

    private final ViewPager mViewPager;
    private float mLastOffset;
    private boolean mScalingEnabled;

    public ShadowTransformer(ViewPager viewPager) {
        mViewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
    }

    public void enableScaling(boolean enable) {
        int currentItem = mViewPager.getCurrentItem();
        View currentView = mViewPager.getChildAt(currentItem);
        if (mScalingEnabled && !enable) {
            // shrink main card
            if (currentView != null) {
                currentView.animate().scaleY(1);
                currentView.animate().scaleX(1);
            }
        } else if (!mScalingEnabled && enable) {
            // grow main card
            if (currentView != null) {
                currentView.animate().scaleY(1.1f);
                currentView.animate().scaleX(1.1f);
            }
        }

        mScalingEnabled = enable;
    }

    @Override
    public void transformPage(View page, float position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int realCurrentPosition;
        int nextPosition;
        float realOffset;
        boolean goingLeft = mLastOffset > positionOffset;

        // If we're going backwards, onPageScrolled receives the last position
        // instead of the current one
        if (goingLeft) {
            realCurrentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffset;
        } else {
            nextPosition = position + 1;
            realCurrentPosition = position;
            realOffset = positionOffset;
        }

        // Avoid crash on overscroll
        int childCount = mViewPager.getChildCount();
        if (nextPosition > childCount - 1
                || realCurrentPosition > childCount - 1) {
            return;
        }

        View currentView = mViewPager.getChildAt(realCurrentPosition);

        // This might be null if a fragment is being used
        // and the views weren't created yet
        if (currentView != null) {
            if (mScalingEnabled) {
                currentView.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
                currentView.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
            }
        }

        View nextView = mViewPager.getChildAt(nextPosition);

        // We might be scrolling fast enough so that the next (or previous) card
        // was already destroyed or a fragment might not have been created yet
        if (nextView != null) {
            if (mScalingEnabled) {
                nextView.setScaleX((float) (1 + 0.1 * (realOffset)));
                nextView.setScaleY((float) (1 + 0.1 * (realOffset)));
            }
        }

        mLastOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
