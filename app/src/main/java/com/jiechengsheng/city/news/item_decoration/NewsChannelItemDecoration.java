package com.jiechengsheng.city.news.item_decoration;

import android.graphics.Rect;
import android.view.View;

import com.jiechengsheng.city.base.BaseItemDecoration;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by zyf on 2021/1/18 11:23 下午
 */
public class NewsChannelItemDecoration extends BaseItemDecoration {

    private final int mDividerHeight = 10;

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        outRect.top = getPxFromDp(parent.getContext(), mDividerHeight);
    }

}
