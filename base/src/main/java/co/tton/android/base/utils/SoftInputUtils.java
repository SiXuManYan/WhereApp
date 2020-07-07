package co.tton.android.base.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;


public class SoftInputUtils {

//    public static void show(Context context) {
//        if (!isShow(context)) {
//            toggle(context);
//        }
//    }
//
//    public static void hide(Context context) {
//        if (isShow(context)) {
//            toggle(context);
//        }
//    }

    public static void toggle(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }

//    public static boolean isShow(Context context) {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        L.e("SoftInput#isShow: " + imm.isActive());
//        return imm.isActive();
//    }
}
