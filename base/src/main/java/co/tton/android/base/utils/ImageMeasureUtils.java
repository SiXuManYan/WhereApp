package co.tton.android.base.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class ImageMeasureUtils {
    // banner
    private static final float HOME_BANNER_RATIO = 16 / 9f;

    private static int sScreenWidth;

    public static void init(Context ctx) {
        Resources resources = ctx.getResources();
        sScreenWidth = resources.getDisplayMetrics().widthPixels;
    }

    public static int[] getBannerMeasure() {
        int[] measure = new int[2];
        measure[0] = sScreenWidth;
        measure[1] = Math.round(sScreenWidth / HOME_BANNER_RATIO);

        return measure;
    }

    public static int getHeight(int width, float radio) {
        if (width < 0) {
            width = sScreenWidth;
        }
        return Math.round(width / radio);
    }
}
