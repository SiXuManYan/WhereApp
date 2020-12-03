package com.jcs.where.home.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcs.where.base.BaseItemDecoration;

public class HomeModulesItemDecoration extends BaseItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) > 4) {
            outRect.top = getPxFromDp(view.getContext(), 18);
        }
    }
}
