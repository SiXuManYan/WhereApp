package com.jiechengsheng.city.features.home

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

/**
 * Created by Wangsw  2021/8/11 15:10.
 *
 */
 abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    enum class State {

        /**
         * 展开
         */
        EXPANDED,

        /**
         * 折叠
         */
        COLLAPSED,


        IDLE
    }

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {

        when {
            verticalOffset == 0 -> {
                if (mCurrentState != State.EXPANDED) {
                    mCurrentState = State.EXPANDED
                    onStateChanged(appBarLayout, mCurrentState, verticalOffset)
                }
            }
            abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                if (mCurrentState != State.COLLAPSED) {
                    mCurrentState = State.COLLAPSED
                    onStateChanged(appBarLayout, mCurrentState, verticalOffset)
                }
            }
            else -> {
                if (mCurrentState != State.IDLE) {
                    mCurrentState = State.IDLE
                    onStateChanged(appBarLayout, mCurrentState, verticalOffset);
                }
            }
        }
    }

   abstract  fun onStateChanged(appBarLayout: AppBarLayout, expanded: State, verticalOffset: Int)


}