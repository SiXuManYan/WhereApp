package com.jcs.where.home.decoration;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.jcs.where.base.BaseItemDecoration;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeModulesItemDecoration extends BaseItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) > 4) {
            outRect.top = getPxFromDp(view.getContext(), 18);
        }
    }
}
