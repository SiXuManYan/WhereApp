package com.jcs.where.features.home

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.ViewConfiguration
import android.view.MotionEvent

/**
 * Created by Wangsw  2022/1/26 11:05.
 */
class HomeSwipeRefreshLayout : SwipeRefreshLayout {

    private var mTouchSlop = 0
    private var mPrevX = 0f

    constructor(context: Context) : super(context) {
        //判断用户在进行滑动操作的最小距离
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //判断用户在进行滑动操作的最小距离
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mPrevX = MotionEvent.obtain(event).x
            MotionEvent.ACTION_MOVE -> {
                val eventX = event.x
                //获取水平移动距离
                val xDiff = Math.abs(eventX - mPrevX)
                //当水平移动距离大于滑动操作的最小距离的时候就认为进行了横向滑动
                //不进行事件拦截,并将这个事件交给子View处理
                if (xDiff > mTouchSlop) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }
}