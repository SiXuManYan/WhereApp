package com.jcs.where.home.decoration;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeModulesItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) > 4) {
            outRect.top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, view.getContext().getResources().getDisplayMetrics());
        }
    }
}
