package co.tton.android.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetworkUtils {

    /**
     * 判断网络是否已连接
     */
    public static boolean isConnected(Context ctx) {
        if (ctx != null) {
            try {
                ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm != null) {
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    return networkInfo != null && networkInfo.isConnected();
                }
            } catch (NoSuchFieldError e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断当前是否使用的是WIFI网络
     */
    @SuppressWarnings("deprecation")
    public static boolean isWifiConnected(Context ctx) {
        if (ctx != null) {
            try {
                ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm != null) {
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    return networkInfo != null && networkInfo.isConnected()
                            && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
                }
            } catch (NoSuchFieldError e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
