package co.tton.android.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import co.tton.android.base.R;

public class ToastUtils {

    private static Toast sToast;

    private static void initToast(Context context) {
        if (sToast == null) {
            sToast = new Toast(context);

            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.toast, null);

            sToast.setView(view);
            sToast.setGravity(Gravity.CENTER, 0, 0);
        }
    }

    public static void showShort(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context, int messageResId) {
        show(context, context.getString(messageResId), Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    public static void showLong(Context context, int messageResId) {
        show(context, context.getString(messageResId), Toast.LENGTH_LONG);
    }

    private static void show(Context context, CharSequence message, int duration) {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
        initToast(context.getApplicationContext());

        sToast.setText(message);
        sToast.setDuration(duration);
        sToast.show();
    }
}
