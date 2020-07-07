package co.tton.android.base.utils;

import android.app.Activity;
import android.view.View;

public class V {

    @SuppressWarnings("unchecked")
    public static <T extends View> T f(Activity activity, int viewId) {
        return (T) activity.findViewById(viewId);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T f(View view, int viewId) {
        return (T) view.findViewById(viewId);
    }
}
