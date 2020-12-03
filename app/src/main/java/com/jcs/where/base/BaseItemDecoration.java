package com.jcs.where.base;

import android.content.Context;
import android.util.TypedValue;

import androidx.recyclerview.widget.RecyclerView;

public class BaseItemDecoration extends RecyclerView.ItemDecoration {

    protected int getPxFromDp(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
