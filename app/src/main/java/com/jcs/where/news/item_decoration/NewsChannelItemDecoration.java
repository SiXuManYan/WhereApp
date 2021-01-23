package com.jcs.where.news.item_decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.jcs.where.base.BaseItemDecoration;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by zyf on 2021/1/18 11:23 下午
 */
public class NewsChannelItemDecoration extends BaseItemDecoration {

    private final int mDividerHeight = 5;

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        outRect.top = getPxFromDp(parent.getContext(), mDividerHeight);
        outRect.bottom = getPxFromDp(parent.getContext(), mDividerHeight);
    }

}
