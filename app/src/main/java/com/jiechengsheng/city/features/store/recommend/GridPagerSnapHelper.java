package com.jiechengsheng.city.features.store.recommend;

import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


/**
 * Created by Wangsw  2021/6/2 15:19.
 */

public class GridPagerSnapHelper extends SnapHelper {
    @Nullable
    private OrientationHelper mVerticalHelper;
    @Nullable
    private OrientationHelper mHorizontalHelper;
    private RecyclerView recyclerView;
    private int rowCount = 1;
    private int columCount = 1;


    /**
     * @param row    行
     * @param column 列
     */
    public GridPagerSnapHelper(int row, int column) {
        this.rowCount = row;
        this.columCount = column;

    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = this.distanceToStart(layoutManager, targetView, this.getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = this.distanceToStart(layoutManager, targetView, this.getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }

        return out;
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return this.findStartSnapView(layoutManager, this.getVerticalHelper(layoutManager));
        } else {
            return layoutManager.canScrollHorizontally() ? this.findStartSnapView(layoutManager, this.getHorizontalHelper(layoutManager)) : null;
        }
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return -1;
        } else {
            View mStartMostChildView = null;
            if (layoutManager.canScrollVertically()) {
                mStartMostChildView = this.findStartView(layoutManager, this.getVerticalHelper(layoutManager));
            } else if (layoutManager.canScrollHorizontally()) {
                mStartMostChildView = this.findStartView(layoutManager, this.getHorizontalHelper(layoutManager));
            }

            if (mStartMostChildView == null) {
                return -1;
            } else {
                int centerPosition = layoutManager.getPosition(mStartMostChildView);
                if (centerPosition == -1) {
                    return -1;
                } else {

                    //　计算当前页面索引
                    int pagerIndex = centerPosition / (rowCount * columCount);
                    // 是否滑向下一页
                    boolean forwardDirection;
                    if (layoutManager.canScrollHorizontally()) {
                        forwardDirection = velocityX > 0;
                    } else {
                        forwardDirection = velocityY > 0;
                    }

                    //　条目是否是翻转模式
                    boolean reverseLayout = false;
                    if (layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) {
                        RecyclerView.SmoothScroller.ScrollVectorProvider vectorProvider = (RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager;
                        PointF vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1);
                        if (vectorForEnd != null) {
                            reverseLayout = vectorForEnd.x < 0.0F || vectorForEnd.y < 0.0F;
                        }
                    }
                    int targetPosition = -1;
                    if (reverseLayout) {
                        targetPosition = (forwardDirection ? (pagerIndex - 1) * (rowCount * columCount) : (pagerIndex) * (rowCount * columCount));
                    } else {
                        targetPosition = (forwardDirection ? (pagerIndex + 1) * (rowCount * columCount) : (pagerIndex) * (rowCount * columCount));
                    }
                    return targetPosition;
                }
            }
        }
    }

    @Override
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        return !(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.recyclerView.getContext()) {
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                int[] snapDistances = GridPagerSnapHelper.this.calculateDistanceToFinalSnap(GridPagerSnapHelper.this.recyclerView.getLayoutManager(), targetView);
                int dx = snapDistances[0];
                int dy = snapDistances[1];
                int time = this.calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
                if (time > 0) {
                    action.update(dx, dy, time, this.mDecelerateInterpolator);
                }

            }

            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 100.0F / (float) displayMetrics.densityDpi;
            }

            protected int calculateTimeForScrolling(int dx) {
                return Math.min(100, super.calculateTimeForScrolling(dx));
            }
        };
    }

    private int distanceToStart(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView, OrientationHelper helper) {
        int childStart = helper.getDecoratedStart(targetView);
        int containerStart;
        if (layoutManager.getClipToPadding()) {
            containerStart = helper.getStartAfterPadding();
        } else {
            containerStart = 0;
        }

        return childStart - containerStart;
    }

    @Nullable
    private View findStartSnapView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        } else {
            View closestChild = null;
            int start;
            if (layoutManager.getClipToPadding()) {
                start = helper.getStartAfterPadding();
            } else {
                start = 0;
            }

            int absClosest = 2147483647;

            for (int i = 0; i < childCount; ++i) {
                View child = layoutManager.getChildAt(i);
                int childStart = helper.getDecoratedStart(child);
                int absDistance = Math.abs(childStart - start);
                if (absDistance < absClosest) {
                    absClosest = absDistance;
                    closestChild = child;
                }
            }

            return closestChild;
        }
    }

    @Nullable
    private View findStartView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        } else {
            View closestChild = null;
            int startest = 2147483647;

            for (int i = 0; i < childCount; ++i) {
                View child = layoutManager.getChildAt(i);
                int childStart = helper.getDecoratedStart(child);
                if (childStart < startest) {
                    startest = childStart;
                    closestChild = child;
                }
            }

            return closestChild;
        }
    }

    @NonNull
    private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (this.mVerticalHelper == null || this.mVerticalHelper.getLayoutManager() != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }

        return this.mVerticalHelper;
    }

    @NonNull
    private OrientationHelper getHorizontalHelper(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (this.mHorizontalHelper == null || this.mHorizontalHelper.getLayoutManager() != layoutManager) {
            this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }

        return this.mHorizontalHelper;
    }
}