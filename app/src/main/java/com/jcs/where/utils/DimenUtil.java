package com.jcs.where.utils;

import android.content.Context;
import android.util.TypedValue;

public class DimenUtil {

    public static int toDp(Context context, int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
