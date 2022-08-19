package com.jcs.where.api.push;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

public class ExampleUtil {
    public static final String PREFS_NAME = "JPUSH_EXAMPLE";
    public static final String PREFS_DAYS = "JPUSH_EXAMPLE_DAYS";
    public static final String PREFS_START_TIME = "PREFS_START_TIME";
    public static final String PREFS_END_TIME = "PREFS_END_TIME";
    public static final String KEY_APP_KEY = "JPUSH_APPKEY";

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }
    /**
     * 只能以 “+” 或者 数字开头；后面的内容只能包含 “-” 和 数字。
     * */
    private final static String MOBILE_NUMBER_CHARS = "^[+0-9][-0-9]{1,}$";
    public static boolean isValidMobileNumber(String s) {
        if(TextUtils.isEmpty(s)) return true;
        Pattern p = Pattern.compile(MOBILE_NUMBER_CHARS);
        Matcher m = p.matcher(s);
        return m.matches();
    }
    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // 取得AppKey
    public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (NameNotFoundException e) {

        }
        return appKey;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }


    private static boolean isReadableASCII(CharSequence string){
        if (TextUtils.isEmpty(string)) return false;
        try {
            Pattern p = Pattern.compile("[\\x20-\\x7E]+");
            return p.matcher(string).matches();
        } catch (Throwable e){
            return true;
        }
    }

    public static void buildLocalNotification(Context context, String tittle, String content){
        JPushLocalNotification ln = new JPushLocalNotification();
        ln.setBuilderId(0);
        ln.setContent(content);
        ln.setTitle(tittle);
        long id = System.currentTimeMillis()/1000;
        ln.setNotificationId(id) ;
        ln.setBroadcastTime(System.currentTimeMillis());

        Map<String , Object> map = new HashMap<String, Object>() ;
        map.put("name", "jpush") ;
        map.put("test", "111") ;
        JSONObject json = new JSONObject(map) ;
        ln.setExtras(json.toString()) ;

        JPushInterface.addLocalNotification(context, ln);
        ToastUtils.showShort( "添加本地通知，notification_id:" + id);
    }
}
