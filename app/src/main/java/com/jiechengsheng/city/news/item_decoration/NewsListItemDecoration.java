package com.jiechengsheng.city.news.item_decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.jiechengsheng.city.base.BaseItemDecoration;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by zyf on 2021/1/18 11:23 下午
 */
public class NewsListItemDecoration extends BaseItemDecoration {

    private final int mDividerHeight = 10;
    private final Paint mPaint;


    public NewsListItemDecoration() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#f5f5f5"));
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position > 0){
            outRect.top = getPxFromDp(parent.getContext(), 10);
        }
    }

    @Override
    public void onDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            // recyclerView是否设置了paddingLeft和paddingRight
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                // divider的top 应该是 item的bottom 加上 marginBottom 再加上 Y方向上的位移
                final int top = child.getBottom() + params.bottomMargin +
                        Math.round(ViewCompat.getTranslationY(child));
                // divider的bottom就是top加上divider的高度了
                final int bottom = (int) (top + mDividerHeight);
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
