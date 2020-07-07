package co.tton.android.base.utils;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

public class BitmapUtils {

    public static int[] getSize(Resources resources, int redIs) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, redIs, options);
        return new int[]{options.outWidth, options.outHeight};
    }
}
